package org.haupt.chemicals.api.controllers;

import org.haupt.chemicals.api.model.Mail;
import org.haupt.chemicals.api.model.Product;
import org.haupt.chemicals.api.model.User;
import org.haupt.chemicals.api.repository.ProductRepository;
import org.haupt.chemicals.api.repository.RoleRepository;
import org.haupt.chemicals.api.repository.UserRepository;
import org.haupt.chemicals.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private ProductService productService;

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

    @GetMapping({"/product.html", "/product.html{titel}" })
    public String suchfunktion(@ModelAttribute("titel") @RequestParam("titel") Optional<String> titel, Model model, Product product){
        if(titel.isPresent()){
            List<Product> ProductList = productService.findProductByTitel(titel.get());
            model.addAttribute("product",product);
            model.addAttribute("product2", ProductList);

            return "product";
        }
        else {
            List<Product> ProductListAll = productRepository.findAll();
            model.addAttribute("model", new Product());
            model.addAttribute("productAll", ProductListAll);
            return "product";
        }
    }

    @GetMapping("/addProduct")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        return "addProductForm";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product) {
        product.setCreated(LocalDateTime.now());
        product.setUpdated(LocalDateTime.now());
        productRepository.save(product);
        return "redirect:/product.html";
    }

    @GetMapping({"/showUpdateProduct", "/showUpdateProduct{productId}"})
    public String showUpdateProduct(@RequestParam Long productId, Model model) {
        Product product = productRepository.findById(productId).get();
        model.addAttribute("product", product);
        return "addProductForm";
    }

    @GetMapping("/deleteProduct")
    String deleteProduct(@RequestParam Long productId){
        productRepository.deleteById(productId);
        return  "redirect:/product.html";
    }

    @GetMapping("/users")
    public String users(User user){
        return "users";
    }

    @PostMapping("/deleteUser")
    String deleteUser(User user){
        userRepo.delete(userRepo.findByEmail(user.getEmail()));
        return  "users";
    }
}
