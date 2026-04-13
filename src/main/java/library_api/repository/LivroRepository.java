package library_api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import library_api.model.entity.livro.LivroEntity;

@Repository
public interface LivroRepository extends JpaRepository<LivroEntity, Long> {
    boolean existsByCodLivro(Long codLivro);

    Optional<LivroEntity> findByCodLivro(Long codLivro);

    List<LivroEntity> findByTituloLivroContainingIgnoreCase(String tituloLivro);

    List<LivroEntity> findByAutorContainingIgnoreCase(String autor);

    List<LivroEntity> findByGeneroContainingIgnoreCase(String genero);

    List<LivroEntity> findByIsbnContainingIgnoreCase(String isbn);

    Optional<LivroEntity> findByIsbn(String isbn);

    List<LivroEntity> findByData(LocalDate data);

    List<LivroEntity> findByDataBetween(LocalDate inicio, LocalDate fim);

}
