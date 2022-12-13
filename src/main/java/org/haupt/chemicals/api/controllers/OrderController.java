package org.haupt.chemicals.api.controllers;

import org.haupt.chemicals.api.model.Order;
import org.haupt.chemicals.api.repository.OrderRepository;
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

@RequestMapping(path = "api/order")
@RestController
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public @ResponseBody
    Iterable<Order> getAllOrder() {
        return orderRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return orderRepository
                .findById(id)
                .map(order -> ResponseEntity.ok().body(order))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        order.setId(null);
        var saved = orderRepository.save(order);
        return ResponseEntity.created(URI.create("/api/order/" + saved.getId())).body(saved);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable(value = "id") Long orderId, @RequestBody Order orderDetails) {
        return orderRepository.findById(orderId).map(order -> {
                    order.setUser(orderDetails.getUser());
                    order.setStatus(orderDetails.getStatus());
                    order.setContent(orderDetails.getContent());
                    order.setProducts(orderDetails.getProducts());
                    Order updatedOrder = orderRepository.save(order);
                    return ResponseEntity.ok(updatedOrder);
                }
        ).orElseGet(() -> createOrder(orderDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable(value =
            "id") Long orderId) {
        return orderRepository
                .findById(orderId)
                .map(
                        order -> {
                            orderRepository.delete(order);
                            return ResponseEntity.ok().<Order>build();
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
