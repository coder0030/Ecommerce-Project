package sumitproject.SpringCart.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDTO {

    @NotBlank(message = "Username is required, email is required")
    @Schema(example = "sumit@gmail.com")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(example = "Password@1234")
    private String password;

}
