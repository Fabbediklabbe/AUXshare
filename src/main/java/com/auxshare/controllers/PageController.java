package com.auxshare.controllers;

import org.springframework.stereotype.Controller;
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
}
