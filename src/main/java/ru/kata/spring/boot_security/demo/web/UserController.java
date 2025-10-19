package ru.kata.spring.boot_security.demo.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.User;

@Controller
public class UserController {
    @GetMapping("/user")
    public String me(@AuthenticationPrincipal User me, Model m) {
        m.addAttribute("user", me);
        return "user";
    }
    @GetMapping("/login")
    public String login(){ return "login"; }
}
