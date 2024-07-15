package com.machines.machines_front_end.config;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "machines-api-test", url = "${backend.base-url}/test")
public interface TestClient {
    @GetMapping
    String getHello();
}
