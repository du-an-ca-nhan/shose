import {
  faEye,
  faFilter,
  faKaaba,
  faListAlt,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  Button,
  Form,
  Table,
  Select,
  Space,
  Row,
  Col,
  Tabs,
  Tooltip,
  Badge,
} from "antd";
import moment from "moment";
import React from "react";
import { useEffect } from "react";
import { useSelector } from "react-redux";
import { BillApi } from "../../../api/employee/bill/bill.api";
import { useState } from "react";
import { useAppDispatch } from "../../../app/hook";
import {
  getAllBill,
  getEmployees,
  getUsers,
} from "../../../app/reducer/Bill.reducer";
import "./style-bill.css";
import { useRef } from "react";
import { AccountApi } from "../../../api/employee/account/account.api";
import FormSearch from "./FormSearch";
import { Link } from "react-router-dom";
import { AddressApi } from "../../../api/employee/address/address.api";
import TabBills from "./TabBills";
import { PlusOutlined } from "@ant-design/icons";

function BillManagement() {
  var listBill = useSelector((state) => state.bill.bills.value);

  var users = useSelector((state) => state.bill.search.users);
  var employees = useSelector((state) => state.bill.search.employees);
  var [status, setStatus] = useState([]);
  var [quantityNotify, setQuantityNotify] = useState([]);

  const dispatch = useAppDispatch();
  const { Option } = Select;

  useEffect(() => {
    console.log(fillter);
    BillApi.fetchAll(fillter).then((res) => {
      dispatch(getAllBill(res.data.data));
    });
  }, []);

  const onChangeFillter = (value, fileName) => {
    setFillter({ ...fillter, [fileName]: value });
  };

  const onChangeStatusBillInFillter = (value) => {
    setStatus(value);
  };

  const handleSubmitSearch = (e) => {
    var data = fillter;
    if (status.length != 0) {
      data.status = status;
    }
    setFillter(data);
    BillApi.fetchAll(fillter).then((res) => {
      dispatch(getAllBill(res.data.data));
    });
  };

  const handleSelectChange = async (value, fileName) => {
    // setFillter({ ...fillter, [fileName]: value });
    var data = fillter;
    data.status = status;
    data.type = value;
    setFillter(data);
    await BillApi.fetchAll(fillter).then((res) => {
      dispatch(getAllBill(res.data.data));
    });
  };
  const handleSelectMultipleChange = (value) => {
    var arr = Object.keys(value).map(function (key) {
      return value[key];
    });
    setStatus(arr);
    var data = fillter;
    data.status = status;
    setFillter(data);
    BillApi.fetchAll(fillter).then((res) => {
      dispatch(getAllBill(res.data.data));
    });
  };

  const clearFillter = (e) => {
    setFillter({
      startTimeString: "",
      endTimeString: "",
      status: [
        "CHO_XAC_NHAN",
        "CHO_VAN_CHUYEN",
        "VAN_CHUYEN",
        "DA_THANH_TOAN",
        "THANH_CONG",
        "TRA_HANG",
        "DA_HUY",
      ],
      endDeliveryDateString: "",
      startDeliveryDateString: "",
      key: "",
      employees: "",
      user: "",
      phoneNumber: "",
      type: "",
      page: 0,
    });
    setStatus([
      "CHO_XAC_NHAN",
      "CHO_VAN_CHUYEN",
      "VAN_CHUYEN",
      "DA_THANH_TOAN",
      "THANH_CONG",
      "TRA_HANG",
      "DA_HUY",
    ]);
    console.log(fillter);
    BillApi.fetchAll(fillter).then((res) => {
      dispatch(getAllBill(res.data.data));
    });
  };

  const [fillter, setFillter] = useState({
    startTimeString: "",
    endTimeString: "",
    status: [
      "CHO_XAC_NHAN",
      "CHO_VAN_CHUYEN",
      "VAN_CHUYEN",
      "DA_THANH_TOAN",
      "THANH_CONG",
      "TRA_HANG",
      "DA_HUY",
    ],
    endDeliveryDateString: "",
    startDeliveryDateString: "",
    key: "",
    employees: "",
    user: "",
    phoneNumber: "",
    type: "",
    page: 0,
  });

  const onChange = (key) => {
    setQuantityNotify(quantityNotify.filter((item) => item.status !== key));
  };

  const listtab = [
    "",
    "CHO_XAC_NHAN",
    "XAC_NHAN",
    "CHO_VAN_CHUYEN",
    "VAN_CHUYEN",
    "DA_THANH_TOAN",
    "THANH_CONG",
    "DA_HUY",
  ];

  const convertString = (key) => {
    return key === ""
      ? "Tất cả"
      : key === "CHO_XAC_NHAN"
      ? "Chờ xác nhận"
      : key === "XAC_NHAN"
      ? "Xác nhận"
      : key === "CHO_VAN_CHUYEN"
      ? "Chờ vận chuyển"
      : key === "VAN_CHUYEN"
      ? "Vận chuyển"
      : key === "DA_THANH_TOAN"
      ? "thanh toán"
      : key === "THANH_CONG"
      ? "Hoàn thành"
      : "Hủy";
  };
  const checkNotifyNew = (key) => {
    var index = quantityNotify.findIndex((item) => item.name == key);
    if (index == -1) {
      return false;
    } else {
      return true;
    }
  };

  const showQuantityBillNotify = (key) => {
    var index = quantityNotify.findIndex((item) => item.status == key);
    if (index != -1) {
      return quantityNotify[index].quantity;
    }
    return null;
  };

  const addNotify = (notify) => {
    setQuantityNotify([...quantityNotify, notify]);
  };

  return (
    <div>
      <div className="title_category">
        {" "}
        <FontAwesomeIcon icon={faKaaba} style={{ fontSize: "26px" }} />
        <span style={{ marginLeft: "10px" }}>Quản lý Hóa đơn</span>
      </div>
      <div className="filter">
        <FontAwesomeIcon icon={faFilter} size="2x" />{" "}
        <span style={{ fontSize: "18px", fontWeight: "500" }}>Bộ lọc</span>
        <hr />
        <div className="">
          <div className="">
            <FormSearch
              fillter={fillter}
              changFillter={onChangeFillter}
              users={users}
              employess={employees}
              onChangeStatusBillInFillter={onChangeStatusBillInFillter}
              status={status}
              handleSubmitSearch={handleSubmitSearch}
              clearFillter={clearFillter}
              handleSelectMultipleChange={handleSelectMultipleChange}
              handleSelectChange={handleSelectChange}
            />
          </div>
        </div>
        <div className="box_btn_filter"></div>
      </div>

      <div className="bill-table">
        <div
          className="title_bill"
          style={{ display: "flex", alignItems: "center" }}
        >
          <FontAwesomeIcon
            icon={faListAlt}
            style={{ fontSize: "26px", marginRight: "10px" }}
          />
          <span style={{ fontSize: "18px", fontWeight: "500" }}>
            Danh sách Hóa đơn
          </span>
          <div style={{ marginLeft: "auto" }}></div>
          <Link to={"/sale-counter"} style={{ marginRight: "10px" }}>
            <Button type="primary" icon={<PlusOutlined />} size={"large"}>
              Tạo đơn hàng
            </Button>
          </Link>
        </div>
        <div style={{ marginTop: "25px" }}>
          <Tabs
            onChange={onChange}
            type="card"
            items={listtab.map((item) => {
              return {
                label: (
                  <Badge count={showQuantityBillNotify(item)}>
                    <span>{convertString(item)}</span>
                  </Badge>
                ),
                key: item,
                children: (
                  <TabBills
                    statusBill={item}
                    dataFillter={fillter}
                    addNotify={addNotify}
                  />
                ),
              };
            })}
          />
        </div>
      </div>
    </div>
  );
}

export default BillManagement;
