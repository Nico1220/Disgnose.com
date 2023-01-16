package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.titel LIke %?1%")
    public List<Product> findByTitel(String titel);

    @Query("SELECT p FROM Product p WHERE p.id = ?1")
    public Product findById(long titel);
}
