import { request } from "../../../helper/request";

export class CartDetailClientApi {
  static changeSize = (data) => {
    return request({
      method: "POST",
      url: `/cart-detail/change-size`,
      data: data,
    });
  };
  static deleteCartDetail = (idCartDetail) => {
    return request({
      method: "DELETE",
      url: `/cart-detail/${idCartDetail}`,
    });
  };
  static changeQuantity = (data) => {
    return request({
      method: "POST",
      url: `/cart-detail/change-quantity`,
      data: data,
    });
  };
}
