package ru.kata.spring.boot_security.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) { this.userService = userService; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/form";
    }

    @PostMapping
    public String create(@ModelAttribute User user, RedirectAttributes ra) {
        try {
            userService.create(user);
            ra.addFlashAttribute("ok", "User created");
            return "redirect:/admin";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            ra.addFlashAttribute("user", user);
            return "redirect:/admin/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "admin/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute User user, RedirectAttributes ra) {
        try {
            userService.update(id, user);
            ra.addFlashAttribute("ok", "User updated");
            return "redirect:/admin";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            ra.addFlashAttribute("user", user);
            return "redirect:/admin/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        userService.delete(id);
        ra.addFlashAttribute("ok", "User deleted");
        return "redirect:/admin";
    }
}
