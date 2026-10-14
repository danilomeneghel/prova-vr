package api.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoRespostaModel {

    private String senha;

    private String numeroCartao;

}
