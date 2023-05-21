package org.haupt.chemicals.api.controllers;

import com.mailjet.client.errors.MailjetException;

import org.haupt.chemicals.api.model.*;
import org.haupt.chemicals.api.repository.ProductRepository;
import org.haupt.chemicals.api.repository.UserRepository;
import org.haupt.chemicals.api.service.ProductService;
import org.haupt.chemicals.api.service.UserODService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ProductService productService;

    @Autowired
    private UserODService userService;

    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.GERMAN);

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
    public String processRegister(User user) throws MailjetException {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setRoles("ADMIN");
            userRepo.save(user);
            return "redirect:/login";
    }

    @GetMapping(value = "/diagnose.html")
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
        product.addAttribute("product", new Diagnose());
        return "diagnose";}

    @GetMapping({"/diagnose.html", "/diagnose.html{titel}" })
    public String suchfunktion(@ModelAttribute("titel") @RequestParam("titel") Optional<String> titel, Model model, Diagnose diagnose){
        if(titel.isPresent() && titel.get() != ""){
            List<Diagnose> diagnoseList = productService.findProductByTitel(titel.get());
            model.addAttribute("product", diagnose);
            model.addAttribute("product2", diagnoseList);
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            if(authentication.getName()!="anonymousUser"){
                User user = userRepo.findByMail(authentication.getName());
                System.out.println(user.getRoles());
                model.addAttribute("role", user.getRoles());
            }
            model.addAttribute("authentication", authentication.getName());
            return "diagnose";
        }
        else {
            List<Diagnose> diagnoseListAll = productRepository.findAll();
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String workingUser = authentication.getName();
            System.out.println(workingUser);
            if(authentication.getName()!="anonymousUser"){
                User user = userRepo.findByMail(authentication.getName());
                System.out.println(user.getRoles());
                model.addAttribute("role", user.getRoles());
            }
            model.addAttribute("authentication", authentication.getName());
            model.addAttribute("model", new Diagnose());
            model.addAttribute("productAll", diagnoseListAll);
            return "diagnose";
        }
    }

    @PreAuthorize("hasAnyRole('MITARBEITER', 'ADMIN')")
    @GetMapping("/addDiagnose")
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
        model.addAttribute("diagnose", new Diagnose());
        return "addProductForm";
    }

    @PreAuthorize("hasAnyRole('MITARBEITER', 'ADMIN')")
    @PostMapping("/saveDiagnose")
    public String saveProduct(@ModelAttribute Diagnose diagnose, Model model) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String workingUser = authentication.getName();
        System.out.println(workingUser);
        if(authentication.getName()!="anonymousUser"){
            User user = userRepo.findByMail(authentication.getName());
            System.out.println(user.getRoles());
            model.addAttribute("role", user.getRoles());
        }
        model.addAttribute("authentication", authentication.getName());
//        diagnose.setCreated(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
//        diagnose.setUpdated(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        productRepository.save(diagnose);
        return "redirect:/diagnose.html";
    }

    @PreAuthorize("hasAnyRole('MITARBEITER', 'ADMIN')")
    @GetMapping({"/showUpdateDiagnose", "/showUpdateDiagnose{productId}"})
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
        Diagnose diagnose = productRepository.findById(productId).get();
        model.addAttribute("diagnose", diagnose);
        model.addAttribute("productId", productId);
        return "addProductForm";
    }

    @PreAuthorize("hasAnyRole('MITARBEITER', 'ADMIN')")
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
        return  "redirect:/diagnose.html";
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

    @PreAuthorize("hasAnyRole('MITARBEITER', 'ADMIN')")
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

    @PreAuthorize("hasAnyRole('MITARBEITER', 'ADMIN')")
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
}
