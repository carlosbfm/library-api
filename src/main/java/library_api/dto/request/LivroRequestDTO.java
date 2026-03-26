package library_api.dto.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.ISBN;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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

    @Size(max = 20)
    @ISBN
    String isbn,

    @NotNull(message = "É obrigatório preencher a data de lançamento do livro")
    @PastOrPresent(message = "A data de lançamento não pode está no futuro")
    LocalDate data
) {}
