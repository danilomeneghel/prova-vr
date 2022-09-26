package api.model.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class ErrorModel {

    @NonNull
    @Schema(name = "Http status code", example = "400", required = true)
    private Integer status;

    @NonNull
    @Schema(name = "Error code", example = "400001", required = true)
    private String code;

    @NonNull
    @Schema(name = "Error message", example = "Error message", required = true)
    private String message;

    @Schema(name = "Error details", example = "Error details")
    private String details;

}