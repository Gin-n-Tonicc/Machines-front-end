package com.machines.machines_front_end.clients;


import com.machines.machines_front_end.config.FeignClientConfiguration;
import com.machines.machines_front_end.dtos.Search;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "machines-api-search", url = "${backend.base-url}/searches", configuration = FeignClientConfiguration.class)
public interface SearchClient {
    @GetMapping("/all")
    List<Search> getAll();
}
