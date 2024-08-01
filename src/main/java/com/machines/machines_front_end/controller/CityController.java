package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.CityClient;
import com.machines.machines_front_end.clients.RegionClient;
import com.machines.machines_front_end.dtos.request.CityRequestDTO;
import com.machines.machines_front_end.dtos.response.CityAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.RegionAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.RegionResponseDTO;
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
@RequestMapping("/cities")
public class CityController {

    private final CityClient cityClient;

    private final RegionClient regionClient;

    @GetMapping("/create")
    public String showCreateCityForm(Model model) {
        List<RegionResponseDTO> regions = regionClient.getAll();
        model.addAttribute("regions", regions);
        model.addAttribute("city", new CityRequestDTO());
        return "cities/create";
    }

    @PostMapping("/create")
    public String createCity(@ModelAttribute("city") CityRequestDTO cityRequestDTO, Model model) {
        try {
            cityClient.create(cityRequestDTO);
            return "redirect:/cities";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            List<RegionResponseDTO> regions = regionClient.getAll();
            model.addAttribute("regions", regions);
            return "cities/create";
        }
    }

    @GetMapping
    public String listCities(Model model) {
        List<CityAdminResponseDTO> cities = cityClient.getAllAdmin();
        List<RegionAdminResponseDTO> regions = regionClient.getAllAdmin();

        // Create a map for quick lookup of region names by regionId
        Map<UUID, String> regionNameMap = regions.stream()
                .collect(Collectors.toMap(RegionResponseDTO::getId, RegionResponseDTO::getName));

        model.addAttribute("cities", cities);
        model.addAttribute("regionNameMap", regionNameMap); // Pass the map to the view

        return "cities/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateCityForm(@PathVariable UUID id, Model model) {
        CityAdminResponseDTO city = cityClient.getByIdAdmin(id);
        List<RegionResponseDTO> regions = regionClient.getAll();
        model.addAttribute("city", city);
        model.addAttribute("regions", regions); // Pass regions to the view
        return "cities/update"; // Thymeleaf view name
    }

    @PostMapping("/update/{id}")
    public String updateCity(@PathVariable UUID id, @ModelAttribute("city") CityRequestDTO cityDTO, Model model) {
        try {
            cityClient.update(id, cityDTO);
            return "redirect:/cities";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            List<RegionResponseDTO> regions = regionClient.getAll();
            CityAdminResponseDTO cityAdminResponseDTO = cityClient.getByIdAdmin(id);

            model.addAttribute("regions", regions);
            model.addAttribute("city", cityAdminResponseDTO);
            return "cities/update";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCity(@PathVariable UUID id) {
        cityClient.delete(id);
        return "redirect:/cities";
    }
}