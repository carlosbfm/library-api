package library_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import library_api.repository.UsuarioRepository;
import library_api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor 
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            var subject = tokenService.validarToken(tokenJWT);
            
            // 1. Buscamos o Optional do banco
            var usuarioOptional = repository.findByLogin(subject);

            // 2. Verificamos se o usuário realmente existe dentro da "caixa" (Optional)
            if (usuarioOptional.isPresent()) {
                var usuarioEntity = usuarioOptional.get(); 
                
                // 3. Usamos o seu UsuarioDetailsImpl para garantir a compatibilidade com o Spring
                var userDetails = new UsuarioDetailsImpl(usuarioEntity);

                // 4. Criamos o objeto de autenticação
                // O terceiro parâmetro (getAuthorities) é o que libera o seu @PreAuthorize
                var authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, 
                    null, 
                    userDetails.getAuthorities()
                );

                // 5. Autenticamos oficialmente no contexto do Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Segue o fluxo para o próximo filtro ou para o Controller
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para pegar o Token do Header "Authorization: Bearer <TOKEN>"
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null;
    }
}