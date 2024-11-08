package api.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriaTransacaoModel {

    private Long numeroCartao;

    private String senha;

    private BigDecimal valor;

}
