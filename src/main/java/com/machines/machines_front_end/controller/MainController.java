package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.OfferClient;
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

    @GetMapping("index")
    public String home(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int size,
                       @RequestParam(required = false) String search,
                       @RequestParam(required = false) UUID subcategoryId,
                       @RequestParam(required = false) UUID cityId,
                       @RequestParam(required = false) OfferState offerState,
                       @RequestParam(required = false) OfferSaleType offerSaleType,
                       @RequestParam(required = false) Double minPrice,
                       @RequestParam(required = false) Double maxPrice,
                       @RequestParam(required = false) Boolean bulgarian,
                       @RequestParam(required = false, defaultValue = "def") OfferSort offerSort,
                       Model model) {
        model.addAttribute("offers", offerClient.getAllOffers(page, size, search, subcategoryId, cityId, offerState, offerSaleType, minPrice, maxPrice, bulgarian, offerSort));
        return "index";
    }

    @GetMapping("aboutUs")
    public String aboutUs() {
        return "aboutUs";
    }

    @GetMapping("contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("privacy-policy")
    public String privacyPolicy() {
        return "privacyPolicy";
    }
}
