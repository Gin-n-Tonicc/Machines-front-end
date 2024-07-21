package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.CategoryClient;
import com.machines.machines_front_end.dtos.request.CategoryRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryClient categoryClient;

    @GetMapping("/create")
    public String showCreateCategoryForm(Model model) {
        model.addAttribute("category", new CategoryRequestDTO());
        return "categories/create";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute("category") CategoryRequestDTO categoryDTO, Model model) {
        try {
            categoryClient.create(categoryDTO);
            return "redirect:/index";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            return "categories/create";
        }
    }
}