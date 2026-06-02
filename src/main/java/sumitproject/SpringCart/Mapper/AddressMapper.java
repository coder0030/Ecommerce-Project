package sumitproject.SpringCart.Mapper;


import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.AddressDTO;
import sumitproject.SpringCart.Entity.Address;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.RequestDTO.AddressRequestDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AddressMapper {

    public AddressDTO toDto(Address address) {
        if (address == null) return null;

        AddressDTO.AddressDTOBuilder builder = AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .country(address.getCountry())
                .isDefault(address.getIsDefault());

        return builder.build();
    }

    public Address toEntity(AddressRequestDTO addressDTO, Address address) {
        if (addressDTO == null) return null;

        if (addressDTO.getStreet() != null) {
            address.setStreet(addressDTO.getStreet());
        }
        if (addressDTO.getCity() != null) {
            address.setCity(addressDTO.getCity());
        }
        if (addressDTO.getState() != null) {
            address.setState(addressDTO.getState());
        }
        if (addressDTO.getPincode() != null) {
            address.setPincode(addressDTO.getPincode());
        }
        if (addressDTO.getCountry() != null) {
            address.setCountry(addressDTO.getCountry());
        }
        if (addressDTO.getIsDefault() != null) {
            address.setIsDefault(addressDTO.getIsDefault());
        }

        return address;
    }

    public Set<AddressDTO> toDtoList(Set<Address> addresses) {
        return addresses.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    public Address updateToEntity(AddressRequestDTO addressDTO, Address address) {
        boolean nullValue = false;

        if (addressDTO.getStreet() == null) nullValue = true;
        if (addressDTO.getCity() == null) nullValue = true;
        if (addressDTO.getState() == null) nullValue = true;
        if (addressDTO.getPincode() == null) nullValue = true;
        if (addressDTO.getCountry() == null) nullValue = true;
        if (addressDTO.getIsDefault() == null) nullValue = true;

        if (nullValue) {
            throw new BadRequestException("Incomplete data provided.");
        }

        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPincode(addressDTO.getPincode());
        address.setCountry(addressDTO.getCountry());
        address.setIsDefault(addressDTO.getIsDefault());

        return address;
    }
}
