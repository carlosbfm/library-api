package library_api.model.entity.cliente;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import library_api.model.enums.TipoCliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ClienteEntity {
    @Id
    @Column(name = "codigo_cliente", length = 12)
    private Long codCliente;

    @Column(name = "nome_cliente", length = 100)
    private String nomeCliente;

    @Column(name = "tipo_cliente", length = 50, nullable = false)
    private TipoCliente tipoCliente;

    @Column(name = "cpf", length = 20 , unique = true)
    private String cpf;

    @Column(name = "cnpj", length = 20 , unique = true)
    private String cnpj;

    @Column(name = "data_cadastro")
    @CreatedDate
    private LocalDate data;

}
