package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.config.FeignClientConfiguration;
import com.machines.machines_front_end.dtos.request.CategoryRequestDTO;
import com.machines.machines_front_end.dtos.response.CategoryAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.CategoryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "machines-api-category", url = "${backend.base-url}/categories", configuration = FeignClientConfiguration.class)
public interface CategoryClient {

    @GetMapping("/all")
    List<CategoryResponseDTO> getAll();

    @GetMapping("/all/withOffers")
    List<CategoryResponseDTO> getAllCategoriesWithOffers();

    @GetMapping("/{id}")
    CategoryResponseDTO getById(@PathVariable UUID id);

    @GetMapping("/{id}/admin")
    CategoryAdminResponseDTO getByIdAdmin(@PathVariable UUID id);

    @PostMapping("/create")
    CategoryResponseDTO create(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO);

    @PutMapping("/{id}")
    CategoryResponseDTO update(@PathVariable UUID id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id);

    @GetMapping("/all/admin")
    List<CategoryAdminResponseDTO> getAllAdmin();
}
