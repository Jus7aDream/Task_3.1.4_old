package ru.kata.spring.boot_security.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import javax.validation.Valid;
import java.security.Principal;
//
/**
 * @ In the name of Allah, most gracious and most merciful! 04.10.2022
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {
    private final UserService userService;
    private final UserServiceImpl userServiceImpl;
    private final RoleService roleService;

    @GetMapping("/admin")
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("users")
    public String showAllUsers(Principal principal, Model model) {
        User user = (User) userServiceImpl.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    @GetMapping("userCard")
    public String userCard(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/userCard";
    }

    @PostMapping("users")
    public String addUser(@ModelAttribute("user")
                          @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/admin/userCard";
        userService.addUser(user);
        return "redirect:/admin/users";
    }

    @PatchMapping("users/{id}")
    public String updateUser(@ModelAttribute("user")
                             @Valid User user, BindingResult bindingResult,
                             @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("users/{id}")
    public String getUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "user";
    }

    @GetMapping("users/{id}/edit")
    public String editButton(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/edit";
    }

    @GetMapping("users/{id}/delete")
    public String deleteButton(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/delete";
    }

    @DeleteMapping("users/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}