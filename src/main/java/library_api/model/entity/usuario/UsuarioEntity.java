package library_api.model.entity.usuario;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codUsuario", length = 6, nullable = false,unique = true )
    private Long codUsuario;

    @Column(name = "login", length = 100, nullable = false)
    private String login;

    @Column(name = "senha", length = 60, nullable = false)
    private String password;

    @Column(name = "data_expiracao_senha", nullable = false)
    private LocalDate dataExpiracaoSenha;
    
    @Column(name = "roles", length = 60, nullable = false)
    private String roles;
}
