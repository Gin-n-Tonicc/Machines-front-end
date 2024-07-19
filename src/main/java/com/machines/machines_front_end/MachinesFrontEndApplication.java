package com.machines.machines_front_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MachinesFrontEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(MachinesFrontEndApplication.class, args);
    }

}