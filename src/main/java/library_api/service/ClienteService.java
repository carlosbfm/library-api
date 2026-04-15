package library_api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import library_api.dto.request.ClienteRequestDTO;
import library_api.dto.response.ClienteResponseDTO;
import library_api.exception.RecursoNaoEncontradoException;
import library_api.exception.RegraDeNegocioException;
import library_api.mapper.ClienteMapper;
import library_api.model.entity.cliente.ClienteEntity;
import library_api.model.enums.TipoCliente;
import library_api.repository.ClienteRepository;
import library_api.util.AjusteDocumentos;
import library_api.util.DocumentoUtil;
import library_api.util.GeradorDeCodigos;
import library_api.util.ValidarDocumentosCliente;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final GeradorDeCodigos geradorDeCodigos;


    @Transactional
    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto){
        ClienteEntity cliente = clienteMapper.toEntity(dto);

        ValidarDocumentosCliente.validarDocumentosObrigatorios(dto);

        String cpfLimpo = dto.cpf() != null ? DocumentoUtil.limpaFormatacao(dto.cpf()) : null;
        String cnpjLimpo = dto.cnpj() != null ? DocumentoUtil.limpaFormatacao(dto.cnpj()) : null;

        if(cpfLimpo != null && clienteRepository.existsByCpf(cpfLimpo)){
            throw new RegraDeNegocioException("Já existe um cliente cadastrado com o CPF: " + dto.cpf());
        }
        if(cnpjLimpo != null && clienteRepository.existsByCnpj(cnpjLimpo)){
            throw new RegraDeNegocioException("Já existe um cliente cadastrado com o CNPJ: " + dto.cnpj());
        }

        AjusteDocumentos.ajustarDocumentosPorTipo(cliente, dto);

        Long codCliente;
        do{
            codCliente = geradorDeCodigos.gerarCodCliente();
        }while(clienteRepository.existsByCodCliente(codCliente));

        cliente.setCodCliente(codCliente);
        cliente.setData(LocalDate.now());

        ClienteEntity clienteSalvo = clienteRepository.save(cliente);
        return clienteMapper.toDto(clienteSalvo);
    }

    public List<ClienteResponseDTO> buscarPorNome(String nome){
        List<ClienteEntity> clientesEncontrados = clienteRepository.findByNomeClienteContainingIgnoreCase(nome);

        return clientesEncontrados.stream()
                .map(clienteMapper::toDto)
                .toList();
    }

    public List<ClienteResponseDTO> buscarTipoCliente(TipoCliente tipoCliente){
        List<ClienteEntity> clientesEncontrados = clienteRepository.findByTipoCliente(tipoCliente);

        return clientesEncontrados.stream()
                .map(clienteMapper::toDto)
                .toList();
    }

    public ClienteResponseDTO buscarPorCpf(String cpf){
        String cpfLimpo = DocumentoUtil.limpaFormatacao(cpf);

        ClienteEntity clienteEncontrado = clienteRepository.findByCpf(cpfLimpo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o CPF: " + cpf));
        
        return clienteMapper.toDto(clienteEncontrado);
    }

    public ClienteResponseDTO buscarPorCnpj(String cnpj){
        String cnpjLimpo = DocumentoUtil.limpaFormatacao(cnpj);

        ClienteEntity clienteEncontrado = clienteRepository.findByCnpj(cnpjLimpo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o CNPJ: " + cnpj));
        
        return clienteMapper.toDto(clienteEncontrado);
    }

    public ClienteResponseDTO buscarPorCodCliente(Long codCliente){
        ClienteEntity clienteEncontrado = clienteRepository.findById(codCliente).
        orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com o Código: " + codCliente));

        return clienteMapper.toDto(clienteEncontrado);
    }

    @Transactional
    public ClienteResponseDTO atualizar(Long codCliente, ClienteRequestDTO dto){
        ClienteEntity clienteAtual = clienteRepository.findById(codCliente)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum cliente encontrado com código: " + codCliente));

        ValidarDocumentosCliente.validarDocumentosObrigatorios(dto);

        String cpfLimpo = dto.cpf() != null ? DocumentoUtil.limpaFormatacao(dto.cpf()) : null;
        String cnpjLimpo = dto.cnpj() != null ? DocumentoUtil.limpaFormatacao(dto.cnpj()) : null;

        if(cpfLimpo != null && !cpfLimpo.equals(clienteAtual.getCpf()) && clienteRepository.existsByCpf(cpfLimpo)){
            throw new RegraDeNegocioException("Já existe um cliente cadastrado com o CPF: " + dto.cpf());
        }
        if(cnpjLimpo != null && !cnpjLimpo.equals(clienteAtual.getCnpj()) && clienteRepository.existsByCnpj(cnpjLimpo)){
            throw new RegraDeNegocioException("Já existe um cliente cadastrado com o CNPJ: " + dto.cnpj());
        }

        AjusteDocumentos.ajustarDocumentosPorTipo(clienteAtual, dto);

        clienteAtual.setTipoCliente(dto.tipoCliente());
        clienteAtual.setNomeCliente(dto.nomeCliente());

        return clienteMapper.toDto(clienteAtual);
    }

    @Transactional
    public void deletar(Long codCliente){
        ClienteEntity clienteDeletado =  clienteRepository.findById(codCliente) 
            .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com código: " + codCliente));
        clienteRepository.delete(clienteDeletado);
    }
}
