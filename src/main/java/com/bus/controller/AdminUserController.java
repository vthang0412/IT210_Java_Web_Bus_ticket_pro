package com.bus.controller;

import com.bus.entity.User;
import com.bus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String list(Model model){

        model.addAttribute("users", userRepository.findAll());

        return "admin/user-list";
    }

    @GetMapping("/create")
    public String create(Model model){

        model.addAttribute("user", new User());

        return "admin/user-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user){

        // encode password when saving
        if(user.getPassword() != null){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);

        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){

        model.addAttribute("user", userRepository.findById(id).orElseThrow());

        return "admin/user-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){

        userRepository.deleteById(id);

        return "redirect:/admin/users";
    }
}
