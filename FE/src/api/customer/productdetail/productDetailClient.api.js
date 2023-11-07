import { request } from "../../../helper/request";

export class ProductDetailClientApi {
  static getByIdCategory = (id) => {
    return request({
      method: "GET",
      url: `/client/product-detail/byCategory/${id}`,
    });
  };

  static getDetailProductOfClient = (idProductDetail) => {
    return request({
      method: "GET",
      url: `/client/product-detail/${idProductDetail}`,
    });
  };
  static getListSizeCart = (id, codeColor) => {
    return request({
      method: "GET",
      url: `/client/product-detail/listSizeCart/${id}&&${codeColor}`,
    });
  };
  static getDetailProductHavePromotion = (pageNo) => {
    return request({
      method: "GET",
      url: `/client/product-detail/have-promotion?page=${pageNo}&size=12`,
    });
  };
  static getDetailProductNew = (pageNo) => {
    return request({
      method: "GET",
      url: `/client/product-detail/new?page=${pageNo}&size=12`,
    });
  };
  static getDetailProductSellMany = (pageNo) => {
    return request({
      method: "GET",
      url: `/client/product-detail/sell-many?page=${pageNo}&size=12`,
    });
  };
  static list = (data) => {
    return request({
      method: "GET",
      url: `/client/product-detail/list`,
      params: data,
    });
  };
}
