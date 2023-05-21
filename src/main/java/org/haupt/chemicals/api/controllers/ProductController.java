package org.haupt.chemicals.api.controllers;

import org.haupt.chemicals.api.model.Diagnose;
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
    Iterable<Diagnose> getAllProduct() {
        return productRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Diagnose> getProduct(@PathVariable Long id) {
        return productRepository
                .findById(id)
                .map(diagnose -> ResponseEntity.ok().body(diagnose))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Diagnose> createProduct(@Valid @RequestBody Diagnose diagnose) {
        diagnose.setId(null);
        Diagnose saved = productRepository.save(diagnose);
        return ResponseEntity.created(URI.create("/api/product/" + saved.getId())).body(saved);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Diagnose> updateProduct(@PathVariable(value = "id") Long productId, @RequestBody Diagnose diagnoseDetails) {
        return productRepository.findById(productId).map(diagnose -> {
                    diagnose.setTitel(diagnoseDetails.getTitel());
                    diagnose.setCreated(diagnoseDetails.getCreated());
                    diagnose.setUpdated(diagnoseDetails.getUpdated());
                    Diagnose updatedDiagnose = productRepository.save(diagnose);
                    return ResponseEntity.ok(updatedDiagnose);
                }
        ).orElseGet(() -> createProduct(diagnoseDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Diagnose> deleteProduct(@PathVariable(value =
            "id") Long productId) {
        return productRepository
                .findById(productId)
                .map(
                        diagnose -> {
                            productRepository.delete(diagnose);
                            return ResponseEntity.ok().<Diagnose>build();
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
