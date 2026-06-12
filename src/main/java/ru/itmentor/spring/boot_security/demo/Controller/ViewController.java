package ru.itmentor.spring.boot_security.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/admin")
    public  String adminPage() {
        return "admin";
    }

    @GetMapping("/user")
    public  String userPage() {
        return "user";
    }
}
