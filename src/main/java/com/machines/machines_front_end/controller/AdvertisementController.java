package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.AdvertisementClient;
import com.machines.machines_front_end.dtos.Advertisement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/advertisements")
public class AdvertisementController {
    private final AdvertisementClient advertisementClient;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("advertisements", advertisementClient.getAll());
        return "advertisements/list";
    }

    @GetMapping("/{id}")
    public String getAdvertisementById(@PathVariable UUID id, Model model) {
        Advertisement advertisement = advertisementClient.getById(id);
        model.addAttribute("advertisement", advertisement);
        return "advertisements/detail";
    }

    @PostMapping("/delete/{id}")
    public String deleteAdvertisement(@PathVariable UUID id) {
        advertisementClient.delete(id);
        return "redirect:/advertisements";
    }
}
