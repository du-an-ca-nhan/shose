package com.example.shose.server.dto.request.brand;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UpdateBrandRequest extends BaseBrandRequest{

    private String id;
}
