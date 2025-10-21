package ru.kata.spring.boot_security.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminUserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam(value = "ok", required = false) String ok,
                        @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("ok", ok);
        model.addAttribute("error", error);
        return "admin/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/form";
    }

    @PostMapping
    public String create(@ModelAttribute User user,
                         @RequestParam(value = "roles", required = false) List<String> roleNames) {
        user.setRoles(resolveRoles(roleNames));
        try {
            userService.create(user);
            return "redirect:/admin?ok=Created";
        } catch (IllegalArgumentException ex) {
            return "redirect:/admin?error=" + url(ex.getMessage());
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User u = userService.findById(id);
        model.addAttribute("user", u);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute User patch,
                         @RequestParam(value = "roles", required = false) List<String> roleNames) {
        patch.setRoles(resolveRoles(roleNames));
        try {
            userService.update(id, patch);
            return "redirect:/admin?ok=Updated";
        } catch (IllegalArgumentException ex) {
            return "redirect:/admin?error=" + url(ex.getMessage());
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin?ok=Deleted";
    }

    private Set<Role> resolveRoles(List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) return java.util.Collections.emptySet();
        return roleNames.stream()
                .map(roleRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private String url(String s) {
        return s == null ? "" : s.replace(" ", "%20");
    }
}
