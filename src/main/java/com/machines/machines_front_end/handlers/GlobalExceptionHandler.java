package com.machines.machines_front_end.handlers;

import com.machines.machines_front_end.exceptions.RedirectException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
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
}

