package library_api.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class GeradorCredenciais {

    private final SecureRandom secureRandom = new SecureRandom();

    public String gerarMatricula() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(secureRandom.nextInt(10)); 
        }
        return sb.toString();
    }

    public String gerarSenha(int tamanho, boolean apenasNumeros) {
        String dicionario = apenasNumeros 
                ? "0123456789" 
                : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
                
        StringBuilder senha = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            senha.append(dicionario.charAt(secureRandom.nextInt(dicionario.length())));
        }
        return senha.toString();
    }
}
