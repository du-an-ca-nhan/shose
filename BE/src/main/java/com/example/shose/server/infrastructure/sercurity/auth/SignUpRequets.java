package com.example.shose.server.infrastructure.sercurity.auth;

import com.example.shose.server.infrastructure.constant.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SignUpRequets {

    private String email;

    private String password;

    private Roles roles;
}
