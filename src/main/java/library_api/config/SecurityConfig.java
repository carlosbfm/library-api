package library_api.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import library_api.security.SecurityFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. A MÁGICA DO CORS: Isso diz ao Spring Security para olhar as regras do método lá embaixo
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
                .csrf(csrf -> csrf.disable()) // Desabilita proteção CSRF (necessário para APIs REST / JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Não guarda sessão no servidor
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll() // Libera a rota de login para qualquer um
                        .requestMatchers(HttpMethod.POST,"/api/funcionarios/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/funcionarios/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/funcionarios/**").authenticated() // Libera o cadastro (só para testes, depois você protege!)
                        .anyRequest().authenticated() // Bloqueia todo o resto
                )
                //meu filtro antes do padrão
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // ==========================================================================================
    // CONFIGURAÇÃO DO CORS (LIBERA O NAVEGADOR PARA O ANGULAR)
    // ==========================================================================================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite que o Angular acesse a API
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); 
        // Permite todos os métodos HTTP essenciais, PRINCIPALMENTE O 'OPTIONS' que o navegador exige
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); 
        // Permite o envio do cabeçalho de Authorization (Token JWT)
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica essa regra em todas as rotas da API
        return source;
    }

    // Exporta o AuthenticationManager para podermos usá-lo no AuthController
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Você já usa o PasswordEncoder no Service, mas ele precisa ser um Bean aqui!
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}