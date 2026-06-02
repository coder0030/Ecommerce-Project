package sumitproject.SpringCart.Service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.UserDTO;
import sumitproject.SpringCart.RequestDTO.UserRequestDTO;

@Component
public interface UserService {
    UserDTO createUser(@Valid UserRequestDTO userRequestDTO);

    Page<UserDTO> getAllUsers(int pageNo, int pageSize);

    UserDTO getUserById(Long id);

    void deleteUserById(Long id);

    UserDTO updateUserById(Long id, @Valid UserRequestDTO userRequestDTO);

    UserDTO partialUpdateUserById(Long id, UserRequestDTO userRequestDTO);

    UserDTO getUserByEmail(String email);
}
