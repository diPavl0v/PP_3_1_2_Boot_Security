package ru.kata.spring.boot_security.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller @RequestMapping("/admin")
public class AdminUserController {
    private final UserService users;
    private final RoleRepository roles;

    public AdminUserController(UserService users, RoleRepository roles) { this.users=users; this.roles=roles; }

    @GetMapping public String index(Model m){ m.addAttribute("users", users.findAll()); return "admin/index"; }

    @GetMapping("/new") public String createForm(Model m){ m.addAttribute("user", new User()); m.addAttribute("roles", roles.findAll()); return "admin/form"; }

    @PostMapping public String create(@ModelAttribute User user){ users.create(user); return "redirect:/admin"; }

    @GetMapping("/{id}/edit") public String edit(@PathVariable Long id, Model m){ m.addAttribute("user", users.findById(id)); m.addAttribute("roles", roles.findAll()); return "admin/form"; }

    @PostMapping("/{id}") public String update(@PathVariable Long id, @ModelAttribute User user){ users.update(id, user); return "redirect:/admin"; }

    @PostMapping("/{id}/delete") public String delete(@PathVariable Long id){ users.delete(id); return "redirect:/admin"; }
}
