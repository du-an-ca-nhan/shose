package com.example.shose.server.controller;

import com.example.shose.server.dto.logindto.ChangePassword;
import com.example.shose.server.dto.logindto.ResetPassword;
import com.example.shose.server.infrastructure.sercurity.auth.JwtAuhenticationResponse;
import com.example.shose.server.infrastructure.sercurity.auth.RefreshTokenRequets;
import com.example.shose.server.infrastructure.sercurity.auth.SignUpRequets;
import com.example.shose.server.infrastructure.sercurity.auth.SigninRequest;
import com.example.shose.server.service.AuthenticationService;
import com.example.shose.server.util.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login-v2")
@RequiredArgsConstructor
public class AuhenticationRestController {


    private final AuthenticationService authenticationService;

    @PostMapping("/singup")
    public String singup (@RequestBody SignUpRequets requets){
        return authenticationService.signUp(requets);
    }

    @PostMapping("/singin")
    public ResponseEntity<JwtAuhenticationResponse> singin (@RequestBody SigninRequest requets)  {
        return ResponseEntity.ok(authenticationService.singIn(requets));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuhenticationResponse> refreshToken (@RequestBody RefreshTokenRequets requets){
        return ResponseEntity.ok(authenticationService.refreshToken(requets));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword (@RequestBody ResetPassword resetPassword){
        return ResponseEntity.ok(authenticationService.resetPassword(resetPassword));
    }

    @PostMapping("/change-password")
    public ResponseObject changePassword (@RequestBody ChangePassword changePassword){
        return new ResponseObject(authenticationService.changePassword(changePassword));
    }


}
