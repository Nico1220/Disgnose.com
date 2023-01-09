package org.haupt.chemicals.api.service;

import org.haupt.chemicals.api.model.Product;
import org.haupt.chemicals.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public List<Product> findProductByTitel(String title) {
        List<Product> products = new ArrayList<>();
        productRepository.findByTitel(title)
                .forEach(products::add);
        for(Product product:products)
            System.out.println(product);
        return products;
    }
}
