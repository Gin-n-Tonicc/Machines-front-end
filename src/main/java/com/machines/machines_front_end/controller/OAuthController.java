package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.clients.OAuthClient;
import com.machines.machines_front_end.dtos.auth.AuthenticationResponse;
import com.machines.machines_front_end.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final OAuthClient oAuthClient;

    private final SessionManager sessionManager;

    @GetMapping("/oauth2/authenticate/google")
    public String authenticateWithGoogle(@RequestParam("code") String code,
                                         HttpServletRequest request) {
        try {
            AuthenticationResponse authenticationResponse = oAuthClient.googleAuthenticate(code);

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", authenticationResponse.getUser().getId());
            sessionManager.setSessionToken(request, authenticationResponse.getAccessToken(), authenticationResponse.getUser().getRole().toString());

            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/login?error=true";
        }
    }
}

