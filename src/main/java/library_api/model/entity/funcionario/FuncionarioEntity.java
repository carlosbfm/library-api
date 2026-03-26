package library_api.model.entity.funcionario;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import library_api.model.entity.usuario.UsuarioEntity;

@Entity
@Table(name = "funcionario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncionarioEntity { 

    @Id
    @Column(name = "matricula_funcionario", length = 12, nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(unique = true, length = 20, nullable = false)
    private String cpf;

    @Column(name = "data_admissao", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataAdmissao;

    @ManyToOne
    @JoinColumn(name = "id_cargo", nullable = false)
    private Cargo cargo;

    @OneToOne(cascade = CascadeType.ALL) 
    @JoinColumn(name = "usuario_id", referencedColumnName = "codUsuario")
    private UsuarioEntity usuario;

}