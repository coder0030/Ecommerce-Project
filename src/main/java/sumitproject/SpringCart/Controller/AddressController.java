package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.AddressDTO;
import sumitproject.SpringCart.RequestDTO.AddressRequestDTO;
import sumitproject.SpringCart.Service.AddressService;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/create/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<AddressDTO> createAddress(@PathVariable Long userId,
                                                    @Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressDTO createdAddress = addressService.createAddress(userId, addressRequestDTO);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AddressDTO>> getAllAddresses(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<AddressDTO> addresses = addressService.getAllAddresses(pageNo, pageSize);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id) {
        AddressDTO addressDTO = addressService.getAddressById(id);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Page<AddressDTO>> getAddressesByUserId(@PathVariable Long userId,
                                                                 @RequestParam(defaultValue = "0") int pageNo,
                                                                 @RequestParam(defaultValue = "20") int pageSize) {
        Page<AddressDTO> addresses = addressService.getAddressesByUserId(userId, pageNo, pageSize);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/default")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<AddressDTO> getDefaultAddressByUserId(@PathVariable Long userId) {
        AddressDTO addressDTO = addressService.getDefaultAddressByUserId(userId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAddressById(@PathVariable Long id) {
        addressService.deleteAddressById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long id,
                                                        @Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressDTO updatedAddress = addressService.updateAddressById(id, addressRequestDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddressDTO> partialUpdateAddressById(@PathVariable Long id,
                                                               @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressDTO updatedAddress = addressService.partialUpdateAddressById(id, addressRequestDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @PatchMapping("/{id}/set-default")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<AddressDTO> setDefaultAddress(@PathVariable Long id, @PathVariable Long userId) {
        AddressDTO updatedAddress = addressService.setDefaultAddress(id, userId);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }
}
