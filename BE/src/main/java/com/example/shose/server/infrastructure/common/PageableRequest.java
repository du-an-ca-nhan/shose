package com.example.shose.server.infrastructure.common;

import com.example.shose.server.infrastructure.constant.PaginationConstant;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public abstract class PageableRequest {

    private int page = PaginationConstant.DEFAULT_PAGE;
    private int size = PaginationConstant.DEFAULT_SIZE;
}
