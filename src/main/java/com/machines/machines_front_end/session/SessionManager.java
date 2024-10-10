package com.machines.machines_front_end.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {
    public void setSessionToken(HttpServletRequest request, String sessionToken, String role) {
        String token = "Bearer " + sessionToken;

        HttpSession session = request.getSession(true);
        session.setAttribute("sessionToken", token);
        session.setAttribute("sessionRole", role);
    }

    public void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        String token = (String) session.getAttribute("sessionToken");
        return token != null && !token.equals("");
    }

    public boolean hasRole(HttpServletRequest request, String role) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            String sessionRole = (String) session.getAttribute("sessionRole");
            return sessionRole != null && sessionRole.equals(role);
        }

        return false;
    }

    public boolean isAdmin(HttpServletRequest request) {
        return hasRole(request, "ADMIN");
    }
}
