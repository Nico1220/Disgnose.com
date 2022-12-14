package org.haupt.chemicals.api.controllers;

import org.haupt.chemicals.api.model.Role;
import org.haupt.chemicals.api.repository.RoleRepository;
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

@RequestMapping(path = "api/role")
@RestController
@CrossOrigin
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public @ResponseBody
    Iterable<Role> getAllRole() {
        return roleRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        return roleRepository
                .findById(id)
                .map(role -> ResponseEntity.ok().body(role))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        role.setId(null);
        var saved = roleRepository.save(role);
        return ResponseEntity.created(URI.create("/api/role/" + saved.getId())).body(saved);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable(value = "id") Long roleId, @RequestBody Role roleDetails) {
        return roleRepository.findById(roleId).map(role -> {
                    role.setName(roleDetails.getName());
                    Role updatedRole = roleRepository.save(role);
                    return ResponseEntity.ok(updatedRole);
                }
        ).orElseGet(() -> createRole(roleDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Role> deleteRole(@PathVariable(value =
            "id") Long roleId) {
        return roleRepository
                .findById(roleId)
                .map(
                        role -> {
                            roleRepository.delete(role);
                            return ResponseEntity.ok().<Role>build();
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
