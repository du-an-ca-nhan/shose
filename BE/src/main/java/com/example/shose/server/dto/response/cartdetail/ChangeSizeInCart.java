package com.example.shose.server.dto.response.cartdetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
public class ChangeSizeInCart  {

     String idCartDetail;
     BigDecimal price;
     Integer quantity;
     String idProductDetail;
}
