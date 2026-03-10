package com.openportfolio.resume_maker.controller;

import com.openportfolio.resume_maker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String username, Model model) {
        String trimmed = username.trim().toLowerCase();
        return userService.findByUsername(trimmed)
                .map(u -> "redirect:/profile/" + trimmed)
                .orElseGet(() -> {
                    model.addAttribute("error", "No user found with username: " + trimmed);
                    return "index";
                });
    }
}
