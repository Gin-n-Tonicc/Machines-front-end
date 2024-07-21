package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.CategoryClient;
import com.machines.machines_front_end.clients.SubcategoryClient;
import com.machines.machines_front_end.dtos.request.SubcategoryRequestDTO;
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
@RequestMapping("/subcategories")
public class SubcategoryController {

    private final CategoryClient categoryClient;

    private final SubcategoryClient subcategoryClient;

    @GetMapping("/create")
    public String showCreateSubcategoryForm(Model model) {
        model.addAttribute("subcategory", new SubcategoryRequestDTO());
        model.addAttribute("categories", categoryClient.getAll());
        return "subcategories/create";
    }

    @PostMapping("/create")
    public String createSubcategory(@ModelAttribute("subcategory") SubcategoryRequestDTO subcategoryDTO, Model model) {
        try {
            subcategoryClient.create(subcategoryDTO);
            return "redirect:/index";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            model.addAttribute("categories", categoryClient.getAll());
            return "subcategories/create";
        }
    }
}
