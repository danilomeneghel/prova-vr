package api.model;

import api.enums.CartaoStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoModel {

    private Long id;

    private String numeroCartao;

    private BigDecimal saldo;

    private CartaoStatus status;

}