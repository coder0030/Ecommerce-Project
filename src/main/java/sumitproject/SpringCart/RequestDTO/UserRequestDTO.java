package sumitproject.SpringCart.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import sumitproject.SpringCart.Helper.Role;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = "Name is required")
    @Schema(example = "Sumit mishra")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Schema(example = "example@gmail.com")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(example = "Password@1234")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(description = "Only 'CUSTOMER' AND 'ADMIN' ROLE for new User only" +
            " 'CUSTOMER' ")
    private Role role;
}
