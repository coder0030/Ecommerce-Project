package sumitproject.SpringCart.ServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.AddressDTO;
import sumitproject.SpringCart.Entity.Address;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.Helper.OrderStatus;
import sumitproject.SpringCart.Mapper.AddressMapper;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.Repository.AddressRepository;
import sumitproject.SpringCart.Repository.OrderServiceRepository;
import sumitproject.SpringCart.Repository.UserRepository;
import sumitproject.SpringCart.RequestDTO.AddressRequestDTO;
import sumitproject.SpringCart.Service.AddressService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AllRepositoryMethods allRepositoryMethods;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final OrderServiceRepository orderServiceRepository;

    private void resetDefaultAddresses(User user) {
        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
            user.getAddresses().forEach(address -> {
                if (Boolean.TRUE.equals(address.getIsDefault())) {
                    address.setIsDefault(false);
                }
            });
        }
    }

    private void handleDefaultAddress(User user, Address newAddress, Boolean isDefault) {
        if (Boolean.TRUE.equals(isDefault)) {
            resetDefaultAddresses(user);
            newAddress.setIsDefault(true);
        } else {
            long activeAddressCount = user.getAddresses().stream()
                    .filter(Address::isActive)
                    .count();
            if (activeAddressCount == 0) {
                newAddress.setIsDefault(true);
            } else {
                newAddress.setIsDefault(false);
            }
        }
    }


    @Override
    @Transactional
    public AddressDTO createAddress(Long userId, AddressRequestDTO addressRequestDTO) {
        User user = allRepositoryMethods.getUserById(userId);

        Address address = addressMapper.toEntity(addressRequestDTO, new Address());

        if (addressRequestDTO.getCountry() == null || addressRequestDTO.getCountry().isBlank()) {
            address.setCountry("INDIA");
        }

        address.setUser(user);
        address.setActive(true);

        handleDefaultAddress(user, address, addressRequestDTO.getIsDefault());

        Address savedAddress = addressRepository.save(address);
        user.addAddress(savedAddress);

        return addressMapper.toDto(savedAddress);
    }

    @Override
    public Page<AddressDTO> getAllAddresses(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Address> addressPage = addressRepository.findAllByIsActiveTrue(pageable);
        if (addressPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return addressPage.map(address -> {
            User user = address.getUser();
            return addressMapper.toDto(address);
        });
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Address address = allRepositoryMethods.getAddressById(id);
        User user = address.getUser();
        return addressMapper.toDto(address);
    }

    @Override
    public Page<AddressDTO> getAddressesByUserId(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        User user = allRepositoryMethods.getUserById(userId);

        Page<Address> addressPage = addressRepository.findByUser_IdAndIsActiveTrue(userId, pageable);

        if (addressPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return addressPage.map(addressMapper::toDto);
    }

    @Override
    public AddressDTO getDefaultAddressByUserId(Long userId) {
        User user = allRepositoryMethods.getUserById(userId);

        Address address = addressRepository.findByUser_IdAndIsDefaultAndIsActive(userId, true, true)
                .orElseThrow(() -> new BadRequestException("No default address found for user: " + userId));

        return addressMapper.toDto(address);
    }

    @Override
    @Transactional
    public void deleteAddressById(Long id) {
        Address address = allRepositoryMethods.getAddressById(id);
        User user = address.getUser();

        boolean canDeleteOrderAddress = orderServiceRepository.existsByAddress_IdAndAddress_IsActiveTrueAndStatus(
        id, OrderStatus.PENDING);

        if (!canDeleteOrderAddress) {
            throw new BadRequestException(
                    "Cannot delete address. An active order is associated with this address."
            );
        }

        address.setActive(false);

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            Optional<Address> anotherAddress = addressRepository
                    .findByUser_IdAndIsActiveTrueAndIdNot(user.getId(), id);

            anotherAddress.ifPresent(newDefault -> {
                newDefault.setIsDefault(true);
                addressRepository.save(newDefault);
            });
        }

        addressRepository.save(address);
    }

    @Override
    @Transactional
    public AddressDTO updateAddressById(Long id, AddressRequestDTO addressRequestDTO) {
        Address address = allRepositoryMethods.getAddressById(id);
        User user = address.getUser();

        boolean canUpdateOrder = orderServiceRepository.existsByAddress_IdAndAddress_IsActiveTrueAndStatusIn(
                id, Set.of(OrderStatus.PENDING, OrderStatus.CONFIRMED)
        );

        if (!canUpdateOrder) {
            throw new BadRequestException(
                    "Cannot update address. Order is PENDING or before SHIPPED status can updated."
            );
        }

        Boolean wasDefault = address.getIsDefault();

        address = addressMapper.updateToEntity(addressRequestDTO, address);

        if (Boolean.TRUE.equals(addressRequestDTO.getIsDefault()) && !Boolean.TRUE.equals(wasDefault)) {
            resetDefaultAddresses(user);
            address.setIsDefault(true);
        }

        Address updatedAddress = addressRepository.save(address);
        return addressMapper.toDto(updatedAddress);
    }

    @Override
    @Transactional
    public AddressDTO partialUpdateAddressById(Long id, AddressRequestDTO addressRequestDTO) {
        Address address = allRepositoryMethods.getAddressById(id);
        User user = address.getUser();

        boolean canUpdateOrder = orderServiceRepository.existsByAddress_IdAndAddress_IsActiveTrueAndStatusIn(
                id, Set.of(OrderStatus.PENDING, OrderStatus.CONFIRMED)
        );

        if (!canUpdateOrder) {
            throw new BadRequestException(
                    "Cannot update address. Order is PENDING or before SHIPPED status can updated."
            );
        }

        if (addressRequestDTO.getCity() != null && !addressRequestDTO.getCity().equals(address.getCity())) {
            address.setCity(addressRequestDTO.getCity());
        }

        if (addressRequestDTO.getPincode() != null && !addressRequestDTO.getPincode().equals(address.getPincode())) {
            address.setPincode(addressRequestDTO.getPincode());
        }

        if (addressRequestDTO.getState() != null && !addressRequestDTO.getState().equals(address.getState())) {
            address.setState(addressRequestDTO.getState());
        }

        if (addressRequestDTO.getStreet() != null && !addressRequestDTO.getStreet().equals(address.getStreet())) {
            address.setStreet(addressRequestDTO.getStreet());
        }

        if (addressRequestDTO.getCountry() != null && !addressRequestDTO.getCountry().equals(address.getCountry())) {
            address.setCountry(addressRequestDTO.getCountry());
        }

        if (addressRequestDTO.getIsDefault() != null) {

            if (addressRequestDTO.getIsDefault() && !Boolean.TRUE.equals(address.getIsDefault())) {
                resetDefaultAddresses(user);
                address.setIsDefault(true);

            } else if (!addressRequestDTO.getIsDefault()) {
                address.setIsDefault(false);
            }
        }

        Address updatedAddress = addressRepository.save(address);
        return addressMapper.toDto(updatedAddress);
    }

    @Override
    @Transactional
    public AddressDTO setDefaultAddress(Long id, Long userId) {
        Address address = allRepositoryMethods.getAddressById(id);

        if (!address.getUser().getId().equals(userId)) {
            throw new BadRequestException("Address does not belong to user: " + userId);
        }

        User user = address.getUser();

        resetDefaultAddresses(user);

        address.setIsDefault(true);
        Address savedAddress = addressRepository.save(address);

        return addressMapper.toDto(savedAddress);
    }
}