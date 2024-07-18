package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.dtos.auth.AuthenticationRequest;
import com.machines.machines_front_end.dtos.auth.AuthenticationResponse;
import com.machines.machines_front_end.dtos.auth.RefreshTokenBodyDTO;
import com.machines.machines_front_end.dtos.auth.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "machines-api-test", url = "${backend.base-url}/auth")

public interface AuthenticationClient {

    @PostMapping("/register")
    AuthenticationResponse register(@RequestBody RegisterRequest request);

    @PostMapping("/authenticate")
    AuthenticationResponse login(@RequestBody AuthenticationRequest request);

    @PostMapping("/refresh-token")
    AuthenticationResponse refreshToken(@RequestBody RefreshTokenBodyDTO refreshTokenBody);

    @GetMapping("/logout")
    void logout(@RequestHeader("Authorization") String auth);
}
