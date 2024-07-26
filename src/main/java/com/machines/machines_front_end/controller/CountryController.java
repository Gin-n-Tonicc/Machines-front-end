package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.CountryClient;
import com.machines.machines_front_end.dtos.request.CountryRequestDTO;
import com.machines.machines_front_end.dtos.response.CountryAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.CountryResponseDTO;
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
@RequestMapping("/countries")
public class CountryController {

    private final CountryClient countryClient;

    @GetMapping("/create")
    public String showCreateCountryForm(Model model) {
        model.addAttribute("country", new CountryRequestDTO());
        return "countries/create";
    }

    @PostMapping("/create")
    public String createCountry(@ModelAttribute("country") CountryRequestDTO countryDTO, Model model) {
        try {
            countryClient.create(countryDTO);
            return "redirect:/countries";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            return "countries/create";
        }
    }

    @GetMapping
    public String listCountries(@RequestParam(defaultValue = "false") boolean includeRegions, Model model) {
        List<CountryAdminResponseDTO> countries = countryClient.getAllAdmin(includeRegions);
        model.addAttribute("countries", countries);
        return "countries/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateCountryForm(@PathVariable UUID id, Model model) {
        CountryResponseDTO country = countryClient.getByIdAdmin(id);
        model.addAttribute("country", country);
        return "countries/update";
    }

    @PostMapping("/update/{id}")
    public String updateCountry(@PathVariable UUID id, @ModelAttribute("country") CountryRequestDTO countryDTO, Model model) {
        try {
            countryClient.update(id, countryDTO);
            return "redirect:/countries";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            model.addAttribute("country", countryDTO);
            return "countries/update";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCountry(@PathVariable UUID id) {
        countryClient.delete(id);
        return "redirect:/countries";
    }
}
