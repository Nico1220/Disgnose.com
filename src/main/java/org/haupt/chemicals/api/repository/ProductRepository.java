package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
