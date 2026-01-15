package com.auxshare.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String showAuxshare() {
        return "auxshare"; // templates/auxshare.html
    }
}

