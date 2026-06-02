package sumitproject.SpringCart.DTO;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import jakarta.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Long parentCategoryId;
    private String parentCategoryName;  // Additional field from self-join
    private List<CategoryDTO> subCategories;  // For hierarchical structure
    private Integer productCount;  // Computed field
    private LocalDateTime createdAt;

}
