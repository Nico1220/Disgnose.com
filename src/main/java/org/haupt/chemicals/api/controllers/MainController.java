package org.haupt.chemicals.api.controllers;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import freemarker.core.Environment;

import org.haupt.chemicals.api.model.*;
import org.haupt.chemicals.api.repository.CartRepository;
import org.haupt.chemicals.api.repository.OrderRepository;
import org.haupt.chemicals.api.repository.ProductRepository;
import org.haupt.chemicals.api.repository.UserRepository;
import org.haupt.chemicals.api.service.MailJetTemplate;
import org.haupt.chemicals.api.service.OrderService;
import org.haupt.chemicals.api.service.ProductService;
import org.haupt.chemicals.api.service.UserODService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private OrderRepository orderRepository;

    @Autowired
    private UserODService userService;

    @Autowired
    private OrderService orderService;

    MailJetTemplate mailJetTemplate = new MailJetTemplate();

    @Value("${app.apiKey}")
    private String apiKey;

    @Value("${app.apiSecret}")
    private String apiSecret;

    Properties properties = new Properties();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/")
    public String index(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        model.addAttribute("authentication", authentication.getName());
        return "index";
    }

    @GetMapping("/no-sidebar.html")
    public String erklärung(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
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
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        model.addAttribute("authentication", authentication.getName());
        return "impressum";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
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
        cart.setCreated(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        cart.setUpdated(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        cartRepository.save(cart);
        return "redirect:/login";
    }


    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'MITARBEITER', 'CUSTOMER')")
    @GetMapping("/contact.html")
    public String contactGet(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        model.addAttribute("authentication", authentication.getName());
        model.addAttribute("mail", new Mail());
        return "contact";
    }

    @GetMapping("/send")
    public String sendEmail(Mail mail) throws MailjetException, MailjetSocketTimeoutException {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(apiKey, apiSecret, new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", authentication.getName())
                                        .put("Name", authentication.getName()))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", "wro19770@spengergasse.at")
                                                .put("Name", "Ala")))
                                .put(Emailv31.Message.SUBJECT, mail.getSubject())
                                .put(Emailv31.Message.TEXTPART, mail.getBody())
                                .put(Emailv31.Message.HTMLPART, "<h3>Dear passenger 1, welcome to <a href='https://www.mailjet.com/'>Mailjet</a>!</h3><br />May the delivery force be with you!")
                                .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
