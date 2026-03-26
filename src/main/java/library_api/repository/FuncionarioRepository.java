package library_api.repository;

import library_api.model.entity.funcionario.Cargo;
import library_api.model.entity.funcionario.FuncionarioEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<FuncionarioEntity, String> {

    Optional<FuncionarioEntity> findByUsuarioLogin(String login);
    
    Optional<FuncionarioEntity> findByMatricula(String matricula);

    boolean existsByMatricula(String matricula);

    List<FuncionarioEntity> findByNomeContainingIgnoreCase(String nome);

    List<FuncionarioEntity> findByCargo(Cargo cargo);

    Optional<FuncionarioEntity> findByCpf(String cpf);
    // Essencial para barrar cadastros duplicados antes de tentar salvar
    boolean existsByCpf(String cpf);
    
}