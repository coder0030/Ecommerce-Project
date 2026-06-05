package sumitproject.SpringCart.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    @NotBlank(message = "Username is required")
    @Schema(example = "sumit@gmail.com")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password length should be or greater than 8, " +
            " one small, one capital or numerical must included.",
            example = "Password@1234"
    )
    private String password;
}
