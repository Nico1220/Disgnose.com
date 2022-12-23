package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Product;
import org.haupt.chemicals.api.model.Role;
import org.haupt.chemicals.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.titel = ?1")
    public Product findByTitel(String titel);
}
