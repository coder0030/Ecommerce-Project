package sumitproject.SpringCart.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.LoginResponseDTO;
import sumitproject.SpringCart.DTO.SignupResponseDTO;
import sumitproject.SpringCart.RequestDTO.LoginRequestDTO;
import sumitproject.SpringCart.RequestDTO.SignupRequestDTO;
import sumitproject.SpringCart.Security.AuthService;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Signup and Login endpoints")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register new user")
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signup(@Valid @RequestBody SignupRequestDTO dto) {
        return new ResponseEntity<>(authService.signup(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return new ResponseEntity<>(authService.login(dto), HttpStatus.OK);
    }
}