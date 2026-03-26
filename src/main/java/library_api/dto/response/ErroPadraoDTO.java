package library_api.dto.response;

import java.time.LocalDateTime;

public record ErroPadraoDTO(
        LocalDateTime timestamp,
        Integer status,
        String erro,
        String mensagem,
        String path
) {
    public ErroPadraoDTO(Integer status, String erro, String mensagem, String path) {
        this(LocalDateTime.now(), status, erro, mensagem, path);
    }
}