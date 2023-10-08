package com.example.shose.server.service.impl;

import com.example.shose.server.dto.request.login.LoginRequest;
import com.example.shose.server.dto.request.login.ResetPassword;
import com.example.shose.server.dto.response.LoginResponse;
import com.example.shose.server.entity.Account;
import com.example.shose.server.infrastructure.constant.Message;
import com.example.shose.server.infrastructure.email.SendEmailService;
import com.example.shose.server.infrastructure.exception.rest.RestApiException;
import com.example.shose.server.repository.AccountRepository;
import com.example.shose.server.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SendEmailService sendEmailService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginResponse getOneByEmailAndPass(LoginRequest request) {
        Account account = accountRepository.getOneByEmail(request.getEmail());
        if (account == null) {
            throw new RestApiException(Message.NOT_EXISTS);
        }
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new RestApiException((Message.PASSWORD_NOT_EXISTS));
        }
        LoginResponse response = new LoginResponse(account);
        return response;
    }

    @Override
    public LoginResponse resetPassword(ResetPassword resetPassword) {
        Account account = accountRepository.resetPassword(resetPassword.getEmailForgot(), resetPassword.getPhoneNumber());
        if (account == null) {
            throw new RestApiException(Message.NOT_EXISTS);
        }
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        account.setPassword(passwordEncoder.encode(String.valueOf(randomNumber)));
        accountRepository.save(account);
        String subject = "Mật khẩu mới của bạn ";
        sendEmailService.sendEmailPasword(account.getEmail(),subject,String.valueOf(randomNumber));
        LoginResponse response = new LoginResponse(account);
        return response;
    }
}
