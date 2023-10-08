package com.example.shose.server.dto.request.paymentsmethod;

import com.example.shose.server.entity.Bill;
import com.example.shose.server.infrastructure.constant.StatusMethod;
import com.example.shose.server.infrastructure.constant.StatusPayMents;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class CreatePaymentsMethodRequest {

    private String actionDescription;

    private BigDecimal totalMoney;

    private StatusMethod method;

    private StatusPayMents status;

}
