package library_api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioResponseDTO(
        String matricula,       
        String nome,
        String cpf,
        LocalDate dataAdmissao,
        String cargo,
        BigDecimal salarioBase
) {}