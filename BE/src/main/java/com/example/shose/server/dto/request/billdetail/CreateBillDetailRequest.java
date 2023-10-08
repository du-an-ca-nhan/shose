package com.example.shose.server.dto.request.billdetail;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateBillDetailRequest {

    @NotEmpty
    private String idBill;

    @NotEmpty
    private String idProduct;

    @NotNull
    private Integer size;

    @NotNull
    private int quantity;

    @NotEmpty
    private String totalMoney;

    @NotEmpty
    private String price;
}
