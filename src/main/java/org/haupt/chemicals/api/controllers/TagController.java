package org.haupt.chemicals.api.controllers;

import org.haupt.chemicals.api.model.Order;
import org.haupt.chemicals.api.model.Tag;
import org.haupt.chemicals.api.repository.TagRepository;
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

@RequestMapping(path = "api/tag")
@RestController
@CrossOrigin
public class TagController {
    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public @ResponseBody
    Iterable<Tag> getAllTag() {
        return tagRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTag(@PathVariable Long id) {
        return tagRepository
                .findById(id)
                .map(tag -> ResponseEntity.ok().body(tag))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Tag> createTag(@Valid @RequestBody Tag tag) {
        tag.setId(null);
        Tag saved = tagRepository.save(tag);
        return ResponseEntity.created(URI.create("/api/tag/" + saved.getId())).body(saved);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable(value = "id") Long tagId, @RequestBody Tag tagDetails) {
        return tagRepository.findById(tagId).map(tag -> {
                    tag.setTitel(tagDetails.getTitel());
                    tag.setContent(tagDetails.getContent());
                    Tag updatedTag = tagRepository.save(tag);
                    return ResponseEntity.ok(updatedTag);
                }
        ).orElseGet(() -> createTag(tagDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable(value =
            "id") Long tagId) {
        return tagRepository
                .findById(tagId)
                .map(
                        tag -> {
                            tagRepository.delete(tag);
                            return ResponseEntity.ok().<Tag>build();
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
