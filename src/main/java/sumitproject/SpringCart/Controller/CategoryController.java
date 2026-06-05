package sumitproject.SpringCart.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.CategoryDTO;
import sumitproject.SpringCart.RequestDTO.CategoryRequestDTO;
import sumitproject.SpringCart.Service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Manage product categories and their hierarchy")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create category", description = "Creates a new product category (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryRequestDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all categories", description = "Retrieves paginated list of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping("/all")
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<CategoryDTO> categories = categoryService.getAllCategories(pageNo, pageSize);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a specific category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get category hierarchy", description = "Retrieves the complete category tree hierarchy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hierarchy retrieved successfully")
    })
    @GetMapping("/hierarchy")
    public ResponseEntity<List<CategoryDTO>> getCategoryHierarchy() {
        List<CategoryDTO> categories = categoryService.getCategoryHierarchy();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Get subcategories", description = "Retrieves all subcategories for a given parent category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subcategories retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Parent category not found")
    })
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<Page<CategoryDTO>> getSubCategories(@PathVariable Long parentId,
                                                              @RequestParam(defaultValue = "0") int pageNo,
                                                              @RequestParam(defaultValue = "20") int pageSize) {
        Page<CategoryDTO> categories = categoryService.getSubCategories(parentId, pageNo, pageSize);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Delete category", description = "Permanently deletes a category (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update category", description = "Updates an existing category (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategoryById(@PathVariable Long id,
                                                          @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategoryById(id, categoryRequestDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @Operation(summary = "Partial update category", description = "Partially updates an existing category (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> partialUpdateCategoryById(@PathVariable Long id,
                                                                 @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryDTO updatedCategory = categoryService.partialUpdateCategoryById(id, categoryRequestDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
}