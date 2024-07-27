package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.dtos.request.CityRequestDTO;
import com.machines.machines_front_end.dtos.response.CityAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.CityResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "machines-api-city", url = "${backend.base-url}/cities")
public interface CityClient {
    @GetMapping("/all")
    List<CityResponseDTO> getAll();

    @GetMapping("/all/admin")
    List<CityAdminResponseDTO> getAllAdmin();

    @GetMapping("/{id}")
    CityResponseDTO getById(@PathVariable UUID id);

    @GetMapping("/{id}/admin")
    CityAdminResponseDTO getByIdAdmin(@PathVariable UUID id);

    @PostMapping("/create")
    CityResponseDTO create(@Valid @RequestBody CityRequestDTO cityRequestDTO);

    @PutMapping("/{id}")
    CityResponseDTO update(@PathVariable UUID id, @Valid @RequestBody CityRequestDTO cityRequestDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id);
}