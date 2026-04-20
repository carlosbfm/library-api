package library_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import library_api.dto.request.LivroRequestDTO;
import library_api.dto.response.LivroResponseDTO;
import library_api.exception.RecursoNaoEncontradoException;
import library_api.exception.RegraDeNegocioException;
import library_api.mapper.LivroMapper;
import library_api.model.entity.livro.LivroEntity;
import library_api.model.enums.StatusLivro;
import library_api.repository.LivroRepository;
import library_api.util.DocumentoUtil;
import library_api.util.GeradorDeCodigos;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;
    private final GeradorDeCodigos geradorDeCodigos;

    @Transactional
    public LivroResponseDTO cadastrar(LivroRequestDTO dto) {
        LivroEntity livro = livroMapper.toEntity(dto);

        String isbnLimpo = DocumentoUtil.limpaIsbn(dto.isbn());

        if(isbnLimpo.isEmpty()){
            throw new RegraDeNegocioException("ISBN inválido. Certifique-se de digitar números válidos");
        }

        Long codigoGerado;
        do {
            codigoGerado = geradorDeCodigos.gerarCodLivro();
        } while (livroRepository.existsById(codigoGerado));

        livro.setCodLivro(codigoGerado);
        livro.setIsbn(isbnLimpo);
        livro.setStatus(StatusLivro.DISPONIVEL);

        LivroEntity livroSalvo = livroRepository.save(livro);
        return livroMapper.toDto(livroSalvo);
    }

    public LivroResponseDTO buscarPorCodigo(Long codLivro) {
        LivroEntity buscaCod = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com o código: " + codLivro));

        return livroMapper.toDto(buscaCod);
    }

    public List<LivroResponseDTO> buscarPorTitulo(String titulo) {

        if(titulo == null || titulo.trim().isEmpty()){
            return listarTodos();
        }

        return livroRepository.findByTituloLivroContainingIgnoreCase(titulo).stream()
                .map(livroMapper::toDto)
                .toList();
    }

    public List<LivroResponseDTO> buscarPorAutor(String autor) {

        if(autor == null || autor.trim().isEmpty()){
            return listarTodos();
        }

        return livroRepository.findByAutorContainingIgnoreCase(autor).stream()
                .map(livroMapper::toDto)
                .toList();
    }

    public List<LivroResponseDTO> buscarPorGenero(String genero) {

        if(genero == null || genero.trim().isEmpty()){
            return  listarTodos();
        }

        return livroRepository.findByGeneroContainingIgnoreCase(genero).stream()
                .map(livroMapper::toDto)
                .toList();
    }

    public List<LivroResponseDTO> buscarPorIsbn(String isbn) {

        if(isbn == null || isbn.trim().isEmpty()){
            return listarTodos();
        }

        String isbnLimpa = DocumentoUtil.limpaIsbn(isbn);

        if(isbnLimpa.isEmpty()){
            return List.of();
        }

        return livroRepository.findByIsbnContainingIgnoreCase(isbnLimpa).stream()
                .map(livroMapper::toDto)
                .toList();
    }

    public List<LivroResponseDTO> listarTodos() {
        return livroRepository.findAll()
                .stream()
                .map(livroMapper::toDto)
                .toList();
    }

    @Transactional
    public LivroResponseDTO atualizar(Long codLivro, LivroRequestDTO dto) {

        LivroEntity entidade = livroRepository.findByCodLivro(codLivro)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com o código: " + codLivro));

        String isbnLimpa = DocumentoUtil.limpaFormatacao(dto.isbn());

        if (isbnLimpa.isEmpty()) {
            throw new RegraDeNegocioException("ISBN inválido para atualização. Certifique-se de digitar números válidos.");
        }

        livroRepository.findByIsbn(isbnLimpa).ifPresent(livroExistente -> {
            if (!livroExistente.getCodLivro().equals(codLivro)) {
                throw new RegraDeNegocioException(
                        "Violação de Integridade: O ISBN " + isbnLimpa + " já pertence a outro livro!");
            }
        });

        livroMapper.atualizarDeDto(dto, entidade);
        entidade.setIsbn(isbnLimpa);

        return livroMapper.toDto(entidade);
    }

    @Transactional
    public void reportarPerda(Long codLivro) {
        LivroEntity livro = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com o código: " + codLivro));
        if (livro.getStatus() == StatusLivro.PERDIDO) {
            throw new RegraDeNegocioException("Este Livro Já consta como perdido no sistema");
        }

        livro.setStatus(StatusLivro.PERDIDO);
    }

    @Transactional
    public void alterarPraEmprestado(Long codLivro) {

        LivroEntity livro = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com o código: " + codLivro));

        switch (livro.getStatus()) {
            case EMPRESTADO -> throw new RegraDeNegocioException("Este Livro já consta como emprestado no sistema.");
            case PERDIDO ->
                throw new RegraDeNegocioException("Não há como emprestar esse livro, pois consta como perdido.");
            case DANIFICADO -> throw new RegraDeNegocioException("O livro está danificado e não pode ser emprestado.");
            case DISPONIVEL -> {
                livro.setStatus(StatusLivro.EMPRESTADO);
            }
            default -> throw new RegraDeNegocioException("Status de livro desconhecido ou inválido.");
        }
    }

    @Transactional
    public void reportarLivroDanificado(Long codLivro) {
        LivroEntity livro = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com o código: " + codLivro));
        if (livro.getStatus() == StatusLivro.DANIFICADO) {
            throw new RegraDeNegocioException("Este Livro Já consta como danificado no sistema");
        }

        livro.setStatus(StatusLivro.DANIFICADO);
    }

    @Transactional
    public void alterarPraDisponivel(Long codLivro) {
        LivroEntity livro = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com o código: " + codLivro));
        if (livro.getStatus() == StatusLivro.DISPONIVEL) {
            throw new RegraDeNegocioException("Este Livro Já consta como disponível no sistema");
        }

        livro.setStatus(StatusLivro.DISPONIVEL);
    }

    @Transactional
    public void deletar(Long codLivro) {
        LivroEntity livroEncontrado = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum livro encontrado com código: " + codLivro));
        livroRepository.delete(livroEncontrado);
    }
}