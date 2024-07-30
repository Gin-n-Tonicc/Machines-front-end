package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.CityClient;
import com.machines.machines_front_end.clients.FileClient;
import com.machines.machines_front_end.clients.OfferClient;
import com.machines.machines_front_end.clients.SubcategoryClient;
import com.machines.machines_front_end.dtos.File;
import com.machines.machines_front_end.dtos.request.OfferRequestDTO;
import com.machines.machines_front_end.dtos.response.OfferResponseDTO;
import com.machines.machines_front_end.dtos.response.OfferSingleResponseDTO;
import com.machines.machines_front_end.enums.OfferSaleType;
import com.machines.machines_front_end.enums.OfferState;
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

    @PostMapping("/update/{id}")
    public String updateOffer(@PathVariable("id") UUID id,
                              @ModelAttribute("offer") OfferRequestDTO offerDTO,
                              @RequestPart(value = "mainPicture", required = false) MultipartFile mainPicture,
                              @RequestPart(value = "pictures", required = false) MultipartFile[] pictures,
                              Model model) {
        try {
            // Handle main picture upload or retain existing
            if (mainPicture != null && !mainPicture.isEmpty()) {
                File profileFile = fileClient.upload(mainPicture);
                offerDTO.setMainPictureId(profileFile.getId());
            } else {
                offerDTO.setMainPictureId(offerClient.getById(id).getMainPicture().getId());
            }

            // Handle additional pictures upload or retain existing
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
            OfferResponseDTO offerResponseDTO = offerClient.getById(id); // Fetch offer details by ID

            // Convert OfferResponseDTO to OfferRequestDTO
            OfferRequestDTO offerRequestDTO = convertToOfferRequestDTO(offerResponseDTO);

            model.addAttribute("offer", offerRequestDTO);
            model.addAttribute("mainPictureUrl", offerResponseDTO.getMainPicture().getPath()); // Add main picture URL to model
            model.addAttribute("additionalPictureUrls", offerResponseDTO.getPictures().stream()
                    .map(File::getPath)
                    .collect(Collectors.toList())); // Add additional picture URLs to model
            model.addAttribute("cities", cityClient.getAll()); // Populate cities dropdown
            model.addAttribute("subcategories", subcategoryClient.getAll()); // Populate subcategories dropdown
            model.addAttribute("offerStates", OfferState.values()); // Populate offer states dropdown
            model.addAttribute("offerSaleTypes", OfferSaleType.values()); // Populate offer sale types dropdown

            return "offers/update"; // Return the view name for the update form
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            return "redirect:/offers"; // Redirect to the offers list on error
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
}
