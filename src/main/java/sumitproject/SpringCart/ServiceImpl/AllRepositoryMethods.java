package sumitproject.SpringCart.ServiceImpl;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.Entity.Address;
import sumitproject.SpringCart.Entity.Category;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.Repository.AddressRepository;
import sumitproject.SpringCart.Repository.CategoryRepository;
import sumitproject.SpringCart.Repository.UserRepository;

@RequiredArgsConstructor
@Component
public class AllRepositoryMethods {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CategoryRepository categoryRepository;
    public User getUserById(Long id) {
        User user = userRepository.findByIdAndIsActive(id, true);
        if(user == null) {
            throw new BadRequestException("User  with id: " + id + " not exists.");
        }
        return user;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmailAndIsActive(email, true);
        if(user == null) {
            throw new BadRequestException("User  with email: " + email + "not exists.");
        }
        return user;
    }

    public Address getAddressById(Long id) {
        Address address = addressRepository.findByIdAndUser_IsActive(id, true);
        if(address == null) {
            new BadRequestException("Address with id: " + id + "not exists");
        }
        return address;
    }

    public Category getCategoryById(Long parentCategoryId) {
      return categoryRepository.findById(parentCategoryId).orElseThrow(() ->
          new BadRequestException("ParentCategoryId: " + parentCategoryId + " not found.")
      );
    }
}
