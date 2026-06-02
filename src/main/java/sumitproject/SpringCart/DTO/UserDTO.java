package sumitproject.SpringCart.DTO;


import lombok.*;
import sumitproject.SpringCart.Helper.Role;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Set<Role> role = new HashSet<>();
    private Boolean isActive;
    private LocalDateTime createdAt;

}