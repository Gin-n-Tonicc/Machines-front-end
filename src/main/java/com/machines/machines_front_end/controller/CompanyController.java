package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.CityClient;
import com.machines.machines_front_end.clients.FileClient;
import com.machines.machines_front_end.clients.CompanyClient;
import com.machines.machines_front_end.dtos.File;
import com.machines.machines_front_end.dtos.request.CompanyRequestDTO;
import com.machines.machines_front_end.dtos.response.CompanyAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.CompanyResponseDTO;
import com.machines.machines_front_end.enums.CompanySort;
import com.machines.machines_front_end.enums.OfferSort;
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
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyClient companyClient;
    private final FileClient fileClient;
    private final CityClient cityClient;

    @GetMapping
    public String getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID cityId,
            @RequestParam(required = false, defaultValue = "def") CompanySort companySort,
            Model model
    ) {
        model.addAttribute("cities", cityClient.getAll());
        model.addAttribute("companies" ,companyClient.getAllCompanies(page, size, search, cityId, companySort));
        return "companies/list";
    }

    @GetMapping("/{id}")
    public String getCompanyById(@PathVariable UUID id, Model model) {
        CompanyResponseDTO company = companyClient.getById(id);
        model.addAttribute("company", company);
        return "companies/detail";
    }

    @GetMapping("/admin/{id}")
    public String getCompanyByIdAdmin(@PathVariable UUID id, Model model) {
        CompanyAdminResponseDTO company = companyClient.getByIdAdmin(id);
        model.addAttribute("company", company);
        return "companies/detail";
    }

    @GetMapping("/admin")
    public String listCompaniesAdmin(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  Model model) {
        Page<CompanyAdminResponseDTO> companies = companyClient.getAllAdmin(page, size);
        model.addAttribute("companies", companies);
        return "companies/listAdmin";
    }

    @GetMapping("/all/user")
    public String getAllForLoggedUser(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      Model model) {
        Page<CompanyAdminResponseDTO> companies = companyClient.getAllForLoggedUser(page, size);
        model.addAttribute("companies", companies);
        return "companies/myCompanies";
    }

    @GetMapping("/create")
    public String showCreateCompanyForm(Model model) {
        model.addAttribute("cities", cityClient.getAll());
        model.addAttribute("company", new CompanyRequestDTO());
        return "companies/create";
    }

    @PostMapping("/create")
    public String createCompany(@ModelAttribute("company") CompanyRequestDTO companyDTO,
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

            companyDTO.setMainPictureId(profileFile.getId());
            companyDTO.setPictureIds(additionalPicturesIds);

            companyClient.create(companyDTO);
            return "redirect:/companies";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            model.addAttribute("cities", cityClient.getAll());
            model.addAttribute("company", new CompanyRequestDTO());
            return "companies/create";
        }
    }

    @PostMapping("/update/{id}")
    public String updateCompany(@PathVariable("id") UUID id,
                              @ModelAttribute("company") CompanyRequestDTO companyDTO,
                              @RequestPart(value = "mainPicture", required = false) MultipartFile mainPicture,
                              @RequestPart(value = "pictures", required = false) MultipartFile[] pictures,
                              Model model) {
        try {
            if (mainPicture != null && !mainPicture.isEmpty()) {
                File profileFile = fileClient.upload(mainPicture);
                companyDTO.setMainPictureId(profileFile.getId());
            } else {
                companyDTO.setMainPictureId(companyClient.getById(id).getMainPicture().getId());
            }

            Set<UUID> additionalPicturesIds = new HashSet<>();
            if (pictures != null && pictures.length > 1) {
                for (MultipartFile multipartFile : pictures) {
                    File file = fileClient.upload(multipartFile);
                    additionalPicturesIds.add(file.getId());
                }
            } else {
                CompanyResponseDTO existingCompany = companyClient.getById(id);
                additionalPicturesIds = existingCompany.getPictures().stream()
                        .map(File::getId)
                        .collect(Collectors.toSet());
            }

            companyDTO.setPictureIds(additionalPicturesIds);
            companyClient.update(id, companyDTO);

            return "redirect:/companies/" + id;
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            model.addAttribute("cities", cityClient.getAll());
            model.addAttribute("company", companyDTO);

            CompanyResponseDTO companyResponseDTO = companyClient.getById(id);
            model.addAttribute("mainPictureUrl", companyResponseDTO.getMainPicture().getPath()); // Add main picture URL to model
            model.addAttribute("additionalPictureUrls", companyResponseDTO.getPictures().stream()
                    .map(File::getPath)
                    .collect(Collectors.toList()));
            return "companies/update";
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") UUID id, Model model) {
        try {
            CompanyResponseDTO companyResponseDTO = companyClient.getById(id);

            CompanyRequestDTO companyRequestDTO = convertToCompanyRequestDTO(companyResponseDTO);

            model.addAttribute("company", companyRequestDTO);
            model.addAttribute("mainPictureUrl", companyResponseDTO.getMainPicture().getPath());
            model.addAttribute("additionalPictureUrls", companyResponseDTO.getPictures().stream()
                    .map(File::getPath)
                    .collect(Collectors.toList()));
            model.addAttribute("cities", cityClient.getAll());

            return "companies/update";
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);

            return "redirect:/companies";
        }
    }


    public CompanyRequestDTO convertToCompanyRequestDTO(CompanyResponseDTO companyResponseDTO) {
        CompanyRequestDTO companyRequestDTO = new CompanyRequestDTO();

        // Copy properties from CompanyResponseDTO to CompanyRequestDTO
        companyRequestDTO.setId(companyResponseDTO.getId());
        companyRequestDTO.setName(companyResponseDTO.getName());
        companyRequestDTO.setEik(companyResponseDTO.getEik());
        companyRequestDTO.setFax(companyResponseDTO.getFax());
        companyRequestDTO.setAddress(companyResponseDTO.getAddress());
        companyRequestDTO.setPhoneNumber(companyResponseDTO.getPhoneNumber());
        companyRequestDTO.setDescription(companyResponseDTO.getDescription());
        companyRequestDTO.setWebsite(companyResponseDTO.getWebsite());
        companyRequestDTO.setMainPictureId(companyResponseDTO.getMainPicture().getId());
        Set<UUID> pictureIds = companyResponseDTO.getPictures().stream().map(File::getId).collect(Collectors.toSet());
        companyRequestDTO.setPictureIds(pictureIds);
        companyRequestDTO.setOwnerId(companyResponseDTO.getOwner().getId());
        companyRequestDTO.setCityId(companyResponseDTO.getCity().getId());

        return companyRequestDTO;
    }

    @PostMapping("/delete/{id}")
    public String deleteCompany(@PathVariable UUID id) {
        companyClient.delete(id);
        return "redirect:/companies/all/user";
    }

    @PostMapping("/delete/admin/{id}")
    public String deleteCompanyAdmin(@PathVariable UUID id) {
        companyClient.delete(id);
        return "redirect:/companies/admin";
    }
}
