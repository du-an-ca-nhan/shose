package com.example.shose.server.dto.request.material;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UpdateMaterialRequest extends BaseMaterialRequest{

    private String id;
}
