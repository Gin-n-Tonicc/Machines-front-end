package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.dtos.request.CategoryRequestDTO;
import com.machines.machines_front_end.dtos.response.CategoryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "machines-api-category", url = "${backend.base-url}/categories")
public interface CategoryClient {

    @GetMapping("/all")
    List<CategoryResponseDTO> getAll();

    @GetMapping("/{id}")
    CategoryResponseDTO getById(@PathVariable UUID id);

    @PostMapping("/create")
    CategoryResponseDTO create(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO);

    @PutMapping("/{id}")
    CategoryResponseDTO update(@PathVariable UUID id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id);
}