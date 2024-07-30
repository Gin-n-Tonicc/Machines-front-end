package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.CityClient;
import com.machines.machines_front_end.clients.FileClient;
import com.machines.machines_front_end.clients.OfferClient;
import com.machines.machines_front_end.clients.SubcategoryClient;
import com.machines.machines_front_end.dtos.File;
import com.machines.machines_front_end.dtos.request.OfferRequestDTO;
import com.machines.machines_front_end.dtos.response.OfferResponseDTO;
import com.machines.machines_front_end.enums.OfferSaleType;
import com.machines.machines_front_end.enums.OfferState;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/offers")
public class OfferController {
    private final OfferClient offerClient;
    private final FileClient fileClient;
    private final SubcategoryClient subcategoryClient;
    private final CityClient cityClient;

    @GetMapping
    public String listOffers(Model model) {
        Page<OfferResponseDTO> offers = offerClient.getAll(1, 10);
        model.addAttribute("offers", offers);
        return "offers/list";
    }

    @GetMapping("/{id}")
    public String getOfferById(@PathVariable UUID id, Model model) {
        OfferResponseDTO offer = offerClient.getById(id);
        model.addAttribute("offer", offer);
        return "offers/detail";
    }

    @GetMapping("/create")
    public String showCreateOfferForm(Model model) {
        model.addAttribute("cities", cityClient.getAll());
        model.addAttribute("subcategories", subcategoryClient.getAll());
        model.addAttribute("offerStates", OfferState.values());
        model.addAttribute("offerSaleTypes", OfferSaleType.values());
        model.addAttribute("offer", new OfferRequestDTO());
        return "offers/create";
    }

    @PostMapping("/create")
    public String createOffer(@ModelAttribute("offer") OfferRequestDTO offerDTO,
                              @RequestPart("mainPicture") MultipartFile mainPicture,
                              @RequestPart("pictures") MultipartFile[] pictures,
                              Model model) {
        try {
            File profileFile = fileClient.upload(mainPicture);
            Set<UUID> additionalPicturesIds = new HashSet<>();

            if (pictures != null) {
                for (MultipartFile multipartFile : pictures) {
                    File file = fileClient.upload(multipartFile);
                    additionalPicturesIds.add(file.getId());
                }
            }

            offerDTO.setMainPictureId(profileFile.getId());
            offerDTO.setPictureIds(additionalPicturesIds);

            offerClient.create(offerDTO);
            return "redirect:/offers";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            model.addAttribute("cities", cityClient.getAll());
            model.addAttribute("subcategories", subcategoryClient.getAll());
            model.addAttribute("offerStates", OfferState.values());
            model.addAttribute("offerSaleTypes", OfferSaleType.values());
            model.addAttribute("offer", new OfferRequestDTO());
            return "offers/create";
        }
    }
}
