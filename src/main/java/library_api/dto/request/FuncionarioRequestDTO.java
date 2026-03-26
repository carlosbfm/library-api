package library_api.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record FuncionarioRequestDTO(
        
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100)
        String nome,

        @CPF
        @Pattern(regexp = "(^\\d{11}$)|(^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$)", 
                 message = "O CPF deve conter 11 números ou estar no formato XXX.XXX.XXX-XX")
        @NotBlank(message = "O CPF é obrigatório.")
        String cpf,

        @NotNull(message = "A data de admissão é obrigatória.")
        @PastOrPresent(message = "A data de admissão não pode ser superior à data atual.")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataAdmissao,

        @NotBlank(message = "O cargo é obrigatório.")
        String cargo,

        @NotNull(message = "O salário base é obrigatório.")
        @Positive(message = "O salário base deve ser maior que zero.")
        BigDecimal salarioBase

) {}