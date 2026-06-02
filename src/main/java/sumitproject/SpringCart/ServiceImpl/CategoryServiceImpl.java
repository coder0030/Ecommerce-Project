package sumitproject.SpringCart.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumitproject.SpringCart.DTO.CategoryDTO;
import sumitproject.SpringCart.Entity.Category;
import sumitproject.SpringCart.Mapper.CategoryMapper;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.Repository.CategoryRepository;
import sumitproject.SpringCart.RequestDTO.CategoryRequestDTO;
import sumitproject.SpringCart.Service.CategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final AllRepositoryMethods allRepositoryMethods;
    private final CategoryMapper categoryMapper;

    private void existsByName(String name) {
        if(categoryRepository.existsByName(name)) {
            throw new BadRequestException("Category name: " + name + " already exists");
        }
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        existsByName(categoryRequestDTO.getName());

        Category category = categoryMapper.toEntity(categoryRequestDTO, new Category());

        if(categoryRequestDTO.getParentCategoryId() != null) {
            Category parentCategory = allRepositoryMethods.getCategoryById(categoryRequestDTO.getParentCategoryId());
            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);
        }

        category.setActive(true);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public Page<CategoryDTO> getAllCategories(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Category> categoryPage = categoryRepository.findAllByIsActiveTrue(pageable);

        if(categoryPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return categoryPage.map(categoryMapper::toDto);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = allRepositoryMethods.getCategoryById(id);
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDTO> getCategoryHierarchy() {
        List<Category> topLevelCategories = categoryRepository.findByParentCategoryIsNullAndIsActiveTrue();

        List<CategoryDTO> hierarchy = new ArrayList<>();
        for (Category category : topLevelCategories) {
            CategoryDTO dto = categoryMapper.toDto(category);
            dto.setSubCategories(getSubCategoriesRecursive(category.getId()));
            hierarchy.add(dto);
        }

        return hierarchy;
    }

    private List<CategoryDTO> getSubCategoriesRecursive(Long parentId) {
        List<Category> subCategories = categoryRepository.findByParentCategory_IdAndIsActiveTrue(parentId);

        List<CategoryDTO> result = new ArrayList<>();
        for (Category category : subCategories) {
            CategoryDTO dto = categoryMapper.toDto(category);
            dto.setSubCategories(getSubCategoriesRecursive(category.getId()));
            result.add(dto);
        }
        return result;
    }

    @Override
    public Page<CategoryDTO> getSubCategories(Long parentId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Category> categoryPage = categoryRepository.findAllByParentCategory_IdAndIsActiveTrue(parentId, pageable);

        if(categoryPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return categoryPage.map(categoryMapper::toDto);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        Category category = allRepositoryMethods.getCategoryById(id);

        boolean hasSubCategories = categoryRepository.existsByParentCategory_IdAndIsActiveTrue(id);
        if(hasSubCategories) {
            throw new BadRequestException("Cannot delete category with sub-categories. Delete sub-categories first.");
        }

        category.setActive(false);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategoryById(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category category = allRepositoryMethods.getCategoryById(id);

        if(categoryRequestDTO.getName() != null && !categoryRequestDTO.getName().equals(category.getName())) {
            existsByName(categoryRequestDTO.getName());
            category.setName(categoryRequestDTO.getName());
        }

        if(categoryRequestDTO.getDescription() != null) {
            category.setDescription(categoryRequestDTO.getDescription());
        }

        if(categoryRequestDTO.getParentCategoryId() != null) {
            if(categoryRequestDTO.getParentCategoryId().equals(id)) {
                throw new BadRequestException("Category cannot be its own parent");
            }

            Category parentCategory = allRepositoryMethods.getCategoryById(categoryRequestDTO.getParentCategoryId());
            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);
        }

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDTO partialUpdateCategoryById(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category category = allRepositoryMethods.getCategoryById(id);

        if(categoryRequestDTO.getName() != null && !categoryRequestDTO.getName().equals(category.getName())) {
            existsByName(categoryRequestDTO.getName());
            category.setName(categoryRequestDTO.getName());
        }

        if(categoryRequestDTO.getDescription() != null && !categoryRequestDTO.getDescription().equals(category.getDescription())) {
            category.setDescription(categoryRequestDTO.getDescription());
        }

        if(categoryRequestDTO.getParentCategoryId() != null) {
            if(categoryRequestDTO.getParentCategoryId().equals(id)) {
                throw new BadRequestException("Category cannot be its own parent");
            }

            Category parentCategory = allRepositoryMethods.getCategoryById(categoryRequestDTO.getParentCategoryId());
            category.setParentCategory(parentCategory);
        }

        return categoryMapper.toDto(categoryRepository.save(category));
    }
}