package com.example.shose.server.service;


import com.example.shose.server.dto.response.cartdetail.ChangeQuantity;
import com.example.shose.server.dto.response.cartdetail.ChangeSizeInCart;
import com.example.shose.server.entity.CartDetail;

public interface CartDetailService {
    String changeSizeCartDetail(ChangeSizeInCart changeSize);
    Boolean deleteCartDetail(String id);
    String changeQuantity(ChangeQuantity changeQuantity);
}
