package org.haupt.chemicals.api.controllers;

import org.haupt.chemicals.api.model.Mail;
import org.haupt.chemicals.api.model.Product;
import org.haupt.chemicals.api.model.User;
import org.haupt.chemicals.api.repository.ProductRepository;
import org.haupt.chemicals.api.repository.RoleRepository;
import org.haupt.chemicals.api.repository.SendingMail;
import org.haupt.chemicals.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;

@RequestMapping(path = "")
@Controller
public class MainController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductRepository productRepository;
//    SendingMail sendingMail;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/no-sidebar.html")
    public String erklärung() {
        return "no-sidebar";
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("/impressum.html")
    public String impressum() {
        return "impressum";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles(new HashSet<>(roleRepository.findByName("USER")));
        userRepo.save(user);

        return "index";
    }

    @PostMapping("/contact.html")
    public String contactGet(Model model) {
        model.addAttribute("mail", new Mail());
        return "contact";
    }

    @GetMapping(value = "/product.html")
    public String getProduct(Model product) {
        product.addAttribute("product", new Product());
        return "product";}
    @PostMapping("/product_eingetragen")
    public String product_eingetragen(Product product) {
        product.setCreated(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        product.setUpdated(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        productRepository.save(product);
        return "product";
    }
}
