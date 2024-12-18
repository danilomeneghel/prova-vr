package api.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transacao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id", unique = true, nullable = false )
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cartao", nullable = false)
    private CartaoEntity cartao;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @CreationTimestamp
    @Temporal( TemporalType.TIMESTAMP )
    private Date createdAt;

    @UpdateTimestamp
    @Temporal( TemporalType.TIMESTAMP )
    private Date updatedAt;

}