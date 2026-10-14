package api.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriaTransacaoModel {

    private String numeroCartao;

    private String senhaCartao;

    private BigDecimal valor;

}
