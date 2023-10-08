package com.example.shose.server.dto.request.address;

import com.example.shose.server.infrastructure.constant.Status;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public abstract class BaseAddressRequest {

    private String id;

    private String line;

    private String district;

    private String province;

    private String ward;

    private Integer provinceId;

    private Integer toDistrictId;

    private String wardCode;

    private Status status;

    private String userId;
}
