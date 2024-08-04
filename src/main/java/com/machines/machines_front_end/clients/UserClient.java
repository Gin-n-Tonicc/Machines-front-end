package com.machines.machines_front_end.clients;


import com.machines.machines_front_end.config.FeignClientConfiguration;
import com.machines.machines_front_end.dtos.auth.AdminUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "machines-api-user", url = "${backend.base-url}/users", configuration = FeignClientConfiguration.class)
public interface UserClient {
    @GetMapping("/all")
    List<AdminUserDTO> getAllUsers();

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable("id") UUID id);

    @GetMapping("/{id}/admin")
    AdminUserDTO getByIdAdmin(@PathVariable UUID id);

    @PutMapping("/{id}")
    AdminUserDTO update(@PathVariable("id") UUID id, @RequestBody AdminUserDTO userDTO);
}
