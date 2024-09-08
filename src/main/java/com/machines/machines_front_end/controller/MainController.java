package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.AdvertisementClient;
import com.machines.machines_front_end.clients.OfferClient;
import com.machines.machines_front_end.dtos.Advertisement;
import com.machines.machines_front_end.enums.OfferSaleType;
import com.machines.machines_front_end.enums.OfferSort;
import com.machines.machines_front_end.enums.OfferState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final OfferClient offerClient;
    private final AdvertisementClient advertisementClient;

    @GetMapping("index")
    public String home(Model model) {
        model.addAttribute("advertisements", advertisementClient.getAll());
        model.addAttribute("offers", offerClient.getTopOffers());
        return "index";
    }

    @GetMapping("aboutUs")
    public String aboutUs() {
        return "aboutUs";
    }

    @GetMapping("forAdvertisement")
    public String forAdvertisement() {
        return "forAdvertisement";
    }

    @GetMapping("contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("privacy-policy")
    public String privacyPolicy() {
        return "privacyPolicy";
    }

    @GetMapping("failure")
    public String failure() {
        return "failure";
    }

    @GetMapping("success")
    public String success() {
        return "success";
    }
}