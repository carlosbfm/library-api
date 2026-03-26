package library_api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import library_api.repository.UsuarioRepository;
import library_api.security.UsuarioDetailsImpl;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return usuarioRepository.findByLogin(username)
                .map(usuario -> new UsuarioDetailsImpl(usuario))
                .orElseThrow(() -> new UsernameNotFoundException("Acesso negado: Matrícula " + username + " não encontrada no sistema."));
    }
}