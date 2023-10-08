package com.example.shose.server.dto.request.customer;

import com.example.shose.server.infrastructure.constant.Status;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class BaseCustomerRequest {

    private String fullName;

    private Long dateOfBirth;

    private String phoneNumber;

    private String email;

    private Boolean gender;

    private String avata;

    private Status status;

    private String password;

    private Integer points;

    private String citizenIdentity;

}
