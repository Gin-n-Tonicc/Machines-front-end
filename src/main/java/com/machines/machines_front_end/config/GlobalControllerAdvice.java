package com.machines.machines_front_end.config;

import com.machines.machines_front_end.clients.CategoryClient;
import com.machines.machines_front_end.dtos.response.CategoryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CategoryClient categoryClient;

    @ModelAttribute
    public void addAttributes(Model model) {
        List<CategoryResponseDTO> categories = categoryClient.getAll();
        model.addAttribute("categories", categories);
    }
}
