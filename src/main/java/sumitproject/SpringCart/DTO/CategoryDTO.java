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
    private String parentCategoryName;
    private List<CategoryDTO> subCategories;
    private Integer productCount;
    private LocalDateTime createdAt;

}
