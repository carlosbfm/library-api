package library_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import library_api.model.entity.usuario.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity,Long>{
    Optional <UsuarioEntity> findByLogin(String login);
}
