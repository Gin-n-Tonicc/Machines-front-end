package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.CategoryClient;
import com.machines.machines_front_end.clients.SubcategoryClient;
import com.machines.machines_front_end.dtos.request.CategoryRequestDTO;
import com.machines.machines_front_end.dtos.response.CategoryAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.CategoryResponseDTO;
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
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryClient categoryClient;
    private final SubcategoryClient subcategoryClient;

    @GetMapping("/create")
    public String showCreateCategoryForm(Model model) {
        model.addAttribute("category", new CategoryRequestDTO());
        return "categories/create";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute("category") CategoryRequestDTO categoryDTO, Model model) {
        try {
            categoryClient.create(categoryDTO);
            return "redirect:/categories";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            return "categories/create";
        }
    }

    @GetMapping
    public String listCategories(Model model) {
        List<CategoryAdminResponseDTO> categories = categoryClient.getAllAdmin();
        model.addAttribute("categories", categories);
        return "categories/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateCategoryForm(@PathVariable UUID id, Model model) {
        CategoryResponseDTO category = categoryClient.getByIdAdmin(id);
        model.addAttribute("category", category);
        return "categories/update";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable UUID id, @ModelAttribute("category") CategoryRequestDTO categoryDTO, Model model) {
        try {
            categoryClient.update(id, categoryDTO);
            return "redirect:/categories";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            model.addAttribute("category", categoryDTO);
            return "categories/update";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable UUID id) {
        categoryClient.delete(id);
        return "redirect:/categories";
    }
}