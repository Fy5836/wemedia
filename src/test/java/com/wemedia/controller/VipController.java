package com.wemedia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VipController {
    @GetMapping("/viparticles")
    public String vip(){
        return "viparticle/list";
    }
}

