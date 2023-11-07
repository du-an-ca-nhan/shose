import { request } from "../../../helper/request";

export class AccountClientApi {
  static changePassword = (data) => {
    return request({
      method: "POST",
      url: `/client/account/changePassword`,
      data: data,
    });
  };
  static getById = (id) => {
    return request({
      method: "GET",
      url: `/client/account/${id}`
    });
  };
  static updateInfoUser = (data) => {
    return request({
      method: "POST",
      url: `/client/account/updateInfo`,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      data: data
    });
  };
  
}