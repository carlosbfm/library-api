package library_api.dto.auth;

public record DadosTokenJWT(
    String token,
    String nome,
    String matricula,
    String cargo
) {
}