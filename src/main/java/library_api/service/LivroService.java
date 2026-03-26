package library_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import library_api.dto.request.LivroRequestDTO;
import library_api.dto.response.LivroResponseDTO;
import library_api.mapper.LivroMapper;
import library_api.model.entity.livro.LivroEntity;
import library_api.model.enums.StatusLivro;
import library_api.repository.LivroRepository;
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

        Long codigoGerado;
        do {
            codigoGerado = geradorDeCodigos.gerarCodLivro();
        } while (livroRepository.existsById(codigoGerado));

        livro.setCodLivro(codigoGerado);
        livro.setStatus(StatusLivro.DISPONIVEL);

        LivroEntity livroSalvo = livroRepository.save(livro);
        return livroMapper.toDto(livroSalvo);
    }

    public LivroResponseDTO buscarPorCodigo(Long codLivro) {
        LivroEntity buscaCod = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com o código: " + codLivro));

        return livroMapper.toDto(buscaCod);
    }

    public List<LivroResponseDTO> buscarPorTitulo(String titulo){
        List<LivroEntity> livrosEncontrados = livroRepository.findByTituloLivroContainingIgnoreCase(titulo);

        if(livrosEncontrados.isEmpty()){
            throw new RuntimeException("Nenhum livro encontrado com título: " + titulo);
        }

        return livrosEncontrados.stream()
                .map(livroMapper::toDto)
                .toList();
    }

    public List<LivroResponseDTO> buscarPorAutor(String autor){
        List<LivroEntity> autoresEncontrados = livroRepository.findByAutorContainingIgnoreCase(autor);

        if(autoresEncontrados.isEmpty()){
            throw new RuntimeException("Nenhum livro encontrado com autor: " + autor);
        }

        return autoresEncontrados.stream()
                .map(livroMapper::toDto)
                .toList();
    }

    public List<LivroResponseDTO> buscarPorGenero(String genero){
        List<LivroEntity> generosEncontrados = livroRepository.findByGeneroContainingIgnoreCase(genero);

        if(generosEncontrados.isEmpty()){
            throw new RuntimeException("Nenhum livro encontrado com o gênero: " + genero);
        }

        return generosEncontrados.stream()
                .map(livroMapper::toDto)
                .toList();
    }

    public List<LivroResponseDTO> buscarPorIsbn(String isbn){
        List<LivroEntity> isbnEncontradas = livroRepository.findByIsbn(isbn);

        if(isbnEncontradas.isEmpty()){
            throw new RuntimeException("Nenhum livro encontrado com a isbn: " + isbn);
        }

        return isbnEncontradas.stream()
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
        LivroEntity entidade = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com o código: " + codLivro));

        entidade.setTituloLivro(dto.tituloLivro());
        entidade.setAutor(dto.autor());
        entidade.setGenero(dto.genero());
        entidade.setIsbn(dto.isbn());
        entidade.setData(dto.data());

        LivroEntity livroAtualizado = livroRepository.save(entidade);
        return livroMapper.toDto(livroAtualizado);
    }

    @Transactional
    public void reportarPerda(Long codLivro) {
        LivroEntity livro = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com o código: " + codLivro));
        if(livro.getStatus() == StatusLivro.PERDIDO){
            throw new RuntimeException("Este Livro Já consta como perdido no sistema");
        }
        
        livro.setStatus(StatusLivro.PERDIDO);
        livroRepository.save(livro);
    }

    @Transactional
    public void alterarPraEmprestado(Long codLivro) {
        LivroEntity livro = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com o código: " + codLivro));
        if(livro.getStatus() != StatusLivro.DISPONIVEL ){
            throw new RuntimeException("Este Livro Já consta como emprestado no sistema");
        }
        livro.setStatus(StatusLivro.EMPRESTADO);
        livroRepository.save(livro);
    }

    @Transactional
    public void reportarLivroDanificado(Long codLivro) {
        LivroEntity livro = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com o código: " + codLivro));
        if(livro.getStatus() == StatusLivro.DANIFICADO){
            throw new RuntimeException("Este Livro Já consta como danificado no sistema");
        }
        
        livro.setStatus(StatusLivro.DANIFICADO);
        livroRepository.save(livro);
    }

    @Transactional
    public void alterarPraDisponivel(Long codLivro) {
        LivroEntity livro = livroRepository.findById(codLivro)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com o código: " + codLivro));
        if(livro.getStatus() == StatusLivro.DISPONIVEL){
            throw new RuntimeException("Este Livro Já consta como disponível no sistema");
        }
        
        livro.setStatus(StatusLivro.DISPONIVEL);
        livroRepository.save(livro);
    }

    

    @Transactional
    public void deletar(Long codLivro){
        LivroEntity livroEncontrado =  livroRepository.findById(codLivro) 
            .orElseThrow(() -> new RuntimeException("Nenhum livro encontrado com código: " + codLivro));
        livroRepository.delete(livroEncontrado);
    }
}