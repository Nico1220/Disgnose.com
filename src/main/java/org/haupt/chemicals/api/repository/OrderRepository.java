package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Cart;
import org.haupt.chemicals.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o inner join User u on o.user=u.email where u.email like %?1%")
    public Order findOderByUser(String email);
}
