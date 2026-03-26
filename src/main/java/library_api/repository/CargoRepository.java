package library_api.repository;

import java.util.Optional;
import library_api.model.entity.funcionario.Cargo;
import library_api.model.enums.TipoCargo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
    Optional<Cargo> findByNomeCargo(TipoCargo nomeCargo);
}