package com.example.shose.server.controller.admin;

import com.example.shose.server.dto.request.login.LoginRequest;
import com.example.shose.server.dto.request.login.ResetPassword;
import com.example.shose.server.service.LoginService;
import com.example.shose.server.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/login")
public class LoginRestController {

    @Autowired
    private LoginService loginService;

    @GetMapping()
    public ResponseObject authentication (LoginRequest request){
        return new ResponseObject(loginService.getOneByEmailAndPass(request));
    }

    @GetMapping("/rest-password")
    public ResponseObject restPassword (ResetPassword resetPassword){
        return new ResponseObject(loginService.resetPassword(resetPassword));
    }
}
