package com.example.shose.server.dto.request.customer;

import com.example.shose.server.infrastructure.constant.Status;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuickCreateCustomerRequest {

    private String fullName;

    private String phoneNumber;

    private String email;

    private Boolean gender;
}
