package library_api.dto.response;

import java.time.LocalDate;


public record LivroResponseDTO(
    Long codLivro,
    String tituloLivro,
    String autor,
    String genero,
    String isbn,
    LocalDate data,
    String status
) {}