//
        return "redirect:/";
    }


    @GetMapping(value = "/product.html")
    public String getProduct(Model product) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            product.addAttribute("role", user.getRoles());
        }
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
            if(authentication.getName()!="anonymousUser"){
                User user = userRepo.findByMail(authentication.getName());
                System.out.println(user.getRoles());
                model.addAttribute("role", user.getRoles());
            }
            model.addAttribute("authentication", authentication.getName());
            return "product";
        }
        else {
            List<Product> ProductListAll = productRepository.findAll();
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            if(authentication.getName()!="anonymousUser"){
                User user = userRepo.findByMail(authentication.getName());
                System.out.println(user.getRoles());
                model.addAttribute("role", user.getRoles());
            }
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
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        model.addAttribute("authentication", authentication.getName());
        model.addAttribute("product", new Product());
        return "addProductForm";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        model.addAttribute("authentication", authentication.getName());
        product.setCreated(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        product.setUpdated(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        productRepository.save(product);
        return "redirect:/product.html";
    }

    @GetMapping({"/showUpdateProduct", "/showUpdateProduct{productId}"})
    public String showUpdateProduct(@RequestParam Long productId, Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        model.addAttribute("authentication", authentication.getName());
        Product product = productRepository.findById(productId).get();
        model.addAttribute("product", product);
        model.addAttribute("productId", productId);
        return "addProductForm";
    }

    @GetMapping("/deleteProduct")
    String deleteProduct(@RequestParam Long productId, Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        model.addAttribute("authentication", authentication.getName());
        productRepository.deleteById(productId);
        return  "redirect:/product.html";
    }

    @PreAuthorize("hasAnyRole('MITARBEITER', 'ADMIN')")
    @GetMapping({"/users", "/users{email}"})
    public String users(@ModelAttribute("email") @RequestParam("email") Optional<String> email, User user, Model model){
        if(email.isPresent() && email.get() != ""){
            List<User> userx = userService.findUsersByEmil(email.get());
            model.addAttribute("userx",userx);
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            if(authentication.getName()!="anonymousUser"){
                System.out.println(user.getRoles());
                model.addAttribute("role", user.getRoles());
            }
            model.addAttribute("authentication", authentication.getName());
            return "users";
        }
        else{
            List<User> UserListAll = userRepo.findAll();
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            if(authentication.getName()!="anonymousUser"){
                System.out.println(user.getRoles());
                model.addAttribute("role", user.getRoles());
            }
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
        if(authentication.getName()!="anonymousUser"){
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        model.addAttribute("user", user);
        return "addUserForm";
    }

    @GetMapping({"/deleteUser", "deleteUser{email}"})
    String deleteUser(@RequestParam String email, Model model){
        if(email.equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            return "redirect:/users.html";
        }
        else{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            if(authentication.getName()!="anonymousUser"){
                User user = userRepo.findByMail(authentication.getName());
                System.out.println(user.getRoles());
                model.addAttribute("role", user.getRoles());
            }
            model.addAttribute("authentication", authentication.getName());
            Cart cart = cartRepository.findByUser(authentication.getName());
            cartRepository.delete(cart);
            userRepo.deleteById(email);
            return  "redirect:/users.html";
        }

    }

    @GetMapping("/warenkorb")
    public String warenkorb(Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
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
            return "redirect:/login";
        }
    }

    @GetMapping("/addWarenkorb")
    String addWarenkorb(@RequestParam long productId, Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        if(workingUser.equals("anonymousUser")){
            return "redirect:/login";
        }
        else{
            model.addAttribute("authentication", authentication.getName());
            Cart cart = cartRepository.findByUser(authentication.getName());
            Product products = productRepository.findById(productId);
            cart.getProducts().add(products);
            cart.setUpdated(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
            cartRepository.save(cart);
            return  "redirect:/warenkorb";
        }
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

    @GetMapping("/bestellen")
    String bestellen() throws MailjetException, MailjetSocketTimeoutException {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        Cart cart = cartRepository.findByUser(authentication.getName());
        Order order = new Order();
        order.setProducts(List.copyOf(cart.getProducts()));
        order.setUser(userRepo.findByMail(authentication.getName()));
        order.setStatus("BESTELLT");
        order.setCreated(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        orderRepository.save(order);
        mailJetTemplate.mailTemplate(authentication.getName(), authentication.getName(), cart.getProducts(), apiKey, apiSecret);
        cartRepository.delete(cart);
        Cart cartNew = new Cart();
        cartNew.setUser(userRepo.findByMail(authentication.getName()));
        cartNew.setCreated(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        cartNew.setUpdated(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        cartRepository.save(cartNew);
        return  "redirect:/";
    }

    @GetMapping({"/bestellungen", "bestellungen{email}"})
    public String bestellungen(@ModelAttribute("email") @RequestParam("email") Optional<String> email, User user, Model model){
        if(email.isPresent() && email.get() != ""){
            List<Order> oderByUser = orderService.findOrderByUser(email.get());
            model.addAttribute("oderByUser",oderByUser);
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            if(authentication.getName()!="anonymousUser"){
                user = userRepo.findByMail(authentication.getName());
                System.out.println(user.getRoles());
                model.addAttribute("role", user.getRoles());
            }
            model.addAttribute("authentication", authentication.getName());
            return "bestellungen";
        }
        else{
            List<Order> OrderListAll = orderRepository.findAll();
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            if(authentication.getName()!="anonymousUser"){
                user = userRepo.findByMail(authentication.getName());
                System.out.println(user.getRoles());
                model.addAttribute("role", user.getRoles());
            }
            model.addAttribute("authentication", authentication.getName());
            model.addAttribute("OrderListAll", OrderListAll);
            return "bestellungen";
        }
    }

    @PostMapping("/saveOrder")
    public String saveOrder(@RequestParam Long id, @ModelAttribute Order order, Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        Order details = orderRepository.findById(id).get();
        order.setUser(details.getUser());
        order.setCreated(details.getCreated());
        order.setProducts(details.getProducts());
        model.addAttribute("authentication", authentication.getName());
        orderRepository.save(order);
        return "redirect:/bestellungen";
    }

    @GetMapping("/showSpecificOrder{id}")
    public String showSpecificOrder(@RequestParam Long id, Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        model.addAttribute("authentication", authentication.getName());
        Order order = orderRepository.findById(id).get();
        List products = order.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("order", order);
        return "addOrderForm";
    }

    @GetMapping("/deleteOrder")
    String deleteOrder(@RequestParam Long id, Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        model.addAttribute("authentication", authentication.getName());
        orderRepository.deleteById(id);
        return  "redirect:/bestellungen";

    }
}
