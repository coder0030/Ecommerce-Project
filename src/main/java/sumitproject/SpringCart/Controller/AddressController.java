package sumitproject.SpringCart.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Adresses ", description = "Creates Address to deliver product at the location.")
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = "Creating new Address", description = "Authenticated only login user and" +
            " admin can create addresses.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Addresses created successfully"
            )
    })
    @PostMapping("/create/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<AddressDTO> createAddress(@PathVariable Long userId,
                                                    @Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressDTO createdAddress = addressService.createAddress(userId, addressRequestDTO);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all addresses",
            description = "Getting all addresses"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Addresses fetched successfully"
            )
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AddressDTO>> getAllAddresses(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<AddressDTO> addresses = addressService.getAllAddresses(pageNo, pageSize);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @Operation(
            summary = "Get addresses by id",
            description = "Getting all addresses"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Addresses fetched successfully"
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id) {
        AddressDTO addressDTO = addressService.getAddressById(id);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get addresses by user id",
            description = "Only user and admin can have access"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Addresses fetched successfully"
            )
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<Page<AddressDTO>> getAddressesByUserId(@PathVariable Long userId,
                                                                 @RequestParam(defaultValue = "0") int pageNo,
                                                                 @RequestParam(defaultValue = "20") int pageSize) {
        Page<AddressDTO> addresses = addressService.getAddressesByUserId(userId, pageNo, pageSize);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }


    @Operation(
            summary = "Get Deafault addresses by user id",
            description = "Only user and admin can have access"
    )
    @GetMapping("/user/{userId}/default")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<AddressDTO> getDefaultAddressByUserId(@PathVariable Long userId) {
        AddressDTO addressDTO = addressService.getDefaultAddressByUserId(userId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }


    @Operation(summary = "Authenticated", description = "Only admin and " +
            " login user can delete Address")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Void> deleteAddressById(@PathVariable Long id) {
        addressService.deleteAddressById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Authenticated", description = "Only admin and " +
            " login user can update Address")
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long id,
                                                        @Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressDTO updatedAddress = addressService.updateAddressById(id, addressRequestDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @Operation(summary = "Authenticated", description = "Only admin and " +
            " login user can update Address")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<AddressDTO> partialUpdateAddressById(@PathVariable Long id,
                                                               @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressDTO updatedAddress = addressService.partialUpdateAddressById(id, addressRequestDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @Operation(summary = "Authenticated", description = "Only admin and " +
            " login user can set default Address")
    @PatchMapping("/{id}/set-default")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<AddressDTO> setDefaultAddress(@PathVariable Long id, @PathVariable Long userId) {
        AddressDTO updatedAddress = addressService.setDefaultAddress(id, userId);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }
}
