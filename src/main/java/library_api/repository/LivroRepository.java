package library_api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import library_api.model.entity.livro.LivroEntity;

@Repository
public interface LivroRepository extends JpaRepository<LivroEntity, Long> {
    boolean existsByCodLivro(Long codLivro);

    Optional<LivroEntity> findByCodLivro(Long codLivro);

    Page<LivroEntity> findByAutorContainingIgnoreCase(String autor, Pageable pageable);

    Page<LivroEntity> findByGeneroContainingIgnoreCase(String genero, Pageable pageable);

    Page<LivroEntity> findByIsbnContainingIgnoreCase(String isbn,Pageable pageable);

    Optional<LivroEntity> findByIsbn(String isbn);

    List<LivroEntity> findByData(LocalDate data);

    List<LivroEntity> findByDataBetween(LocalDate inicio, LocalDate fim);

    Page<LivroEntity> findByTituloLivroContainingIgnoreCase(String titulo, Pageable pageable);

}
