package org.haupt.chemicals.api.controllers;

import org.haupt.chemicals.api.model.Cart;
import org.haupt.chemicals.api.model.Mail;
import org.haupt.chemicals.api.model.Product;
import org.haupt.chemicals.api.model.User;
import org.haupt.chemicals.api.repository.CartRepository;
import org.haupt.chemicals.api.repository.ProductRepository;
import org.haupt.chemicals.api.repository.UserRepository;
import org.haupt.chemicals.api.service.ProductService;
import org.haupt.chemicals.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/")
    public String index(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        if(authentication.getName()!="anonymousUser"){
//            User user = userRepo.findByMail(authentication.getName());
//            System.out.println(user.getRoles());
//            model.addAttribute("role", user.getRoles());
//        }
        model.addAttribute("authentication", authentication.getName());
        return "index";
    }

    @GetMapping("/no-sidebar.html")
    public String erklärung(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        return "no-sidebar";
    }

//    @RequestMapping("/login")
//    public String login() {
//        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        String workingUser = authentication.getName();
//        System.out.println(workingUser);
//        return "login";
//    }

    @GetMapping("/impressum.html")
    public String impressum(Model model)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        return "impressum";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles("USER");
        userRepo.save(user);
        Cart cart = new Cart();
        cart.setUser(userRepo.findByMail(user.getEmail()));
        cart.setCreated(LocalDateTime.now());
        cart.setUpdated(LocalDateTime.now());
        cartRepository.save(cart);
        return "index";
    }

    @GetMapping("/contact.html")
    public String contactGet(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        model.addAttribute("mail", new Mail());
        return "contact";
    }

    @GetMapping(value = "/product.html")
    public String getProduct(Model product) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        product.addAttribute("authentication", authentication.getName());
        product.addAttribute("product", new Product());
        return "product";}

    @GetMapping({"/product.html", "/product.html{titel}" })
    public String suchfunktion(@ModelAttribute("titel") @RequestParam("titel") Optional<String> titel, Model model, Product product){
        if(titel.isPresent() && titel.get() != ""){
            List<Product> ProductList = productService.findProductByTitel(titel.get());
            model.addAttribute("product",product);
            model.addAttribute("product2", ProductList);
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            model.addAttribute("authentication", authentication.getName());
            return "product";
        }
        else {
            List<Product> ProductListAll = productRepository.findAll();
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            model.addAttribute("authentication", authentication.getName());
            model.addAttribute("model", new Product());
            model.addAttribute("productAll", ProductListAll);
            return "product";
        }
    }

    @GetMapping("/addProduct")
    public String addProduct(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        model.addAttribute("product", new Product());
        return "addProductForm";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        product.setCreated(LocalDateTime.now());
        product.setUpdated(LocalDateTime.now());
        productRepository.save(product);
        return "redirect:/product.html";
    }

    @GetMapping({"/showUpdateProduct", "/showUpdateProduct{productId}"})
    public String showUpdateProduct(@RequestParam Long productId, Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        Product product = productRepository.findById(productId).get();
        model.addAttribute("product", product);
        return "addProductForm";
    }

    @GetMapping("/deleteProduct")
    String deleteProduct(@RequestParam Long productId, Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        productRepository.deleteById(productId);
        return  "redirect:/product.html";
    }

    @GetMapping({"/users", "/users{email}"})
    public String users(@ModelAttribute("email") @RequestParam("email") Optional<String> email, User user, Model model){
        if(email.isPresent() && email.get() != ""){
            User userx = userRepo.findByMail(email.get());
            model.addAttribute("userx",userx);
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            model.addAttribute("authentication", authentication.getName());
            return "users";
        }
        else{
            List<User> UserListAll = userRepo.findAll();
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            model.addAttribute("authentication", authentication.getName());
            model.addAttribute("model", new User());
            model.addAttribute("userAll", UserListAll);
            return "users";
        }
    }
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        System.out.println(userRepo.findByMail(user.getEmail()).getPassword());
        if(user.getPassword() == "") {
            user.setPassword(userRepo.findByMail(user.getEmail()).getPassword());
        }
        else {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }
        userRepo.save(user);
        return "redirect:/users.html";
    }

    @GetMapping({"/showUpdateUser", "/showUpdateUser{email}"})
    public String showUpdateUser(@RequestParam String email, Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        User user = userRepo.findById(email).get();
        model.addAttribute("user", user);
        return "addUserForm";
    }

    @GetMapping("/deleteUser")
    String deleteUser(@RequestParam String email, Model model){
        if(email == SecurityContextHolder.getContext().getAuthentication().getName()){
            return "redirect:/users.html";
        }
        else{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            model.addAttribute("authentication", authentication.getName());
            userRepo.deleteById(email);
            return  "redirect:/users.html";
        }

    }

    @GetMapping("/warenkorb")
    public String warenkorb(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        if(cartRepository.findByUser(authentication.getName()) != null){
            Cart cart = cartRepository.findByUser(authentication.getName());
            List<Product> products = cart.getProducts();
            model.addAttribute("cart", cart);
            model.addAttribute("products", products);
            return "warenkorb";
        }
        else{
            model.addAttribute("products", new Product());
            return "redirect/:login";
        }
    }

    @GetMapping("/addWarenkorb")
    String addWarenkorb(@RequestParam long productId, Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        Cart cart = cartRepository.findByUser(authentication.getName());
        Product products = productRepository.findById(productId);
        cart.getProducts().add(products);
        cartRepository.save(cart);
        return  "redirect:/warenkorb";
    }

    @GetMapping("/deleteWarenkorb")
    String deleteWarenkorb(@RequestParam long productId, Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        model.addAttribute("authentication", authentication.getName());
        Cart cart = cartRepository.findByUser(authentication.getName());
        cart.getProducts().remove(productRepository.findById(productId));
        cartRepository.save(cart);
        return  "redirect:/warenkorb";
    }


}