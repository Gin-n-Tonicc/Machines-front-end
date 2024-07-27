package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.dtos.request.RegionRequestDTO;
import com.machines.machines_front_end.dtos.response.RegionAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.RegionResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "machines-api-region", url = "${backend.base-url}/regions")
public interface RegionClient {
    @GetMapping("/all")
    List<RegionResponseDTO> getAll();

    @GetMapping("/all/admin")
    List<RegionAdminResponseDTO> getAllAdmin();

    @GetMapping("/{id}")
    RegionResponseDTO getById(@PathVariable UUID id);

    @GetMapping("/{id}/admin")
    RegionAdminResponseDTO getByIdAdmin(@PathVariable UUID id);

    @PostMapping("/create")
    RegionResponseDTO create(@Valid @RequestBody RegionRequestDTO regionRequestDTO);

    @PutMapping("/{id}")
    RegionResponseDTO update(@PathVariable UUID id, @Valid @RequestBody RegionRequestDTO regionRequestDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id);
}