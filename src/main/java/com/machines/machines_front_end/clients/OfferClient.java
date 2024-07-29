package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.dtos.request.OfferRequestDTO;
import com.machines.machines_front_end.dtos.response.OfferAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.OfferResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "machines-api-offer", url = "${backend.base-url}/offers")
public interface OfferClient {
    @GetMapping("/all")
    List<OfferResponseDTO> getAll();

//    @GetMapping("/all/admin")
//    List<OfferAdminResponseDTO> getAllAdmin();
//
    @GetMapping("/{id}")
    OfferResponseDTO getById(@PathVariable UUID id);

//    @PostMapping("/create")
//    OfferResponseDTO create(@Valid @RequestBody OfferRequestDTO offerRequestDTO, HttpServletRequest httpServletRequest);
//
//    @PutMapping("/{id}")
//    OfferResponseDTO update(@PathVariable UUID id, @Valid @RequestBody OfferRequestDTO offerRequestDTO, HttpServletRequest httpServletRequest);
//
//    @DeleteMapping("/{id}")
//    void delete(@PathVariable UUID id, HttpServletRequest httpServletRequest);
}
