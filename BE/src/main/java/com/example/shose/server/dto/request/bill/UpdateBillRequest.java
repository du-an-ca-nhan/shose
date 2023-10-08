package com.example.shose.server.dto.request.bill;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class UpdateBillRequest {

    private String name;

    private String phoneNumber;

    private String address;

    private String moneyShip;

    private String note;
}
