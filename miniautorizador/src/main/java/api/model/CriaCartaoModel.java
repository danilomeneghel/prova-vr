package api.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriaCartaoModel {

    private String numeroCartao;

    private String senha;

}