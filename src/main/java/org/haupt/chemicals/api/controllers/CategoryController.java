package org.haupt.chemicals.api.controllers;

import org.haupt.chemicals.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(path = "api/category")
@RestController
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public @ResponseBody
    Iterable<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        return categoryRepository
                .findById(id)
                .map(category -> ResponseEntity.ok().body(category))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        category.setId(null);
        Category saved = categoryRepository.save(category);
        return ResponseEntity.created(URI.create("/api/category/" + saved.getId())).body(saved);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable(value = "id") Long categoryId, @RequestBody Category categoryDetails) {
        return categoryRepository.findById(categoryId).map(category -> {
                    category.setParent(categoryDetails.getParent());
                    category.setTitel(categoryDetails.getTitel());
                    category.setContent(categoryDetails.getContent());
                    category.setProducts(categoryDetails.getProducts());
                    Category updatedCategory = categoryRepository.save(category);
                    return ResponseEntity.ok(updatedCategory);
                }
        ).orElseGet(() -> createCategory(categoryDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable(value =
            "id") Long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .map(
                        category -> {
                            categoryRepository.delete(category);
                            return ResponseEntity.ok().<Category>build();
                        })
                .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> onConstraintValidationException(
            ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField() , fieldError.getDefaultMessage());
        }
        return errors;
    }
}
