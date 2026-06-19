package sumitproject.SpringCart.Security;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.AppConfig.AppConfig;
import sumitproject.SpringCart.DTO.LoginResponseDTO;
import sumitproject.SpringCart.DTO.SignupResponseDTO;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.Helper.AuthProviderType;
import sumitproject.SpringCart.Helper.Role;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.Repository.UserRepository;
import sumitproject.SpringCart.RequestDTO.LoginRequestDTO;
import sumitproject.SpringCart.RequestDTO.SignupRequestDTO;

import java.time.LocalDateTime;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final CustomUserDetailService userDetailService;

    public SignupResponseDTO signup(@Valid SignupRequestDTO signupRequestDTO) {
        if (userRepository.findByEmailAndIsActiveTrue(signupRequestDTO.getUsername()).isPresent()) {
            throw new BadRequestException("User already exists with email: " + signupRequestDTO.getUsername());
        }

        User user = new User();
        user.setEmail(signupRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        user.setRoles(Set.of(Role.CUSTOMER));
        user.setProvider(Set.of(AuthProviderType.LOCAL));

        User saved = userRepository.save(user);
        return new SignupResponseDTO(saved.getId(), saved.getEmail());
    }

    public LoginResponseDTO login(@Valid LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                        )
        );

        User user = (User) authentication.getPrincipal();
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        String token = authUtil.generateAccessToken(user);
        return new LoginResponseDTO(token, user.getId());
    }
}
