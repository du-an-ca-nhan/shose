package com.example.shose.server.dto.response;

import com.example.shose.server.dto.response.base.BaseResponse;
import com.example.shose.server.entity.Category;
import org.springframework.data.rest.core.config.Projection;


@Projection(types = Category.class)
public interface CategoryResponse extends BaseResponse {

}
