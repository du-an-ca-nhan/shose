package com.example.shose.server.dto.request.product;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UpdateProductRequest extends BaseProductRequest{

    private String id;
}
