package com.example.shose.server.dto.response.employee;

import com.example.shose.server.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;


@Projection(types = {User.class})
public interface SimpleEmployeeResponse {

    @Value("#{target.id}")
    String getId();

    @Value("#{target.full_name}")
    String getUserName();

}
