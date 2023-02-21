package org.haupt.chemicals.api.controllers;

import org.haupt.chemicals.api.model.Product;
import org.haupt.chemicals.api.repository.ProductRepository;
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

@RequestMapping(path = "api/product")
@RestController
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public @ResponseBody
    Iterable<Product> getAllProduct() {
        return productRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productRepository
                .findById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        product.setId(null);
        Product saved = productRepository.save(product);
        return ResponseEntity.created(URI.create("/api/product/" + saved.getId())).body(saved);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") Long productId, @RequestBody Product productDetails) {
        return productRepository.findById(productId).map(product -> {
                    product.setTitel(productDetails.getTitel());
                    product.setCreated(productDetails.getCreated());
                    product.setUpdated(productDetails.getUpdated());
                    product.setContent(productDetails.getContent());
                    Product updatedProduct = productRepository.save(product);
                    return ResponseEntity.ok(updatedProduct);
                }
        ).orElseGet(() -> createProduct(productDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable(value =
            "id") Long productId) {
        return productRepository
                .findById(productId)
                .map(
                        product -> {
                            productRepository.delete(product);
                            return ResponseEntity.ok().<Product>build();
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
