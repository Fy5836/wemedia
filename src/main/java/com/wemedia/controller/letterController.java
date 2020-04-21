package com.wemedia.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class letterController {
    @GetMapping("/letters")
    public String letter(){
        return "letter/list";
    }
}

