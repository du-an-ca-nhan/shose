package com.example.shose.server.dto.request.productdetail;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UpdateProductDetailRequest extends BaseProductDetailRequest{

    private String id;

    private String colorId;

    private String sizeId;
}
