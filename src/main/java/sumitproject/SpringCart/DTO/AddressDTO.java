package sumitproject.SpringCart.DTO;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long id;
    private Long userId;
    private String userName;  // Additional field from join
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private Boolean isDefault;
    private String fullAddress;

    public String getFullAddress() {
        return String.format("%s, %s, %s - %s, %s",
                street, city, state, pincode, country);
    }
}
