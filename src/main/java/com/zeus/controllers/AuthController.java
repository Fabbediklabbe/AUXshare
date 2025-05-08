package com.zeus.controllers;
import com.zeus.models.User;
import com.zeus.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;


@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/auxshare/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/auxshare/register")
    public String processRegister(@RequestParam String username, @RequestParam String password, Model model) {
    if (userRepository.findByUsername(username).isPresent()) {
        model.addAttribute("error", "Användarnamnet är redan taget.");
        return "register";
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
    return "redirect:/auxshare/login";
}

    @GetMapping("/auxshare/login")
    public String showLoginForm() {
        return "login";
    }
}

