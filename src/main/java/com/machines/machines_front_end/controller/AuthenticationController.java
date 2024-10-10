package com.machines.machines_front_end.controller;

import com.machines.machines_front_end.annotations.PageRoleGuard;
import com.machines.machines_front_end.clients.AuthenticationClient;
import com.machines.machines_front_end.dtos.auth.AuthenticationRequest;
import com.machines.machines_front_end.dtos.auth.AuthenticationResponse;
import com.machines.machines_front_end.dtos.auth.RegisterRequest;
import com.machines.machines_front_end.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private static final String REDIRECT_INDEX = "redirect:/";
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private final AuthenticationClient authenticationClient;
    private final SessionManager sessionManager;

    @GetMapping("login")
    @PageRoleGuard(redirectTo = "/", authenticated = false)
    public String login(HttpSession session) {
        return "login";
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        sessionManager.invalidateSession(request);
        return new ModelAndView("redirect:/");
    }


    @PostMapping("/login")
    @PageRoleGuard(redirectTo = "/", authenticated = false)
    public String login(AuthenticationRequest authenticationRequest, Model model, HttpServletRequest httpServletRequest, HttpSession httpSession) {
        try {
            AuthenticationResponse authenticationResponse = authenticationClient.login(authenticationRequest);
            httpSession.setAttribute("userId", authenticationResponse.getUser().getId());
            sessionManager.setSessionToken(httpServletRequest, authenticationResponse.getAccessToken(), authenticationResponse.getUser().getRole().toString());
            return REDIRECT_INDEX; // Redirect to a success page, e.g., home page
        } catch (Exception e) {
            model.addAttribute("error", "Невалидно име или парола"); // Customize the error message as needed
            return "login"; // Return to the login form with an error message
        }
    }

    @GetMapping("/register")
    @PageRoleGuard(redirectTo = "/", authenticated = false)
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    @PageRoleGuard(redirectTo = "/", authenticated = false)
    public String registerUser(RegisterRequest request, Model model, HttpServletRequest httpServletRequest) {
        try {
            AuthenticationResponse authenticationResponse = authenticationClient.register(request);
            sessionManager.setSessionToken(httpServletRequest, authenticationResponse.getAccessToken(), authenticationResponse.getUser().getRole().toString());
            return REDIRECT_INDEX; // Name of the success Thymeleaf template
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            return "register"; // Return to the registration form with an error message
        }
    }

    @GetMapping("/forgot-password")
    @PageRoleGuard(redirectTo = "/", authenticated = false)
    public String showForgotPasswordForm() {
        return "forgot-password"; // Name of the Thymeleaf template
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        try {
            ResponseEntity<String> response = authenticationClient.forgotPassword(email);
            model.addAttribute("message", response.getBody());
            return "forgot-password-success"; // Name of the success Thymeleaf template
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null && e.getCause().getMessage() != null)
                    ? e.getCause().getMessage()
                    : e.getMessage();
            model.addAttribute("error", errorMessage);
            return "forgot-password"; // Redirect back to the form with error message
        }
    }

    @GetMapping("/forgotten-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String password, Model model) {
        try {
            authenticationClient.resetPassword(token, password);
            model.addAttribute("message", "Password successfully updated.");
            return "reset-password-success"; // Name of the success Thymeleaf template
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "reset-password"; // Return to the form with an error message
        }
    }

}