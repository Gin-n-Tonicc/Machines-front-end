package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.CategoryClient;
import com.machines.machines_front_end.clients.UserClient;
import com.machines.machines_front_end.dtos.auth.AdminUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserClient userClient;
    private final CategoryClient categoryClient;

    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new AdminUserDTO());
        return "users/create";
    }

    @GetMapping
    public String listUsers(Model model) {
        List<AdminUserDTO> users = userClient.getAllUsers();
        model.addAttribute("users", users);
        return "users/list";
    }


    @GetMapping("/update/{id}")
    public String showUpdateUserForm(@PathVariable UUID id, Model model) {
        AdminUserDTO user = userClient.getByIdAdmin(id);
        model.addAttribute("user", user);
        return "users/update";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable UUID id, @ModelAttribute("user") AdminUserDTO userDTO, Model model) {
        try {
            System.out.println(userDTO.toString());
            userClient.update(id, userDTO);
            return "redirect:/";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            AdminUserDTO user = userClient.getByIdAdmin(id);

            model.addAttribute("user", user);
            return "users/update";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable UUID id) {
        userClient.deleteUser(id);
        return "redirect:/users";
    }
}
