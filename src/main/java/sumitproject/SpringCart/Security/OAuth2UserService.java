package sumitproject.SpringCart.Security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sumitproject.SpringCart.DTO.LoginResponseDTO;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.Helper.AuthProviderType;
import sumitproject.SpringCart.Helper.Role;
import sumitproject.SpringCart.Repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService {

    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    private LoginResponseDTO response(User user) {
        String token = authUtil.generateAccessToken(user);
        return new LoginResponseDTO(token, user.getId());
    }

    @Transactional
    public LoginResponseDTO processOAuthPostLogin(OAuth2User oAuth2User, String registrationId) {

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        AuthProviderType providerType = authUtil.getProviderType(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        if (email == null || email.isBlank()) {
            throw new BadCredentialsException("Email not provided by OAuth provider. Please use a different login method.");
        }

        log.info("OAuth login attempt - Provider: {}, Email: {}, ProviderId: {}", providerType, email, providerId);

        User userByProviderId = userRepository.findByProviderIdAndIsActiveTrue(providerId);

        if (userByProviderId != null) {
            log.info("CASE 1: User found by providerId: {}", userByProviderId.getId());

            if (!userByProviderId.getEmail().equals(email)) {
                userByProviderId.setEmail(email);
                userByProviderId = userRepository.save(userByProviderId);
            }

            return response(userByProviderId);
        }

        Optional<User> optionalUser = userRepository.findByEmailAndIsActiveTrue(email);

        if (optionalUser.isEmpty()) {

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name != null ? name : email.split("@")[0]);
            newUser.setPassword(null);

            Set<AuthProviderType> providerSet = new HashSet<>();
            providerSet.add(providerType);
            newUser.setProvider(providerSet);

            newUser.setProviderId(providerId);

            Set<Role> roles = new HashSet<>();
            roles.add(Role.CUSTOMER);
            newUser.setRoles(roles);

            return response(userRepository.save(newUser));
        }

        User userByEmail = optionalUser.get();

        if (userByEmail.getProvider().contains(providerType)) {
            userByEmail.setProviderId(providerId);
            return response(userRepository.save(userByEmail));
        }

        if (userByEmail.getProvider().contains(AuthProviderType.LOCAL)) {
            userByEmail.getProvider().add(providerType);
            userByEmail.setProviderId(providerId);
            return response(userRepository.save(userByEmail));
        }

        AuthProviderType existingProvider = userByEmail.getProvider().stream()
                .filter(p -> p != AuthProviderType.LOCAL)
                .findFirst()
                .orElse(null);

        throw new BadCredentialsException(
                String.format("Email %s is already registered with %s. Please login using %s, or sign in first and link your %s account from profile settings.",
                        email, existingProvider, existingProvider, providerType)
        );
    }
}