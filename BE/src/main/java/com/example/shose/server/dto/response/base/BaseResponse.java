package com.example.shose.server.dto.response.base;

import org.springframework.beans.factory.annotation.Value;


public interface BaseResponse {


    @Value("#{target.id}")
    String getId();

    @Value("#{target.name}")
    String getName();

    @Value("#{target.status}")
    String getStatus();

    @Value("#{target.createdDate}")
    Long getCreatedDate();

    @Value("#{target.lastModifiedDate}")
    Long getLastModifiedDate();
}
