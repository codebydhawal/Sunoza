package com.sunoza.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/rest/home")
    public String home() {
        return "Hello World";
    }
}
