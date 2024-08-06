package com.machines.machines_front_end.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/process-oauth2")
public class OAuth2RedirectController {

    @GetMapping
    public String processOAuth2Redirect() {
        return "process-oauth2";
    }
}