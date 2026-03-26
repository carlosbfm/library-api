package library_api.util;

import library_api.dto.request.ClienteRequestDTO;
import library_api.model.entity.cliente.ClienteEntity;
import library_api.model.enums.TipoCliente;

public class AjusteDocumentos {
    
    public static void ajustarDocumentosPorTipo(ClienteEntity cliente, ClienteRequestDTO dto) {
        
        if (dto.tipoCliente() == TipoCliente.PF) {
            String cpfLimpo = (dto.cpf() != null) ? dto.cpf().replaceAll("\\D", "") : null;
            cliente.setCpf(cpfLimpo);
            cliente.setCnpj(null);
            
        } else if (dto.tipoCliente() == TipoCliente.PJ) {
            String cnpjLimpo = (dto.cnpj() != null) ? dto.cnpj().replaceAll("\\D", "") : null;
            cliente.setCnpj(cnpjLimpo);
            cliente.setCpf(null);
        }
    }

    private AjusteDocumentos(){}
}