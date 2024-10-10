package com.machines.machines_front_end.handlers;

import com.machines.machines_front_end.exceptions.RedirectException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RedirectException.class)
    @ResponseStatus(HttpStatus.FOUND) // Set response status for redirect
    public void handleRedirectException(RedirectException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error"; // Name of the Thymeleaf template to show errors
    }
}

