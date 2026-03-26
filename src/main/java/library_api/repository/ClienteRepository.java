package library_api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import library_api.model.entity.cliente.ClienteEntity;
import library_api.model.enums.TipoCliente;

@Repository
public interface ClienteRepository extends  JpaRepository<ClienteEntity, Long> {
    
    boolean existsByCodCliente(Long codCLiente);

    Optional<ClienteEntity> findByCodCliente(Long codCliente);

    List<ClienteEntity> findByNomeClienteContainingIgnoreCase(String nomeCliente);

    List<ClienteEntity> findByTipoCliente(TipoCliente tipoCliente);

    boolean existsByCpf(String cpf);

    Optional<ClienteEntity> findByCpf(String cpf);

    boolean existsByCnpj(String cnpj);

    Optional<ClienteEntity> findByCnpj(String cnpj);

    List<ClienteEntity> findByData(LocalDate data);
}
