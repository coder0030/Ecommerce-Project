package sumitproject.SpringCart.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDTO {

    @NotBlank(message = "Username is required, email is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

}
