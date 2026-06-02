package sumitproject.SpringCart.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import sumitproject.SpringCart.DTO.CategoryDTO;
import sumitproject.SpringCart.RequestDTO.CategoryRequestDTO;
import sumitproject.SpringCart.Service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    @Override
    public CategoryDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        return null;
    }

    @Override
    public Page<CategoryDTO> getAllCategories(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        return null;
    }

    @Override
    public List<CategoryDTO> getCategoryHierarchy() {
        return List.of();
    }

    @Override
    public Page<CategoryDTO> getSubCategories(Long parentId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public void deleteCategoryById(Long id) {

    }

    @Override
    public CategoryDTO updateCategoryById(Long id, CategoryRequestDTO categoryRequestDTO) {
        return null;
    }

    @Override
    public CategoryDTO partialUpdateCategoryById(Long id, CategoryRequestDTO categoryRequestDTO) {
        return null;
    }
}
