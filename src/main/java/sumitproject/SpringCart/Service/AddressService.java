package sumitproject.SpringCart.Service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.AddressDTO;
import sumitproject.SpringCart.RequestDTO.AddressRequestDTO;

@Component
public interface AddressService {
    AddressDTO createAddress(Long userId, @Valid AddressRequestDTO addressRequestDTO);

    Page<AddressDTO> getAllAddresses(int pageNo, int pageSize);

    AddressDTO getAddressById(Long id);

    Page<AddressDTO> getAddressesByUserId(Long userId, int pageNo, int pageSize);

    AddressDTO getDefaultAddressByUserId(Long userId);

    void deleteAddressById(Long id);

    AddressDTO updateAddressById(Long id, @Valid AddressRequestDTO addressRequestDTO);

    AddressDTO partialUpdateAddressById(Long id, AddressRequestDTO addressRequestDTO);

    AddressDTO setDefaultAddress(Long id, Long userId);
}
