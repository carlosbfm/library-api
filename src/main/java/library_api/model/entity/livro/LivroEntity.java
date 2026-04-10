package library_api.model.entity.livro;



import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import library_api.model.enums.StatusLivro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "livro")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivroEntity {

    @Id
    @Column( name = "codigo_livro", length = 6,nullable = false, unique = true )
    private Long codLivro;

    @Column(name = "titulo", length = 150)
    private String tituloLivro;

    @Column(name = "autor",length = 100)
    private String autor;

    @Column(name = "genero", length = 50)
    private String genero;

    @Column(name = "isbn", length = 20)
    private String isbn;

    @Column(name = "data_lancamento")
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 100)
    private StatusLivro status;

}
