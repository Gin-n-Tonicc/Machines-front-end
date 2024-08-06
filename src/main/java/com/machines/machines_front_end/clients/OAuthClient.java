package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.config.FeignClientConfiguration;
import com.machines.machines_front_end.dtos.auth.AuthenticationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "machines-api-oauth2", url = "${backend.base-url}/oauth2", configuration = FeignClientConfiguration.class)
public interface OAuthClient {
    @GetMapping("/url/google")
    String auth();

    @GetMapping("/authenticate/google")
    AuthenticationResponse googleAuthenticate(@RequestParam("code") String code);
}
