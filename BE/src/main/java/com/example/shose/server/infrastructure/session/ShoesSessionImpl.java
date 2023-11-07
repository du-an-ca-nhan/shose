package com.example.shose.server.infrastructure.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoesSessionImpl implements ShoseSession{

    @Autowired
    private HttpSession httpSession;

    @Override
    public String getUserId() {
        return String.valueOf(httpSession.getAttribute("id"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(httpSession.getAttribute("email"));
    }

    @Override
    public String getFullName() {
        return String.valueOf(httpSession.getAttribute("fullName"));
    }

    @Override
    public String getRole() {
        return String.valueOf(httpSession.getAttribute("role"));
    }
}
