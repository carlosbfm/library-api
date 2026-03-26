package library_api.service; // Ou library_api.security, caso tenha movido a classe

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException; 

import library_api.model.entity.usuario.UsuarioEntity; 

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(UsuarioEntity usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            
            return JWT.create()
                    .withIssuer("Library API") 
                    .withSubject(usuario.getLogin()) 
                    .withClaim("role", usuario.getRoles()) 
                    .withExpiresAt(dataExpiracao()) 
                    .sign(algoritmo); 
                    
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validarToken(String token) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("Library API") 
                    .build()
                    .verify(token)
                    .getSubject(); 

        } catch (JWTVerificationException exception){
            return ""; 
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}