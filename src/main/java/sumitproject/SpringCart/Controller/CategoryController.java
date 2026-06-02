package sumitproject.SpringCart.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sumitproject.SpringCart.DTO.CategoryDTO;
import sumitproject.SpringCart.RequestDTO.CategoryRequestDTO;
import sumitproject.SpringCart.Service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryRequestDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Page<CategoryDTO> categories = categoryService.getAllCategories(pageNo, pageSize);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @GetMapping("/hierarchy")
    public ResponseEntity<List<CategoryDTO>> getCategoryHierarchy() {
        List<CategoryDTO> categories = categoryService.getCategoryHierarchy();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<Page<CategoryDTO>> getSubCategories(@PathVariable Long parentId,
                                                              @RequestParam(defaultValue = "0") int pageNo,
                                                              @RequestParam(defaultValue = "20") int pageSize) {
        Page<CategoryDTO> categories = categoryService.getSubCategories(parentId, pageNo, pageSize);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDTO> updateCategoryById(@PathVariable Long id,
                                                          @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategoryById(id, categoryRequestDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDTO> partialUpdateCategoryById(@PathVariable Long id,
                                                                 @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryDTO updatedCategory = categoryService.partialUpdateCategoryById(id, categoryRequestDTO);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
}
