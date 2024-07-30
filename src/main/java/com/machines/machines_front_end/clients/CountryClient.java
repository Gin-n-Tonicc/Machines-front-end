package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.config.FeignClientConfiguration;
import com.machines.machines_front_end.dtos.request.CountryRequestDTO;
import com.machines.machines_front_end.dtos.response.CountryAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.CountryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "machines-api-country", url = "${backend.base-url}/countries", configuration = FeignClientConfiguration.class)
public interface CountryClient {
    @GetMapping("/all")
    List<CountryResponseDTO> getAll(@RequestParam(defaultValue = "false") boolean includeRegions);

    @GetMapping("/all/admin")
    List<CountryAdminResponseDTO> getAllAdmin(@RequestParam(defaultValue = "false") boolean includeRegions);

    @GetMapping("/{id}/admin")
    CountryAdminResponseDTO getByIdAdmin(@PathVariable UUID id);

    @GetMapping("/{id}")
    CountryResponseDTO getById(@PathVariable UUID id);

    @PostMapping("/create")
    CountryResponseDTO create(@Valid @RequestBody CountryRequestDTO countryRequestDTO);

    @PutMapping("/{id}")
    CountryResponseDTO update(@PathVariable UUID id, @Valid @RequestBody CountryRequestDTO countryRequestDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id);
}
