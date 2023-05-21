package org.haupt.chemicals.api.service;

import org.haupt.chemicals.api.model.Diagnose;
import org.haupt.chemicals.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public List<Diagnose> findProductByTitel(String title) {
        List<Diagnose> diagnoses = new ArrayList<>();
        productRepository.findByTitel(title)
                .forEach(diagnoses::add);
        for(Diagnose diagnose : diagnoses)
            System.out.println(diagnose);
        return diagnoses;
    }
}
