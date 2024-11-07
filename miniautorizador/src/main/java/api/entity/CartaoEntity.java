package api.entity;

import api.enums.CartaoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "cartao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id", unique = true, nullable = false )
    private Long id;

    @Column(name = "numeroCartao", nullable = false)
    @NotNull( message = "Número do cartão é obrigatório" )
    @Size(min = 13, max = 19, message = "O número do cartão deve ter entre 13 e 19 dígitos")
    private Long numeroCartao;

    @Column(name = "senha")
    private String senha;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_saldo", nullable = false)
    private SaldoEntity saldo;

    private CartaoStatus status;

    @CreationTimestamp
    @Temporal( TemporalType.TIMESTAMP )
    private Date createdAt;

    @UpdateTimestamp
    @Temporal( TemporalType.TIMESTAMP )
    private Date updatedAt;

}