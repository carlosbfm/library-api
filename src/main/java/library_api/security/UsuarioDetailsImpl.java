package library_api.security;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import library_api.model.entity.usuario.UsuarioEntity;

public class UsuarioDetailsImpl implements UserDetails {
    private final UsuarioEntity usuario;

    // Construtor: Nós passamos a entidade limpa para cá
    public UsuarioDetailsImpl(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(usuario.getRoles()));
    }

    @Override
    public String getPassword() {
        String senhaDoBanco = usuario.getPassword();
        System.out.println(">>> SENHA QUE VEIO DO BANCO: " + senhaDoBanco);
        return senhaDoBanco;
    }

    @Override
    public String getUsername() {
        return usuario.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (usuario.getDataExpiracaoSenha() == null) return false;
        return !LocalDate.now().isAfter(usuario.getDataExpiracaoSenha());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    public UsuarioEntity getUsuario() {
        return this.usuario;
    }
}
