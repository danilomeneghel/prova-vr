package api.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriaCartaoModel {

    private Long numeroCartao;

    private String senha;

}