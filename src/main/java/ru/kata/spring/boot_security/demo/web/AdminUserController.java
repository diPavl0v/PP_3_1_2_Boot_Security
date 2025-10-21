package ru.kata.spring.boot_security.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final UserService users;
    private final RoleRepository roles;

    public AdminUserController(UserService users, RoleRepository roles) {
        this.users = users;
        this.roles = roles;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", users.findAll());
        return "admin/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roles.findAll());
        return "admin/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", users.findById(id));
        model.addAttribute("allRoles", roles.findAll());
        return "admin/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("user") User user,
                         BindingResult br,
                         Model model) {
        if (br.hasErrors()) {
            model.addAttribute("allRoles", roles.findAll());
            return "admin/form";
        }
        try {
            users.create(user);
            return "redirect:/admin";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("allRoles", roles.findAll());
            return "admin/form";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("user") User patch,
                         Model model) {
        try {
            users.update(id, patch);
            return "redirect:/admin";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("user", users.findById(id));
            model.addAttribute("allRoles", roles.findAll());
            return "admin/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        users.delete(id);
        return "redirect:/admin";
    }
}
