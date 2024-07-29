package com.machines.machines_front_end.clients;

import com.machines.machines_front_end.dtos.File;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(name = "machines-api-file", url = "${backend.base-url}/files")
public interface FileClient {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    File upload(@RequestPart("file") MultipartFile multipartFile);
}