package sumitproject.SpringCart.Mapper;

import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.UserDTO;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.RequestDTO.UserRequestDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDto(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRoles())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public User toEntity(UserRequestDTO userDTO, User user) {
        if (userDTO == null) return null;

        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        if (userDTO.getRole() != null) {
            user.addRole(userDTO.getRole());
        }

        return user;
    }

    public List<UserDTO> toDtoList(List<User> users) {
        if (users == null || users.isEmpty()) return null;
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    public User updateToEntity(UserRequestDTO userDTO, User user) {
        boolean nullValue = false;

        if (userDTO.getName() == null) nullValue = true;
        if (userDTO.getEmail() == null) nullValue = true;
        if (userDTO.getPassword() == null) nullValue = true;
        if (userDTO.getRole() == null) nullValue = true;

        if (nullValue) {
            throw new BadRequestException("Incomplete data provided.");
        }

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.addRole(userDTO.getRole());

        return user;
    }
}