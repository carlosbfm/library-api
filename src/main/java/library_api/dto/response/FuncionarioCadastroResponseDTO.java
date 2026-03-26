package library_api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioCadastroResponseDTO(
        String matricula,       
        String senhaTemporaria, // <-- O segredo viaja aqui só essa vez
        String nome,
        String cpf,
        LocalDate dataAdmissao,
        String cargo,
        BigDecimal salarioBase
) {}