package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
