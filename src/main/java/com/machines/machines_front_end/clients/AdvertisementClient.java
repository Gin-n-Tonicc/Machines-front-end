package com.machines.machines_front_end.clients;


import com.machines.machines_front_end.config.FeignClientConfiguration;
import com.machines.machines_front_end.dtos.Advertisement;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "machines-api-advertisement", url = "${backend.base-url}/advertisements", configuration = FeignClientConfiguration.class)
public interface AdvertisementClient {
    @GetMapping("/all")
    List<Advertisement> getAll();

    @GetMapping("/all/admin")
    List<Advertisement> getAllAdmin();

    @GetMapping("/{id}")
    Advertisement getById(@PathVariable UUID id);

    @GetMapping("/{id}")
    Advertisement getByIdAdmin(@PathVariable UUID id);

    @PostMapping("/create")
    Advertisement create(@Valid @RequestBody Advertisement advertisementRequestDTO);

    @PutMapping("/{id}")
    Advertisement update(@PathVariable UUID id, @Valid @RequestBody Advertisement advertisementRequestDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id);
}
