package library_api.dto.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.ISBN;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LivroRequestDTO(
    
    @NotBlank(message = "É obrigatório preencher o título do livro!")
    @Size(max = 150)
    String tituloLivro,

    @NotBlank(message = "É obrigatório preencher o Autor do livro!")
    @Size(max = 100)
    String autor,

    @Size(max = 50)
    String genero,

    @ISBN
    @Size(max = 20)
    @Pattern(regexp = "(^\\d{13}$)|(^\\d{3}-\\d{2}-\\d{3}-\\d{4}-\\d{1}$)",
        message = "A ISBN deve conter 13 números ou estar no formato XXX-XX-XXX-XXXX-X")
    String isbn,

    @NotNull(message = "É obrigatório preencher a data de lançamento do livro")
    @PastOrPresent(message = "A data de lançamento não pode está no futuro")
    LocalDate data
) {}
