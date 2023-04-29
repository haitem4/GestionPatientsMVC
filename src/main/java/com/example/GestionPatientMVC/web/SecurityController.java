package com.example.GestionPatientMVC.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/noAuthorized")
    public String noAuthoried() {
        return "noAuthorized";
    }
}
