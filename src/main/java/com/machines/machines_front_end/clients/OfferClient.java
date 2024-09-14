package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.config.FeignClientConfiguration;
import com.machines.machines_front_end.dtos.request.OfferRequestDTO;
import com.machines.machines_front_end.dtos.response.OfferAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.OfferResponseDTO;
import com.machines.machines_front_end.dtos.response.OfferSingleAdminResponseDTO;
import com.machines.machines_front_end.enums.OfferSaleType;
import com.machines.machines_front_end.enums.OfferSort;
import com.machines.machines_front_end.enums.OfferState;
import com.machines.machines_front_end.enums.OfferType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "machines-api-offer", url = "${backend.base-url}/offers", configuration = FeignClientConfiguration.class)
public interface OfferClient {
    @GetMapping("/all")
    Page<OfferResponseDTO> getAllOffers(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID subcategoryId,
            @RequestParam(required = false) UUID cityId,
            @RequestParam(required = false) OfferState offerState,
            @RequestParam(required = false) OfferSaleType offerSaleType,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean bulgarian,
            @RequestParam(required = false, defaultValue = "def") OfferSort offerSort
    );

    @GetMapping("/top")
    List<OfferResponseDTO> getTopOffers();

    @GetMapping("/all/admin")
    Page<OfferAdminResponseDTO> getAllAdmin(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID subcategoryId,
            @RequestParam(required = false) UUID cityId,
            @RequestParam(required = false) OfferState offerState,
            @RequestParam(required = false) OfferSaleType offerSaleType,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean bulgarian,
            @RequestParam(required = false, defaultValue = "def") OfferSort offerSort
    );

    @GetMapping("/all/user")
    Page<OfferAdminResponseDTO> getAllForLoggedUser(@RequestParam int page, @RequestParam int size);

    @GetMapping("/byOwner")
    Page<OfferResponseDTO> getByOwner(@RequestParam int page, @RequestParam int size,
                                                             @RequestParam UUID userId);

    @GetMapping("/{id}")
    OfferResponseDTO getById(@PathVariable UUID id);

    @GetMapping("/{id}/admin")
    OfferSingleAdminResponseDTO getByIdAdmin(@PathVariable UUID id);

    @PostMapping("/create")
    OfferResponseDTO create(@RequestBody OfferRequestDTO offerRequestDTO);

    @PutMapping("/{id}")
    OfferResponseDTO update(@PathVariable UUID id, @Valid @RequestBody OfferRequestDTO offerRequestDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id);

    @GetMapping("/promote/{id}")
    String promoteOffer(
            @PathVariable("id") UUID id,
            @RequestParam("customerName") String customerName,
            @RequestParam("offerType") OfferType offerType);
}
