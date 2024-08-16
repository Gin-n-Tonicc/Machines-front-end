package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.AdvertisementClient;
import com.machines.machines_front_end.clients.FileClient;
import com.machines.machines_front_end.dtos.Advertisement;
import com.machines.machines_front_end.dtos.File;
import com.machines.machines_front_end.dtos.request.AdvertisementRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/advertisements")
public class AdvertisementController {
    private final AdvertisementClient advertisementClient;
    private final FileClient fileClient;

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

    @GetMapping("/create")
    public String showCreateAdvertisementForm(Model model) {
        model.addAttribute("advertisement", new Advertisement());
        return "advertisements/create";
    }
    
    @PostMapping("/create")
    public String createAdvertisement(@ModelAttribute("advertisement") AdvertisementRequest advertisementDTO, Model model,
                                      @RequestPart("pictures") MultipartFile[] pictures) {
        try {
            Set<File> additionalPictures = new HashSet<>();
            if (pictures != null) {
                for (MultipartFile multipartFile : pictures) {
                    File file = fileClient.upload(multipartFile);
                    additionalPictures.add(file);
                }
            }

            Advertisement advertisement = new Advertisement(
                    advertisementDTO.getTitle(),
                    additionalPictures,
                    advertisementDTO.getTargetUrl(),
                    advertisementDTO.getPosition(),
                    advertisementDTO.getStartDate(),
                    advertisementDTO.getEndDate(),
                    advertisementDTO.isActive());

            advertisementClient.create(advertisement);
            return "redirect:/advertisements";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            return "advertisements/create";
        }
    }
    
    @PostMapping("/delete/{id}")
    public String deleteAdvertisement(@PathVariable UUID id) {
        advertisementClient.delete(id);
        return "redirect:/advertisements";
    }
}
