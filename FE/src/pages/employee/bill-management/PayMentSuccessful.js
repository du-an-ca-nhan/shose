import { Row } from "antd";
import React from "react";
import { useEffect } from "react";
import { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./style-payment-success.css";
import logo from "./../../../assets/images/logo_client.png";
import { white } from "color-name";
import { PaymentsMethodApi } from "../../../api/employee/paymentsmethod/PaymentsMethod.api";
import {
  faSquareCheck,
  faTriangleExclamation,
} from "@fortawesome/free-solid-svg-icons";
import { Link } from "react-router-dom";

const getUrlVars = () => {
  const urlParams = new URLSearchParams(window.location.search);
  const parameters = {};
  for (const [key, value] of urlParams.entries()) {
    if (key === "vnp_TxnRef" || key === "vnp_TransactionStatus") {
      parameters[key] = value;
    }
  }
  return parameters;
};

const saveToLocalStorage = (parameters) => {
  localStorage.setItem("parameters", JSON.stringify(parameters));
};

const fetchData = () => {
  const parameters = getUrlVars();
  saveToLocalStorage(parameters);
};

const getTransactionStatus = () => {
  const urlParams = new URLSearchParams(window.location.search);
  var parameters = "";
  for (const [key, value] of urlParams.entries()) {
    if (key === "vnp_TransactionStatus") {
      parameters = value;
    }
  }
  return parameters;
};

const getAmount = () => {
  const urlParams = new URLSearchParams(window.location.search);
  var parameters = "";
  for (const [key, value] of urlParams.entries()) {
    if (key === "vnp_Amount") {
      parameters = value;
    }
  }
  return parameters;
};

function PayMentSuccessful() {
  getUrlVars();
  console.log(new URLSearchParams(window.location.search));
  const [status, setStatus] = useState();
  const [amount, setAmount] = useState();
  const formatCurrency = (value) => {
    const formatter = new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
      currencyDisplay: "code",
    });
    return formatter.format(value);
  };
  useEffect(() => {
    const param = new URLSearchParams(window.location.search);

    // // chia chuỗi URL thành các phần tử
    // const parts = url.split("?");

    // lấy phần tử thứ hai
    // const queryString = parts[1];

    // in kết quả
    // console.log(url);
    fetchData();
    setStatus(getTransactionStatus());
    setAmount(getAmount());
    PaymentsMethodApi.checkPaymentVnPay(param).then((res) => {});
  }, []);
  setTimeout(() => {
    window.open("http://localhost:3000/sale-counter", "_self");
  }, 10000);
  return (
    <>
      <div className="header-payment-success">
        <img className="logo-payment-success" src={logo} alt="logo" />
      </div>
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        {status === "00" ? (
          <div className="content-payment-success">
            <FontAwesomeIcon
              className="icon-payment-success"
              icon={faSquareCheck}
            />
            <h1>Thanh toán thành công</h1>
            <div style={{ marginTop: "5%" }}>
              Tổng thanh toán: {formatCurrency(amount)}
            </div>
            <Link to="/sale">Tiếp tục mua</Link>
          </div>
        ) : (
          <div className="content-payment-success">
            <FontAwesomeIcon
              className="icon-payment-fail"
              icon={faTriangleExclamation}
            />
            <h1>Thanh toán thất bại</h1>
            <div>
              <Link style={{ marginLeft: "10px" }} to="/sale-counter">
                Tiếp tục mua
              </Link>
            </div>
          </div>
        )}
      </div>
    </>
  );
}

export default PayMentSuccessful;
