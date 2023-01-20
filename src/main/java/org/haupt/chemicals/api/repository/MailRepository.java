package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Order, String> {
}
