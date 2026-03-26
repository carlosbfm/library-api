package library_api.dto.request;


import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import library_api.model.enums.TipoCliente;

public record ClienteRequestDTO(
    
    @NotBlank(message = "É obrigatório preencher o nome do cliente")
    @Size(max = 100)
    String nomeCliente,

    @CPF
    String cpf,

    @CNPJ
    String cnpj,

    @NotNull(message = "Informe o tipo de cliente")
    TipoCliente tipoCliente
    

) {}
