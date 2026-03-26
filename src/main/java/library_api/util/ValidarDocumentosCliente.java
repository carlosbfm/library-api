package library_api.util;

import library_api.dto.request.ClienteRequestDTO;
import library_api.model.enums.TipoCliente;

public class ValidarDocumentosCliente {
    
    public static void validarDocumentosObrigatorios(ClienteRequestDTO dto) {
        if(dto.tipoCliente() == TipoCliente.PF && dto.cpf() == null){
            throw new IllegalArgumentException("Para Pessoa Física, o CPF é obrigatório.");
        }
        if(dto.tipoCliente() == TipoCliente.PJ && dto.cnpj() == null){
            throw new IllegalArgumentException("Para Pessoa Jurídica, o CNPJ é obrigatório.");
        }
    }
    // Construtor privado para impedir que alguém dê um "new ValidarDocumentos()"
    private ValidarDocumentosCliente() {}
}
