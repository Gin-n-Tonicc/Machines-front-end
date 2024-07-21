package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.dtos.request.SubcategoryRequestDTO;
import com.machines.machines_front_end.dtos.response.SubcategoryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "machines-api-subcategory", url = "${backend.base-url}/subcategories")
public interface SubcategoryClient {
    @GetMapping("/all")
    List<SubcategoryResponseDTO> getAll();

    @GetMapping("/{id}")
    SubcategoryResponseDTO getById(@PathVariable UUID id);

    @PostMapping("/create")
    SubcategoryResponseDTO create(@Valid @RequestBody SubcategoryRequestDTO subcategoryRequestDTO);

    @PutMapping("/{id}")
    SubcategoryResponseDTO update(@PathVariable UUID id, @Valid @RequestBody SubcategoryRequestDTO subcategoryRequestDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id);
}
