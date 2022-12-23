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
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

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

    @RequestMapping("/login")
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
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
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
        product.setCreated(LocalDateTime.now());
        product.setUpdated(LocalDateTime.now());
        productRepository.save(product);
        return "product";
    }


    @GetMapping("/users")
    public String users(User user){
        return "users";
    }
//    List<User> getAllUsers() {
//        userRepo.findAll();
//
//    }
//
//    @GetMapping("/user/{id}")
//    User getUserById(@PathVariable Long id) {
//        return userRepo.findById(id)
//                .orElseThrow(() -> new Benutzernichtgefundenexception(id));
//    }
//
//    @PutMapping("/user/{id}")
//    User updateUser(@RequestBody User newUser, @PathVariable Long id) {
//        return userRepo.findById(id)
//                .map(user -> {
//                    user.setUsername(newUser.getUsername());
//                    user.setName(newUser.getName());
//                    user.setEmail(newUser.getEmail());
//                    return userRepository.save(user);
//                }).orElseThrow(() -> new Benutzernichtgefundenexception(id));
//    }
//
    @PostMapping("/deleteUser")
    String deleteUser(User user){
        userRepo.delete(userRepo.findByEmail(user.getEmail()));
        return  "users";
    }

    @PostMapping("/deleteProduct")
    String deleteProduct(Product product){
        productRepository.delete(productRepository.findByTitel(product.getTitel()));
        return  "product";
    }
}
