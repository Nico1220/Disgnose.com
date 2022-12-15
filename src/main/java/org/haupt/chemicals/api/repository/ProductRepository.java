package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Product;
import org.haupt.chemicals.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public Set<Product> findByTitel(String TITEL);
}
