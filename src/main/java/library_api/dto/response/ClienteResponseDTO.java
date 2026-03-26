package library_api.dto.response;

import java.time.LocalDate;

import library_api.model.enums.TipoCliente;

public record ClienteResponseDTO(
    Long codCliente,
    String nomeCliente,
    TipoCliente tipoCliente,
    String cpf,
    String cnpj,
    LocalDate data
) {}
