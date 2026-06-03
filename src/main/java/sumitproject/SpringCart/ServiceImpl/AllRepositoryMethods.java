package sumitproject.SpringCart.ServiceImpl;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.Entity.*;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.MyException.DataNotFoundException;
import sumitproject.SpringCart.Repository.*;

@RequiredArgsConstructor
@Component
public class AllRepositoryMethods {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderServiceRepository orderServiceRepository;

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
            throw new BadRequestException("Address with id: " + id + "not exists");
        }
        return address;
    }

    public Category getCategoryById(Long parentCategoryId) {
      return categoryRepository.findByIdAndIsActiveTrue(parentCategoryId).orElseThrow(() ->
          new BadRequestException("ParentCategoryId: " + parentCategoryId + " not found.")
      );
    }

    public Product getProductById(Long id) {
        Product product = productRepository.findByIdAndIsActiveTrue(id);
        if(product == null) {
            throw new BadRequestException("Product id: " + id + " is not exists");
        }
        return product;
    }

    public Cart getCartById(Long id) {
        Cart cart = cartRepository.findByIdAndIsActiveTrue(id);
        if(cart == null) {
            throw new BadRequestException("Cart id: " + id + " not exists");
        }
        return cart;
    }

    public CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new DataNotFoundException("CartItem id: " + id + " not exists."));
    }

}
