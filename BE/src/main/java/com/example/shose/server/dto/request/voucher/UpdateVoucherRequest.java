package com.example.shose.server.dto.request.voucher;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateVoucherRequest extends BaseVoucherRequest{
    private String id;
}
