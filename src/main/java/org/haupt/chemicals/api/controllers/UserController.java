package org.haupt.chemicals.api.controllers;

import org.haupt.chemicals.api.model.Mail;
import org.haupt.chemicals.api.model.User;
import org.haupt.chemicals.api.repository.UserRepository;
import org.haupt.chemicals.api.repository.SendingMail;
import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping(path = "")
@Controller
public class UserController {
    @Autowired
    private UserRepository userRepo;

    SendingMail sendingMail;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/no-sidebar.html")
    public String erklärung() {
        return "no-sidebar";
    }

    @GetMapping("login")
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
        userRepo.save(user);

        return "index";
    }

    @GetMapping("/contact.html")
    public String contactGet(Model model) {
        model.addAttribute("mail", new Mail());
        return "contact";
    }

    @PostMapping("/contact.html")
    public String contactPost(@Valid Mail mail, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "contact";
        }

        model.addAttribute("noErrors", true);
        model.addAttribute("user", mail);
        String subject = mail.getName() + " " + mail.getEmail() + " "+ mail.getSubject() +" sent you a message";
        sendingMail.sendMail(subject, mail.getMessage());
        return "contact";
    }
}
