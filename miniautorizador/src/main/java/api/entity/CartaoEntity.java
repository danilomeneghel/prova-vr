package api.entity;

import api.enums.CartaoStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "cartao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id", unique = true, nullable = false )
    private Long id;

    @Column(name = "numeroCartao", nullable = false, unique = true)
    @NotNull( message = "Número do cartão é obrigatório" )
    private String numeroCartao;

    @Column(name = "senha")
    private String senha;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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