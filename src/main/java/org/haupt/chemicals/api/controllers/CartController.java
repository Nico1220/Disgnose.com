package org.haupt.chemicals.api.controllers;

import org.haupt.chemicals.api.model.Cart;
import org.haupt.chemicals.api.repository.CartRepository;
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

@RequestMapping(path = "api/cart")
@RestController
@CrossOrigin
public class CartController {
    @Autowired
    private CartRepository cartRepository;

    @GetMapping
    public @ResponseBody
    Iterable<Cart> getAllCart() {
        return cartRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable Long id) {
        return cartRepository
                .findById(id)
                .map(cart -> ResponseEntity.ok().body(cart))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Cart> createCart(@Valid @RequestBody Cart cart) {
        cart.setId(null);
        Cart saved = cartRepository.save(cart);
        return ResponseEntity.created(URI.create("/api/cart/" + saved.getId())).body(saved);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable(value = "id") Long cartId, @RequestBody Cart cartDetails) {
        return cartRepository.findById(cartId).map(cart -> {
                    cart.setUser(cartDetails.getUser());
                    cart.setCreated(cartDetails.getCreated());
                    cart.setUpdated(cartDetails.getUpdated());
                    cart.setProducts(cartDetails.getProducts());
                    Cart updatedCart = cartRepository.save(cart);
                    return ResponseEntity.ok(updatedCart);
                }
        ).orElseGet(() -> createCart(cartDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cart> deleteCart(@PathVariable(value =
            "id") Long cartId) {
        return cartRepository
                .findById(cartId)
                .map(
                        cart -> {
                            cartRepository.delete(cart);
                            return ResponseEntity.ok().<Cart>build();
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
