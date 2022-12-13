package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
