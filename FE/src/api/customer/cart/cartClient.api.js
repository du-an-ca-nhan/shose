import { request } from "../../../helper/request";

export class CartClientApi {
    static addCart = (data) => {
        return request({
          method: "POST",
          url: `/cart`,
          data:data
        });
      };
      static listCart = (idAccount) => {
        return request({
          method: "GET",
          url: `/cart/${idAccount}`,
        });
      };
      static quantityInCart = (idAccount) => {
        return request({
          method: "GET",
          url: `/cart/quantityInCart/${idAccount}`,
        });
      };

}