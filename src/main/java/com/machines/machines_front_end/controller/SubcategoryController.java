package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.annotations.PageRoleGuard;
import com.machines.machines_front_end.clients.CategoryClient;
import com.machines.machines_front_end.clients.SubcategoryClient;
import com.machines.machines_front_end.dtos.request.SubcategoryRequestDTO;
import com.machines.machines_front_end.dtos.response.CategoryAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.CategoryResponseDTO;
import com.machines.machines_front_end.dtos.response.SubcategoryAdminResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/subcategories")
public class SubcategoryController {

    private final SubcategoryClient subcategoryClient;
    private final CategoryClient categoryClient;

    @GetMapping("/create")
    @PageRoleGuard(redirectTo = "/", authenticated = true, role = "ADMIN")
    public String showCreateSubcategoryForm(Model model) {
        List<CategoryResponseDTO> categories = categoryClient.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("subcategory", new SubcategoryRequestDTO());
        return "subcategories/create";
    }

    @PostMapping("/create")
    @PageRoleGuard(redirectTo = "/", authenticated = true, role = "ADMIN")
    public String createSubcategory(@ModelAttribute("subcategory") SubcategoryRequestDTO subcategoryDTO, Model model) {
        try {
            subcategoryClient.create(subcategoryDTO);
            return "redirect:/subcategories";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            List<CategoryResponseDTO> categories = categoryClient.getAll();
            model.addAttribute("categories", categories);
            return "subcategories/create";
        }
    }

    @GetMapping
    @PageRoleGuard(redirectTo = "/", authenticated = true, role = "ADMIN")
    public String listSubcategories(Model model) {
        List<SubcategoryAdminResponseDTO> subcategories = subcategoryClient.getAllAdmin();
        List<CategoryAdminResponseDTO> categories = categoryClient.getAllAdmin();

        // Create a map for quick lookup of category names by categoryId
        Map<UUID, String> categoryNameMap = categories.stream()
                .collect(Collectors.toMap(CategoryResponseDTO::getId, CategoryResponseDTO::getName));

        model.addAttribute("subcategories", subcategories);
        model.addAttribute("categoryNameMap", categoryNameMap); // Pass the map to the view

        return "subcategories/list";
    }


    @GetMapping("/update/{id}")
    @PageRoleGuard(redirectTo = "/", authenticated = true, role = "ADMIN")
    public String showUpdateSubcategoryForm(@PathVariable UUID id, Model model) {
        SubcategoryAdminResponseDTO subcategory = subcategoryClient.getByIdAdmin(id);
        List<CategoryResponseDTO> categories = categoryClient.getAll();
        model.addAttribute("subcategory", subcategory);
        model.addAttribute("categories", categories); // Pass categories to the view
        return "subcategories/update"; // Thymeleaf view name
    }

    @PostMapping("/update/{id}")
    @PageRoleGuard(redirectTo = "/", authenticated = true, role = "ADMIN")
    public String updateSubcategory(@PathVariable UUID id, @ModelAttribute("subcategory") SubcategoryRequestDTO subcategoryDTO, Model model) {
        try {
            subcategoryClient.update(id, subcategoryDTO);
            return "redirect:/subcategories";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            List<CategoryResponseDTO> categories = categoryClient.getAll();
            SubcategoryAdminResponseDTO subcategory = subcategoryClient.getByIdAdmin(id);

            model.addAttribute("categories", categories);
            model.addAttribute("subcategory", subcategory);
            return "subcategories/update";
        }
    }

    @PostMapping("/delete/{id}")
    @PageRoleGuard(redirectTo = "/", authenticated = true, role = "ADMIN")
    public String deleteSubcategory(@PathVariable UUID id) {
        subcategoryClient.delete(id);
        return "redirect:/subcategories";
    }
}
