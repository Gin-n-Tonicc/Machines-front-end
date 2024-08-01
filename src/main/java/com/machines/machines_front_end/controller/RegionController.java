package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.CountryClient;
import com.machines.machines_front_end.clients.RegionClient;
import com.machines.machines_front_end.dtos.request.RegionRequestDTO;
import com.machines.machines_front_end.dtos.response.CountryAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.CountryResponseDTO;
import com.machines.machines_front_end.dtos.response.RegionAdminResponseDTO;
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
@RequestMapping("/regions")
public class RegionController {

    private final RegionClient regionClient;

    private final CountryClient countryClient;

    @GetMapping("/create")
    public String showCreateRegionForm(Model model) {
        List<CountryResponseDTO> countries = countryClient.getAll(false);
        model.addAttribute("countries", countries);
        model.addAttribute("region", new RegionRequestDTO());
        return "regions/create";
    }

    @PostMapping("/create")
    public String createRegion(@ModelAttribute("region") RegionRequestDTO regionRequestDTO, Model model) {
        try {
            regionClient.create(regionRequestDTO);
            return "redirect:/regions";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            List<CountryResponseDTO> countries = countryClient.getAll(false);
            model.addAttribute("countries", countries);
            return "regions/create";
        }
    }

    @GetMapping
    public String listRegions(Model model) {
        List<RegionAdminResponseDTO> regions = regionClient.getAllAdmin();
        List<CountryAdminResponseDTO> countries = countryClient.getAllAdmin(false);

        // Create a map for quick lookup of country names by countryId
        Map<UUID, String> countryNameMap = countries.stream()
                .collect(Collectors.toMap(CountryResponseDTO::getId, CountryResponseDTO::getName));

        model.addAttribute("regions", regions);
        model.addAttribute("countryNameMap", countryNameMap); // Pass the map to the view

        return "regions/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateRegionForm(@PathVariable UUID id, Model model) {
        RegionAdminResponseDTO region = regionClient.getByIdAdmin(id);
        List<CountryResponseDTO> countries = countryClient.getAll(false);
        model.addAttribute("region", region);
        model.addAttribute("countries", countries); // Pass countries to the view
        return "regions/update"; // Thymeleaf view name
    }

    @PostMapping("/update/{id}")
    public String updateRegion(@PathVariable UUID id, @ModelAttribute("region") RegionRequestDTO regionDTO, Model model) {
        try {
            regionClient.update(id, regionDTO);
            return "redirect:/regions";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            List<CountryResponseDTO> countries = countryClient.getAll(false);
            RegionAdminResponseDTO regionAdminResponseDTO = regionClient.getByIdAdmin(id);

            model.addAttribute("countries", countries);
            model.addAttribute("region", regionAdminResponseDTO);
            return "regions/update";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteRegion(@PathVariable UUID id) {
        regionClient.delete(id);
        return "redirect:/regions";
    }
}
