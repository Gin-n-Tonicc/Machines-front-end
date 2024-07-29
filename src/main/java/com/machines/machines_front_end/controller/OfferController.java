package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.OfferClient;
import com.machines.machines_front_end.dtos.response.OfferResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/offers")
public class OfferController {
    private final OfferClient offerClient;

    @GetMapping
    public String listOffers(Model model) {
        List<OfferResponseDTO> offers = offerClient.getAll();
        model.addAttribute("offers", offers);
        return "offers/list";
    }

    @GetMapping("/{id}")
    public String getOfferById(@PathVariable UUID id, Model model) {
        OfferResponseDTO offer = offerClient.getById(id);
        model.addAttribute("offer", offer);
        return "offers/detail";
    }
}
