package com.example.shose.server.service;

import com.example.shose.server.dto.request.login.LoginRequest;
import com.example.shose.server.dto.request.login.ResetPassword;
import com.example.shose.server.dto.response.LoginResponse;

public interface LoginService {

    LoginResponse getOneByEmailAndPass(LoginRequest request);

    LoginResponse resetPassword (ResetPassword resetPassword);
}
