package library_api.dto.response;

import java.time.LocalDate;



public record ClienteResponseDTO(
    Long codCliente,
    String nomeCliente,
    String tipoCliente,
    String cpf,
    String cnpj,
    LocalDate data
) {}
