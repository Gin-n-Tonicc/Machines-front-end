package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.CityClient;
import com.machines.machines_front_end.clients.FileClient;
import com.machines.machines_front_end.clients.OfferClient;
import com.machines.machines_front_end.clients.SubcategoryClient;
import com.machines.machines_front_end.dtos.File;
import com.machines.machines_front_end.dtos.request.OfferRequestDTO;
import com.machines.machines_front_end.dtos.response.OfferAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.OfferResponseDTO;
import com.machines.machines_front_end.dtos.response.OfferSingleAdminResponseDTO;
import com.machines.machines_front_end.enums.OfferSaleType;
import com.machines.machines_front_end.enums.OfferSort;
import com.machines.machines_front_end.enums.OfferState;
import com.machines.machines_front_end.enums.OfferType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public String getAll(
            @RequestParam(defaultValue = "1") int page,
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
            Model model
    ) {
        model.addAttribute("subcategories", subcategoryClient.getAll());
        model.addAttribute("cities", cityClient.getAll());
        model.addAttribute("offers", offerClient.getAllOffers(page, size, search, subcategoryId, cityId, offerState, offerSaleType, minPrice, maxPrice, bulgarian, offerSort));
        return "offers/list";
    }

    @GetMapping("/{id}")
    public String getOfferById(@PathVariable UUID id, Model model) {
        OfferResponseDTO offer = offerClient.getById(id);

        if (offer.getOfferType() == OfferType.TOP || offer.getOfferType() == OfferType.VIP) {
            offer.setSimilarOffers(offerClient.getByOwner(1, 10, offer.getOwner().getId()).toList());
            model.addAttribute("vipText", "Разгледайте други обяви на този потребител");
        } else {
            model.addAttribute("vipText", "Разгледайте подобни обяви");
        }

        model.addAttribute("offer", offer);
        return "offers/detail";
    }

    @GetMapping("/admin/{id}")
    public String getOfferByIdAdmin(@PathVariable UUID id, Model model) {
        OfferSingleAdminResponseDTO offer = offerClient.getByIdAdmin(id);
        model.addAttribute("offer", offer);
        return "offers/detail";
    }

    @GetMapping("/admin")
    public String listOffersAdmin(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  Model model) {
        Page<OfferAdminResponseDTO> offers = offerClient.getAllAdmin(page, size);
        model.addAttribute("offers", offers);
        return "offers/listAdmin";
    }

    @GetMapping("/all/user")
    public String getAllForLoggedUser(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      Model model) {
        Page<OfferAdminResponseDTO> offers = offerClient.getAllForLoggedUser(page, size);
        model.addAttribute("offers", offers);
        return "offers/myOffers";
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

    @PostMapping("/update/{id}")
    public String updateOffer(@PathVariable("id") UUID id,
                              @ModelAttribute("offer") OfferRequestDTO offerDTO,
                              @RequestPart(value = "mainPicture", required = false) MultipartFile mainPicture,
                              @RequestPart(value = "pictures", required = false) MultipartFile[] pictures,
                              Model model) {
        try {
            if (mainPicture != null && !mainPicture.isEmpty()) {
                File profileFile = fileClient.upload(mainPicture);
                offerDTO.setMainPictureId(profileFile.getId());
            } else {
                offerDTO.setMainPictureId(offerClient.getById(id).getMainPicture().getId());
            }

            Set<UUID> additionalPicturesIds = new HashSet<>();
            if (pictures != null && pictures.length > 1) {
                for (MultipartFile multipartFile : pictures) {
                    File file = fileClient.upload(multipartFile);
                    additionalPicturesIds.add(file.getId());
                }
            } else {
                OfferResponseDTO existingOffer = offerClient.getById(id);
                additionalPicturesIds = existingOffer.getPictures().stream()
                        .map(File::getId)
                        .collect(Collectors.toSet());
            }

            offerDTO.setPictureIds(additionalPicturesIds);
            offerClient.update(id, offerDTO);

            return "redirect:/offers/" + id;
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            model.addAttribute("cities", cityClient.getAll());
            model.addAttribute("subcategories", subcategoryClient.getAll());
            model.addAttribute("offerStates", OfferState.values());
            model.addAttribute("offerSaleTypes", OfferSaleType.values());
            model.addAttribute("offer", offerDTO);

            OfferResponseDTO offerResponseDTO = offerClient.getById(id);
            model.addAttribute("mainPictureUrl", offerResponseDTO.getMainPicture().getPath()); // Add main picture URL to model
            model.addAttribute("additionalPictureUrls", offerResponseDTO.getPictures().stream()
                    .map(File::getPath)
                    .collect(Collectors.toList()));
            return "offers/update";
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") UUID id, Model model) {
        try {
            OfferResponseDTO offerResponseDTO = offerClient.getById(id);

            OfferRequestDTO offerRequestDTO = convertToOfferRequestDTO(offerResponseDTO);

            model.addAttribute("offer", offerRequestDTO);
            model.addAttribute("mainPictureUrl", offerResponseDTO.getMainPicture().getPath());
            model.addAttribute("additionalPictureUrls", offerResponseDTO.getPictures().stream()
                    .map(File::getPath)
                    .collect(Collectors.toList()));
            model.addAttribute("cities", cityClient.getAll());
            model.addAttribute("subcategories", subcategoryClient.getAll());
            model.addAttribute("offerStates", OfferState.values());
            model.addAttribute("offerSaleTypes", OfferSaleType.values());

            return "offers/update";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);

            return "redirect:/offers";
        }
    }


    public OfferRequestDTO convertToOfferRequestDTO(OfferResponseDTO offerResponseDTO) {
        OfferRequestDTO offerRequestDTO = new OfferRequestDTO();

        // Copy properties from OfferResponseDTO to OfferRequestDTO
        offerRequestDTO.setId(offerResponseDTO.getId());
        offerRequestDTO.setTitle(offerResponseDTO.getTitle());
        offerRequestDTO.setPhoneNumber(offerResponseDTO.getPhoneNumber());
        offerRequestDTO.setDescription(offerResponseDTO.getDescription());
        offerRequestDTO.setWebsiteURL(offerResponseDTO.getWebsiteURL());
        offerRequestDTO.setPrice(offerResponseDTO.getPrice());
        offerRequestDTO.setBulgarian(offerResponseDTO.isBulgarian());
        offerRequestDTO.setAutoUpdate(offerResponseDTO.isAutoUpdate());
        offerRequestDTO.setOfferState(offerResponseDTO.getOfferState());
        offerRequestDTO.setOfferSaleType(offerResponseDTO.getOfferSaleType());
        offerRequestDTO.setOfferType(offerResponseDTO.getOfferType());
        offerRequestDTO.setMainPictureId(offerResponseDTO.getMainPicture().getId());
        Set<UUID> pictureIds = offerResponseDTO.getPictures().stream().map(File::getId).collect(Collectors.toSet());
        offerRequestDTO.setPictureIds(pictureIds);
        offerRequestDTO.setOwnerId(offerResponseDTO.getOwner().getId());
        offerRequestDTO.setCityId(offerResponseDTO.getCity().getId());
        offerRequestDTO.setSubcategoryId(offerResponseDTO.getSubcategory().getId());
        // Add additional fields
        offerRequestDTO.setManufactureYear(offerResponseDTO.getManufactureYear());
        offerRequestDTO.setModel(offerResponseDTO.getModel());
        offerRequestDTO.setPowerSupplyVoltage(offerResponseDTO.getPowerSupplyVoltage());
        offerRequestDTO.setFuelType(offerResponseDTO.getFuelType());
        offerRequestDTO.setHorsePower(offerResponseDTO.getHorsePower());
        offerRequestDTO.setConsumption(offerResponseDTO.getConsumption());
        offerRequestDTO.setOutputPower(offerResponseDTO.getOutputPower());
        offerRequestDTO.setProductivity(offerResponseDTO.getProductivity());
        offerRequestDTO.setCapacity(offerResponseDTO.getCapacity());
        offerRequestDTO.setMinRevolutions(offerResponseDTO.getMinRevolutions());
        offerRequestDTO.setNominalRevolutions(offerResponseDTO.getNominalRevolutions());
        offerRequestDTO.setMaxRevolutions(offerResponseDTO.getMaxRevolutions());
        offerRequestDTO.setDimensions(offerResponseDTO.getDimensions());
        offerRequestDTO.setOwnWeight(offerResponseDTO.getOwnWeight());
        offerRequestDTO.setWorkMoves(offerResponseDTO.getWorkMoves());

        return offerRequestDTO;
    }

    @PostMapping("/delete/{id}")
    public String deleteOffer(@PathVariable UUID id) {
        offerClient.delete(id);
        return "redirect:/offers/all/user";
    }

    @PostMapping("/delete/admin/{id}")
    public String deleteOfferAdmin(@PathVariable UUID id) {
        offerClient.delete(id);
        return "redirect:/offers/admin";
    }

    @GetMapping("/promote/{id}/form")
    public String showPromotionForm(@PathVariable UUID id, Model model) {
        model.addAttribute("id", id);
        return "/offers/offer-types";
    }

    @GetMapping("/promote/{id}")
    public String promoteOffer(
            @PathVariable UUID id,
            @RequestParam(name = "customerName") String customerName,
            @RequestParam(name = "offerType") OfferType offerType,
            Model model
    ) {
        String response = offerClient.promoteOffer(id, customerName, offerType);

        model.addAttribute("response", response);
        return "redirect:" + response;
    }
}
