package sumitproject.SpringCart.Mapper;

import org.springframework.stereotype.Component;
import sumitproject.SpringCart.DTO.CategoryDTO;
import sumitproject.SpringCart.Entity.Category;
import sumitproject.SpringCart.MyException.BadRequestException;
import sumitproject.SpringCart.RequestDTO.CategoryRequestDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDTO toDto(Category category) {
        if (category == null) return null;

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentCategoryId(category.getParentCategory().getId())
                .build();
    }

    public Category toEntity(CategoryRequestDTO categoryDTO, Category category) {
        if (categoryDTO == null) return null;

        if (categoryDTO.getName() != null) {
            category.setName(categoryDTO.getName());
        }
        if (categoryDTO.getDescription() != null) {
            category.setDescription(categoryDTO.getDescription());
        }

        return category;
    }

    public List<CategoryDTO> toDtoList(List<Category> categories) {
        if (categories == null || categories.isEmpty()) return null;
        return categories.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Category updateToEntity(CategoryRequestDTO categoryDTO, Category category) {
        boolean nullValue = false;

        if (categoryDTO.getName() == null) nullValue = true;
        if (categoryDTO.getDescription() == null) nullValue = true;

        if (nullValue) {
            throw new BadRequestException("Incomplete data provided.");
        }

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        return category;
    }
}
