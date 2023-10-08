import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./style-payment-account.css";
import { Row, Col, InputNumber, Input, Select, Form } from "antd";
import { CloseOutlined } from "@ant-design/icons";
import { toast } from "react-toastify";
import { AddressClientApi } from "./../../../api/customer/address/addressClient.api";
import { BillClientApi } from "./../../../api/customer/bill/billClient.api";
import { PaymentClientApi } from "../../../api/customer/payment/paymentClient.api";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import logoVnPay from "../../../../src/assets/images/logo_vnpay.png";
import {
  faCarRear,
  faTags,
  faLocationDot,
} from "@fortawesome/free-solid-svg-icons";
import dayjs from "dayjs";
import utc from "dayjs/plugin/utc";
import { parseInt } from "lodash";
import { useCart } from "../cart/CartContext";
dayjs.extend(utc);
function PaymentAccount() {
  const { updateTotalQuantity } = useCart();
  const { Option } = Select;
  const idAccount = localStorage.getItem("idAccount");
  const [listCity, setListCity] = useState([]);
  const [listDistrict, setListDistrict] = useState([]);
  const [addressDefault, setAddressDefault] = useState({});
  const [listWard, setListWard] = useState([]);
  const [formBill, setFormBill] = useState({
    address: "",
    billDetail: [],
    itemDiscount: 0,
    paymentMethod: "paymentReceive",
    phoneNumber: "",
    totalMoney: 0,
    userName: "",
    idVoucher:"",
    afterPrice:0
  });
  const [moneyShip, setMoneyShip] = useState(0);
  const [dayShip, setDayShip] = useState("");
  const [keyMethodPayment, setKeyMethodPayment] = useState("paymentReceive");
  const [totalBill, setTotalBill] = useState(0);
  const [formGetMoneyShip, setFormGetMoneyShip] = useState([]);
  const listproductOfBill = JSON.parse(sessionStorage.getItem("bill"));
  const voucher = JSON.parse(sessionStorage.getItem("voucher"));
  const comercial = [
    { title: "CHÀO MỪNG QUÝ KHÁCH!" },
    { title: " CHÚC QUÝ KHÁCH MUA HÀNG HAPPY!" },
    { title: " FREE SHIPPING VỚI HÓA ĐƠN TRÊN 800K!" },
  ];
  const [currentTitleIndex, setCurrentTitleIndex] = useState(0);
  const [totalAfter, setTotalAfter] = useState({});
  const [total, setTotal] = useState({});
  const [totalBefore, setTotalBefore] = useState(0);

  useEffect(() => {
 
    getAddressDefault(idAccount);
    moneyBefore();
   
    const interval = setInterval(() => {
      setCurrentTitleIndex((prevIndex) => (prevIndex + 1) % comercial.length);
    }, 3000);

    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    console.log(formBill);
  }, [formBill]);
  useEffect(() => {
    formBillChange("totalMoney", totalBefore)
  }, [totalBefore]);
  useEffect(() => {
    setTotalBefore(total.totalMoney - voucher.value);
    
  }, [total]);
  useEffect(() => {
    formBillChange("afterPrice", totalAfter)
  }, [totalAfter]);

  useEffect(() => {
    setTotalAfter(totalBefore + moneyShip);
    formBillChange("moneyShip", moneyShip)
    
  }, [moneyShip]);
  useEffect(() => {
    getMoneyShip(addressDefault.districtId, addressDefault.wardCode);
    getDayShip(addressDefault.districtId, addressDefault.wardCode)
    const updatedListproductOfBill = listproductOfBill.map((item) => {
      const { nameProduct, nameSize,image, ...rest } = item;
      return rest;
    });
    setFormBill((prevFormBill) => ({
      ...prevFormBill,
      "address": addressDefault.line + ", " + addressDefault.ward + " - " + addressDefault.district + " - " + addressDefault.province,
      "phoneNumber": addressDefault.phoneNumber,
      "userName": addressDefault.fullname,
      "idVoucher": voucher.idVoucher,
      "itemDiscount": voucher.value,
      "billDetail":updatedListproductOfBill,
      "idAccount":idAccount
    }));
    console.log(addressDefault);
  }, [addressDefault]);

  useEffect(() => {
    console.log(keyMethodPayment);
  }, [keyMethodPayment]);

  const getAddressDefault = (idAccount) => {
    AddressClientApi.getByAccountAndStatus(idAccount).then(
      (res) => {
        setAddressDefault(res.data.data);
      },
      (err) => {
        console.log(err);
      }
    );
  };
  const payment = () => {
   

    if (formBill.paymentMethod === "paymentVnpay") {
      const data = {
        vnp_Ammount: totalBefore,
      };
      PaymentClientApi.paymentVnpay(data).then(
        (res) => {
          window.location.replace(res.data.data);
          sessionStorage.setItem("formBill", JSON.stringify(formBill));
        },
        (err) => {
          console.log(err);
        }
      );
    } else {
      BillClientApi.createBillAccountOnline(formBill).then(
        (res) => {
          // console.log("thanh toán khi nhận hàng!");
          // const cartLocal = JSON.parse(localStorage.getItem("cartLocal"));
          // const updatelist = cartLocal.filter((item) => {
          //   // Kiểm tra xem item.idProductDetail có tồn tại trong formBill.billDetail hay không
          //   return !formBill.billDetail.some((itemBill) => item.idProductDetail === itemBill.idProductDetail);
          // });
          // const total = updatelist.reduce((acc, item) => acc + item.quantity, 0);
          // updateTotalQuantity(total);
          // localStorage.setItem("cartLocal", JSON.stringify(updatelist));
        },
        (err) => {
          console.log(err);
        }
      );
    }
  };

  const getMoneyShip = (districtId, wardCode) => {
    if (totalBill > 2000000) {
      setMoneyShip(0);
    } else {
      AddressClientApi.getMoneyShip(districtId, wardCode).then(
        (res) => {
          setMoneyShip(res.data.data.total);
        },
        (err) => {
          console.log(err);
        }
      );
    }
  };
  const getDayShip = (districtId, wardCode) => {
    AddressClientApi.getDayShip(districtId, wardCode).then(
      (res) => {
        const day = dayjs(res.data.data.leadtime * 1000).format("DD-MM-YYYY");
        setDayShip(day);
      },
      (err) => {
        console.log(err);
      }
    );
  };
  const formatMoney = (price) => {
    return (
      parseInt(price)
        .toString()
        .replace(/\B(?=(\d{3})+(?!\d))/g, ".") + " VND"
    );
  };
  const formBillChange = (name, value) => {
    setFormBill((prevFormBill) => ({
      ...prevFormBill,
      [name]: value,
    }));
  };

  const paymentReceive = () => {
    setKeyMethodPayment("paymentReceive");
    setFormBill({ ...formBill, paymentMethod: "paymentReceive" });
  };
  const paymentVnpay = () => {
    setKeyMethodPayment("paymentVnpay");
    setFormBill({ ...formBill, paymentMethod: "paymentVnpay" });
  };
  const moneyBefore = () => {
    const money = listproductOfBill.reduce(
      (total, item) => total + parseInt(item.price * item.quantity),
      0
    );
    const quantity = listproductOfBill.reduce(
      (total, item) => total + parseInt(item.quantity),
      0
    );
    console.log(quantity);
    setTotal((prev) => ({
      ...prev,
      totalMoney: money,
      totalQuantity: quantity,
    }));
  };

  return (
    <div className="payment-acc">
      <div className="comercial">{comercial[currentTitleIndex].title}</div>
      <Row>
        <Col lg={{ span: 16, offset: 4 }}>
          <div className="border-top-address"></div>
          <div className="address-payment">
            <div className="title-address-recieve-good">
              <span className="icon-address-payment-acc">
                <FontAwesomeIcon icon={faLocationDot} /> Địa chỉ nhận hàng
              </span>
            </div>
            <div>
              <span style={{ fontSize: 17, fontWeight: 600 }}>
                {addressDefault.fullname}
              </span>
              {" | "}
              <span>{addressDefault.phoneNumber}</span>
              <span style={{ fontSize: 17, marginLeft: 100 }}>
                {addressDefault.line}, {addressDefault.ward},{" "}
                {addressDefault.district}, {addressDefault.province}
              </span>
              {addressDefault.status === "DANG_SU_DUNG" ? (
                <span className="status-default">Mặc định</span>
              ) : (
                ""
              )}
              <span className="change-address-payment">Thay đổi</span>
            </div>
          </div>
          <div className="product-of-bill-acc">
            <div className="title-product-of-bill-acc">
              <div style={{ fontSize: 17 }}>Sản phẩm</div>
              <div style={{ marginLeft: "30%" }}>Size</div>
              <div style={{ marginLeft: "12%" }}>Đơn giá</div>
              <div style={{ marginLeft: "12%" }}>Số lượng</div>
              <div style={{ marginLeft: "auto" }}>Thành tiền</div>
            </div>

            <div className="content-product-of-bill-acc">
              {listproductOfBill.map((item, index) => (
                <div className="item-product-bill-acc">
                  <div
                    style={{
                      width: 433,
                      display: "flex",
                      alignItems: "center",
                    }}
                  >
                    <img
                      className="img-product-bill-acc"
                      src={item.image.split(",")[0]}
                      alt="..."
                    />
                    <span style={{ marginLeft: "5%" }}>{item.nameProduct}</span>
                  </div>
                  <span>{item.nameSize}</span>
                  <span style={{ marginLeft: "11%" }}>
                    {" "}
                    {formatMoney(item.price)}
                  </span>
                  <span style={{ marginLeft: "12%" }}>{item.quantity}</span>
                  <span style={{ marginLeft: "auto" }}>
                    {" "}
                    {formatMoney(parseInt(item.price) * item.quantity)}
                  </span>
                </div>
              ))}
              <div style={{ display: "flex" }}>
                <div style={{ marginLeft: "auto" }}>
                  Tổng số tiền ({total.totalQuantity} sản phẩm):
                  <span className="total-money-before">
                    {formatMoney(total.totalMoney)}
                  </span>
                </div>
              </div>
            </div>
          </div>
          <div className="voucher-payment-acc">
            <span style={{ fontSize: 20 }}>
              <FontAwesomeIcon
                style={{ color: "#ff4400", marginRight: 10 }}
                icon={faTags}
              />{" "}
              Bee voucher
            </span>
            <span className="money-reduce-payment-acc">
              - {formatMoney(voucher.value)}
            </span>
          </div>
          <div className="footer-payment-acc">
            <div className="method-payment-acc">
              <div style={{ fontSize: 18, fontWeight: 600 }}>
                Phương thức thanh toán
              </div>
              <div
                className={`payment-when-recevie-acc ${
                  keyMethodPayment === "paymentReceive" ? "click" : ""
                }`}
                onClick={paymentReceive}
              >
                Thanh toán khi nhận hàng
              </div>
              <div
                className={`payment-by-vnpay-acc ${
                  keyMethodPayment === "paymentVnpay" ? "click" : ""
                }`}
                onClick={paymentVnpay}
              >
                Thanh toán VnPay{" "}
                <img
                  style={{ width: "40px", marginLeft: "20px" }}
                  src={logoVnPay}
                  alt="..."
                />
              </div>
            </div>
            <div className="time-recieve-goods">
              <FontAwesomeIcon
                icon={faCarRear}
                style={{ fontSize: "30px", marginRight: "20px" }}
              />
              <span>Thời gian nhận hàng dự kiến: {dayShip}</span>
            </div>
            <div className="titles-money-payment-acc">
              <div className="box-title-money">
                <h3 className="title-money">Tổng tiền</h3>{" "}
                <div className="text-money"> {formatMoney(totalBefore)}</div>
              </div>
              <div className="box-title-money">
                <h3 className="title-money">Phí vận chuyển</h3>{" "}
                <div className="text-money">{formatMoney(moneyShip)} </div>
              </div>
              <div className="box-title-money">
                <h3 className="title-money">Tổng thanh toán</h3>{" "}
                <div className="text-money-total">
                  {formatMoney(totalAfter)}{" "}
                </div>
              </div>
            </div>
            <div className="form-submit-payment-acc">
              <div className="button-submit-buy-acc" onClick={payment}>Đặt hàng</div>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  );
}
export default PaymentAccount;
