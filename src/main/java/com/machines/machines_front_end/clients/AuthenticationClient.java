package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.dtos.auth.AuthenticationRequest;
import com.machines.machines_front_end.dtos.auth.AuthenticationResponse;
import com.machines.machines_front_end.dtos.auth.RefreshTokenBodyDTO;
import com.machines.machines_front_end.dtos.auth.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/forgot-password")
        // Sends link to email so the user can change their password
    ResponseEntity<String> forgotPassword(@RequestParam("email") String email);

    @PostMapping("/password-reset")
    ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword);
}
