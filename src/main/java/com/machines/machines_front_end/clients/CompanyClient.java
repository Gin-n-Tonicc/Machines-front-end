package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.config.FeignClientConfiguration;
import com.machines.machines_front_end.dtos.request.CompanyRequestDTO;
import com.machines.machines_front_end.dtos.response.CompanyAdminResponseDTO;
import com.machines.machines_front_end.dtos.response.CompanyResponseDTO;
import com.machines.machines_front_end.enums.CompanySort;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@FeignClient(name = "machines-api-company", url = "${backend.base-url}/companies", configuration = FeignClientConfiguration.class)
public interface CompanyClient {
    @GetMapping("/all")
    Page<CompanyResponseDTO> getAllCompanies(@RequestParam int page,
    @RequestParam int size,
    @RequestParam(required = false) String search,
    @RequestParam(required = false) UUID cityId,
    @RequestParam(required = false, defaultValue = "def") CompanySort companySort
    );

    @GetMapping("/{id}/admin")
    CompanyAdminResponseDTO getByIdAdmin(@PathVariable UUID id);

    @GetMapping("/all/admin")
    Page<CompanyAdminResponseDTO> getAllAdmin(@RequestParam int page, @RequestParam int size);

    @GetMapping("/all/user")
    Page<CompanyAdminResponseDTO> getAllForLoggedUser(@RequestParam int page, @RequestParam int size);

    @GetMapping("/{id}")
    CompanyResponseDTO getById(@PathVariable UUID id);

    @PostMapping("/create")
    CompanyResponseDTO create(@RequestBody CompanyRequestDTO companyRequestDTO);

    @PutMapping("/{id}")
    CompanyResponseDTO update(@PathVariable UUID id, @Valid @RequestBody CompanyRequestDTO companyRequestDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id);
}
