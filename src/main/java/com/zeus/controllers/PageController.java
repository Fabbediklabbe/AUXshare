package com.zeus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    public PageController() {
        System.out.println("PageController har laddats!");
    }

    @GetMapping("/")
    public String showIndex() {
        return "index"; // laddar templates/index.html
    }

    @GetMapping("/auxshare")
    public String showAuxshare() {
        return "auxshare"; // laddar templates/auxshare.html
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "Fabian");
        return "hello"; // Matchar hello.html
    }
}
