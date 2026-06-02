package sumitproject.SpringCart.ServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.UserDTO;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.Mapper.UserMapper;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.MyException.DuplicateValueException;
import sumitproject.SpringCart.Repository.UserRepository;
import sumitproject.SpringCart.RequestDTO.UserRequestDTO;
import sumitproject.SpringCart.Service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AllRepositoryMethods allRepositoryMethods;
    private final PasswordEncoder passwordEncoder;

    private void checkEmailExistenceForCreate(String email) {
        if(userRepository.existsByEmailAndIsActive(email, true)) {
            throw new DuplicateValueException("Email already exists");
        }
    }

    private void checkEmailExistenceForUpdate(String email, Long id) {
        if(userRepository.existsByEmailAndIdNotAndIsActive(email, id, true)) {
            throw new DuplicateValueException("Email already exists, enter different email");
        }
    }

    private void checkPassword(String password) {
        String  regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

        if(!password.matches(regexp)) {
            throw new BadRequestException("Password must contain uppercase," +
                    " lowercase, digit and be at least 8 characters long");
        }
    }

    @Transactional
    @Override
    public UserDTO createUser(UserRequestDTO userRequestDTO) {
        checkEmailExistenceForCreate(userRequestDTO.getEmail());
        checkPassword(userRequestDTO.getPassword());

        User user = new User();
        user = userMapper.toEntity(userRequestDTO, user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public Page<UserDTO> getAllUsers(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<User> users = userRepository.findAll(pageable);
        if(users.isEmpty()) {
            return Page.empty(pageable);
        }

        return users.map(userMapper::toDto);
    }

    @Override
    public UserDTO getUserById(Long id) {
      User user = allRepositoryMethods.getUserById(id);
      return userMapper.toDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
      User user = allRepositoryMethods.getUserById(id);
      user.setActive(false);
      userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO partialUpdateUserById(Long id, UserRequestDTO userRequestDTO) {
        User user = allRepositoryMethods.getUserById(id);

        if(userRequestDTO.getEmail() != null && !userRequestDTO.getEmail().equals(user.getEmail())) {
            checkEmailExistenceForUpdate(userRequestDTO.getEmail(), user.getId());
            user.setEmail(userRequestDTO.getEmail());
        }

        if (userRequestDTO.getPassword() != null &&
                !passwordEncoder.matches(userRequestDTO.getPassword(), user.getPassword())) {

            checkPassword(userRequestDTO.getPassword());
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        if(userRequestDTO.getName() != null && !userRequestDTO.getName().equals(user.getName())) {
            user.setName(userRequestDTO.getName());
        }

        user = userMapper.toEntity(userRequestDTO, user);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO updateUserById(Long id, UserRequestDTO userRequestDTO) {
        User user = allRepositoryMethods.getUserById(id);
        user = userMapper.updateToEntity(userRequestDTO, user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = allRepositoryMethods.getUserByEmail(email);
        return userMapper.toDto(user);
    }
}
