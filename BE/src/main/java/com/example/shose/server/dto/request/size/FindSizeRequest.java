package com.example.shose.server.dto.request.size;

import com.example.shose.server.infrastructure.common.PageableRequest;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public  class FindSizeRequest  extends PageableRequest {

    private String name;

    private String status;
}
