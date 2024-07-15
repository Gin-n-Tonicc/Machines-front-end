package com.machines.machines_front_end.controller;
import com.machines.machines_front_end.config.TestClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/v1/test")
@AllArgsConstructor
public class TestController {

    private final TestClient testClient;

    @GetMapping("/hello")
    public String sayHello(Model model) {
        String message = testClient.getHello();
        model.addAttribute("message", message);
        return "hello";
    }
}
