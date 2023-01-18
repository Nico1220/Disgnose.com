package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Cart;
import org.haupt.chemicals.api.model.Order;
import org.haupt.chemicals.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

//    @Query("SELECT o FROM Order o inner join User u on o.user=u.email where u.email like %?1%")
//    public Order findOderByUser(String email);

    @Query("SELECT o FROM Order o inner join User u on o.user=u.email where u.email like %?1%")
    public List<Order> findOrderListByUser(String email);
}
