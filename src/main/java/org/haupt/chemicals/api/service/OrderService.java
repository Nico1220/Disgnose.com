package org.haupt.chemicals.api.service;

import org.haupt.chemicals.api.model.Order;
import org.haupt.chemicals.api.model.Product;
import org.haupt.chemicals.api.repository.OrderRepository;
import org.haupt.chemicals.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public List<Order> findOrderByUser(String title) {
        List<Order> orders = new ArrayList<>();
        orderRepository.findOrderListByUser(title)
                .forEach(orders::add);
        for(Order order:orders)
            System.out.println(order);
        return orders;
    }
}
