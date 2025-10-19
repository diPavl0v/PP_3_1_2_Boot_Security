package app.controller;

import app.model.User;
import app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class UserController {
    @Autowired private UserService userService;

    @GetMapping("/")                 // <— маппинг корня
    public String index(){ return "redirect:/users"; }

    @GetMapping("/users")
    public String users(Model m){ m.addAttribute("users", userService.getAll()); return "users"; }

    @GetMapping("/userForm")
    public String form(@RequestParam(required=false) Long id, Model m){
        m.addAttribute("user", id==null? new User(): userService.getById(id));
        return "userForm";
    }

    @PostMapping("/saveUser")
    public String save(@RequestParam(required=false) Long id,
                       @RequestParam String name,
                       @RequestParam String email,
                       @RequestParam(required=false) Integer age){
        User u = id==null? new User(): userService.getById(id);
        if(u==null) u = new User();
        u.setName(name); u.setEmail(email); u.setAge(age);
        userService.save(u);
        return "redirect:/users";
    }

    @PostMapping("/deleteUser")
    public String delete(@RequestParam Long id){ userService.delete(id); return "redirect:/users"; }
}
