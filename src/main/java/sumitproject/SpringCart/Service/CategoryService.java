package sumitproject.SpringCart.Service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.CategoryDTO;
import sumitproject.SpringCart.RequestDTO.CategoryRequestDTO;

import java.util.List;

@Component
public interface CategoryService {
    CategoryDTO createCategory(@Valid CategoryRequestDTO categoryRequestDTO);

    Page<CategoryDTO> getAllCategories(int pageNo, int pageSize);

    CategoryDTO getCategoryById(Long id);

    List<CategoryDTO> getCategoryHierarchy();

    Page<CategoryDTO> getSubCategories(Long parentId, int pageNo, int pageSize);

    void deleteCategoryById(Long id);

    CategoryDTO updateCategoryById(Long id, @Valid CategoryRequestDTO categoryRequestDTO);

    CategoryDTO partialUpdateCategoryById(Long id, CategoryRequestDTO categoryRequestDTO);
}
