package com.auxshare.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String showAuxshareRoot() {
        return "auxshare"; // templates/auxshare.html
    }

    // valfritt: behåll /auxshare som alias
    @GetMapping("/auxshare")
    public String showAuxshareAlias() {
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
    	return "login";
    }

    @GetMapping("/register")
    public String register() { 
    	return "register"; 
    }
}

