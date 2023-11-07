import React, { useEffect, useState } from "react";
import "./style-voucher.css";
import {
  Form,
  Input,
  Button,
  Select,
  Table,
  InputNumber,
  DatePicker,
  Row,
  Col,
} from "antd";
import { VoucherApi } from "../../../api/employee/voucher/Voucher.api";
import { GetVoucher, SetVoucher } from "../../../app/reducer/Voucher.reducer";
import CreateVoucherManagement from "./modal/CreateVoucherManagement";
import UpdateVoucherManagement from "./modal/UpdateVoucherManagement";
import DetailVoucherManagement from "./modal/DetailVoucherManagement";
import {
  faEdit,
  faEye,
  faFilter,
  faKaaba,
  faListAlt,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useAppDispatch, useAppSelector } from "../../../app/hook";
import dayjs from "dayjs";
import utc from "dayjs/plugin/utc";
dayjs.extend(utc);
const VoucherManagement = () => {
  const dispatch = useAppDispatch();
  const [list, setList] = useState([]);
  const [modalCreate, setModalCreate] = useState(false);
  const [modalUpdate, setModalUpdate] = useState(false);
  const [modalDetail, setModalDetail] = useState(false);
  const [id, setId] = useState("");
  const [formDataSearch, setFormDataSearch] = useState({});

  const data = useAppSelector(GetVoucher);
  useEffect(() => {
    if (data != null) {
      setList(data);
      console.log(data);
    }
  }, [data]);
  useEffect(() => {
    console.log(list);
  }, [list]);

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [formDataSearch]);
  const updatedList = list.map((item, index) => ({
    ...item,
    stt: index + 1,
  }));

  const resetFormSearch = () => {
    setFormDataSearch({});
    loadData();
  };

  const convertToLongSearch = () => {
    const convertedFormDataSearch = { ...formDataSearch };
    if (formDataSearch.startDate) {
      convertedFormDataSearch.startDate =
        dayjs(formDataSearch.startDate).unix() * 1000;
    }
    if (formDataSearch.endDate) {
      convertedFormDataSearch.endDate =
        dayjs(formDataSearch.endDate).unix() * 1000;
    }
    return convertedFormDataSearch;
  };

  const loadData = () => {
    VoucherApi.fetchAll(convertToLongSearch()).then(
      (res) => {
        setList(res.data.data);
        dispatch(SetVoucher(res.data.data));
      },
      (err) => {
        console.log(err);
      }
    );
  };

  const handleInputChangeSearch = (name, value) => {
    setFormDataSearch({ ...formDataSearch, [name]: value });
  };

  const openUpdate = (id) => {
    setModalUpdate(true);
    setId(id);
  };
  const openAdd = () => {
    setModalCreate(true);
  };
  const openDetail = (id) => {
    setModalDetail(true);
    setId(id);
  };

  const columns = [
    {
      title: "STT",
      dataIndex: "stt",
      key: "stt",
      sorter: (a, b) => a.stt - b.stt,
    },
    {
      title: "Mã khuyến mãi",
      dataIndex: "code",
      key: "code",
      sorter: (a, b) => a.code.localeCompare(b.code),
    },
    {
      title: "Tên khuyến mãi",
      dataIndex: "name",
      key: "name",
      sorter: (a, b) => a.name.localeCompare(b.name),
    },
    {
      title: "Số lượng tồn",
      dataIndex: "quantity",
      align: "center",
      key: "quantity",
      sorter: (a, b) => a.quantity - b.quantity,
    },
    {
      title: "Giá trị giảm",
      dataIndex: "value",
      align: "center",
      key: "value",
      sorter: (a, b) => a.value - b.value,
    },
    {
      title: "Ngày bắt đầu",
      dataIndex: "startDate",
      align: "center",
      key: "startDate",
      sorter: (a, b) => a.startDate - b.startDate,
      render: (date) => dayjs(date).format("HH:mm:ss  DD-MM-YYYY "),
    },
    {
      title: "Ngày kết thúc",
      dataIndex: "endDate",
      align: "center",
      key: "endDate",
      sorter: (a, b) => a.endDate - b.endDate,
      render: (date) => dayjs(date).format("HH:mm:ss DD-MM-YYYY"),
    },
    {
      title: "Ngày cập nhật",
      dataIndex: "lastModifiedDate",
      key: "lastModifiedDate",
      align: "center",
      sorter: (a, b) => a.lastModifiedDate - b.lastModifiedDate,
      render: (date) => dayjs(date).format("HH:mm:ss DD-MM-YYYY"),
    },
    {
      title: "Trạng Thái",
      dataIndex: "status",
      key: "status",
      align: "center",
      render: (text) => {
        const genderClass =
          text === "DANG_SU_DUNG" ? "trangthai-sd" : "trangthai-ksd";
        return (
          <button className={`gender ${genderClass}`}>
            {text === "DANG_SU_DUNG" ? "Còn hạn" : "Hết hạn"}
          </button>
        );
      },
    },
    {
      title: "Hành động",
      dataIndex: "hanhDong",
      key: "hanhDong",
      align: "center",
      render: (text, record) => (
        <div style={{ display: "flex", justifyContent: "center", gap: "10px" }}>
          <Button
            type="primary"
            title="Chi tiết thể loại"
            style={{ backgroundColor: "#FF9900" }}
            onClick={() => openDetail(record.id)}
          >
            <FontAwesomeIcon icon={faEye} />
          </Button>
          <Button
            type="primary"
            title="Chỉnh sửa thể loại"
            style={{ backgroundColor: "green", borderColor: "green" }}
            onClick={() => openUpdate(record.id)}
          >
            <FontAwesomeIcon icon={faEdit} />
          </Button>
        </div>
      ),
    },
  ];

  const { Option } = Select;
  const fieldsSearch = [
    {
      name: "code",
      type: "text",
      label: "Mã khuyễn mãi",
      class: "input-search",
      placeholder: "Tìm kiếm",
    },
    // {
    //   name: "name",
    //   type: "text",
    //   label: "Tên khuyễn mãi",
    //   class: "input-search",
    //   placeholder: "Tìm kiếm",
    // },
    {
      name: "value",
      type: "number",
      label: "Giá trị giảm",
      class: "input-search",
      placeholder: "Tìm kiếm",
    },
    {
      name: "quantity",
      type: "number",
      label: "Số lượng tồn",
      class: "input-search",
      placeholder: "Tìm kiếm",
    },
    {
      name: "status",
      type: "select",
      label: "Trạng thái",
      options: [
        { value: "DANG_SU_DUNG", label: "Còn hạn" },
        { value: "KHONG_SU_DUNG", label: "Hết hạn" },
      ],
      class: "input-search",
      placeholder: "Tìm kiếm",
    },
    {
      name: "startDate",
      type: "date",
      label: "Từ ngày",
      class: "input-search",
      placeholder: "Tìm kiếm",
    },
    {
      name: "endDate",
      type: "date",
      label: "Đến ngày",
      class: "input-search",
      placeholder: "Tìm kiếm",
    },
  ];
  return (
    <div className="promotion">
      <h1 className="title-promotion">
        {" "}
        <FontAwesomeIcon icon={faKaaba} /> Quản lý khuyến mãi
      </h1>

      <div className="form-search">
        <h3>
          <FontAwesomeIcon icon={faFilter} /> Bộ lọc
        </h3>
        <hr></hr>

        <div className="row-search">
          <Row>
            <Col>
              {fieldsSearch
                .slice(0, Math.floor(fieldsSearch.length / 3))
                .map((field, index) => {
                  return (
                    <div key={index}>
                      <Form.Item
                        label={field.label}
                        labelCol={{ span: 7 }}
                        wrapperCol={{ span: 15 }}
                      >
                        {field.type === "number" && (
                          <InputNumber
                            className={field.class}
                            name={field.name}
                            placeholder={field.placeholder}
                            value={formDataSearch[field.name] || ""}
                            onChange={(value) =>
                              handleInputChangeSearch(field.name, value)
                            }
                            min="1"
                          />
                        )}
                        {field.type === "date" && (
                          <DatePicker
                            className={field.class}
                            name={field.name}
                            placeholder={field.placeholder}
                            value={formDataSearch[field.name]}
                            onChange={(value) => {
                              handleInputChangeSearch(field.name, value);
                            }}
                            format="DD-MM-YYYY"
                          />
                        )}

                        {field.type === "select" && (
                          <Select
                            className={field.class}
                            name={field.name}
                            placeholder={field.placeholder}
                            value={formDataSearch[field.name] || ""}
                            onChange={(value) =>
                              handleInputChangeSearch(field.name, value)
                            }
                          >
                            <Option value="">Tất cả</Option>
                            {field.options.map((option, optionIndex) => (
                              <Option key={optionIndex} value={option.value}>
                                {option.label}
                              </Option>
                            ))}
                          </Select>
                        )}
                        {field.type !== "date" &&
                          field.type !== "select" &&
                          field.type !== "number" && (
                            <Input
                              className={field.class}
                              name={field.name}
                              placeholder={field.placeholder}
                              value={formDataSearch[field.name] || ""}
                              onChange={(e) =>
                                handleInputChangeSearch(
                                  field.name,
                                  e.target.value
                                )
                              }
                            />
                          )}
                      </Form.Item>
                    </div>
                  );
                })}
            </Col>
            <Col>
              {fieldsSearch
                .slice(
                  Math.floor(fieldsSearch.length / 3),
                  Math.floor((2 * fieldsSearch.length) / 3)
                )
                .map((field, index) => {
                  return (
                    <div key={index}>
                      <Form.Item
                        label={field.label}
                        labelCol={{ span: 7 }}
                        wrapperCol={{ span: 15 }}
                      >
                        {field.type === "number" && (
                          <InputNumber
                            className={field.class}
                            name={field.name}
                            placeholder={field.placeholder}
                            value={formDataSearch[field.name] || ""}
                            onChange={(value) =>
                              handleInputChangeSearch(field.name, value)
                            }
                            min="1"
                          />
                        )}
                        {field.type === "date" && (
                          <DatePicker
                            className={field.class}
                            name={field.name}
                            placeholder={field.placeholder}
                            value={formDataSearch[field.name]}
                            onChange={(value) => {
                              handleInputChangeSearch(field.name, value);
                            }}
                            format="DD-MM-YYYY"
                          />
                        )}

                        {field.type === "select" && (
                          <Select
                            className={field.class}
                            name={field.name}
                            placeholder={field.placeholder}
                            value={formDataSearch[field.name] || ""}
                            onChange={(value) =>
                              handleInputChangeSearch(field.name, value)
                            }
                          >
                            <Option value="">Tất cả</Option>
                            {field.options.map((option, optionIndex) => (
                              <Option key={optionIndex} value={option.value}>
                                {option.label}
                              </Option>
                            ))}
                          </Select>
                        )}
                        {field.type !== "date" &&
                          field.type !== "select" &&
                          field.type !== "number" && (
                            <Input
                              className={field.class}
                              name={field.name}
                              placeholder={field.placeholder}
                              value={formDataSearch[field.name] || ""}
                              onChange={(e) =>
                                handleInputChangeSearch(
                                  field.name,
                                  e.target.value
                                )
                              }
                            />
                          )}
                      </Form.Item>
                    </div>
                  );
                })}
            </Col>
            <Col>
              {fieldsSearch
                .slice(Math.floor((2 * fieldsSearch.length) / 3))
                .map((field, index) => {
                  return (
                    <div key={index}>
                      <Form.Item
                        label={field.label}
                        labelCol={{ span: 7 }}
                        wrapperCol={{ span: 15 }}
                      >
                        {field.type === "number" && (
                          <InputNumber
                            className={field.class}
                            name={field.name}
                            placeholder={field.placeholder}
                            value={formDataSearch[field.name] || ""}
                            onChange={(value) =>
                              handleInputChangeSearch(field.name, value)
                            }
                            min="1"
                          />
                        )}
                        {field.type === "date" && (
                          <DatePicker
                            className={field.class}
                            name={field.name}
                            placeholder={field.placeholder}
                            value={formDataSearch[field.name]}
                            onChange={(value) => {
                              handleInputChangeSearch(field.name, value);
                            }}
                            format="DD-MM-YYYY"
                          />
                        )}

                        {field.type === "select" && (
                          <Select
                            className={field.class}
                            name={field.name}
                            placeholder={field.placeholder}
                            value={formDataSearch[field.name] || ""}
                            onChange={(value) =>
                              handleInputChangeSearch(field.name, value)
                            }
                          >
                            <Option value="">Tất cả</Option>
                            {field.options.map((option, optionIndex) => (
                              <Option key={optionIndex} value={option.value}>
                                {option.label}
                              </Option>
                            ))}
                          </Select>
                        )}
                        {field.type !== "date" &&
                          field.type !== "select" &&
                          field.type !== "number" && (
                            <Input
                              className={field.class}
                              name={field.name}
                              placeholder={field.placeholder}
                              value={formDataSearch[field.name] || ""}
                              onChange={(e) =>
                                handleInputChangeSearch(
                                  field.name,
                                  e.target.value
                                )
                              }
                            />
                          )}
                      </Form.Item>
                    </div>
                  );
                })}
            </Col>
          </Row>
        </div>

        <div className="reset-form-search">
          <Button
            title="Làm mới mục tìm kiếm"
            className="button-submit"
            onClick={resetFormSearch}
          >
            Làm mới
          </Button>
        </div>
      </div>

      <h3>
        {" "}
        <FontAwesomeIcon icon={faListAlt} /> Danh sách khuyến mãi{" "}
      </h3>
      <hr></hr>
      <div className="manager-promotion">
        <Button
          title="Thêm phiếu giảm giá"
          onClick={openAdd}
          className="button-add"
        >
          + Thêm
        </Button>

        <div className="promotion-table">
          <Table
            dataSource={updatedList}
            rowKey="id"
            columns={columns}
            pagination={{ pageSize: 5 }}
            rowClassName={(record, index) =>
              index % 2 === 0 ? "even-row" : "odd-row"
            }
          />
        </div>
      </div>

      {/* modal */}
      <CreateVoucherManagement
        modalCreate={modalCreate}
        setModalCreate={setModalCreate}
      />
      <UpdateVoucherManagement
        modalUpdate={modalUpdate}
        setModalUpdate={setModalUpdate}
        id={id}
      />
      <DetailVoucherManagement
        modalDetail={modalDetail}
        setModalDetail={setModalDetail}
        id={id}
      />
    </div>
  );
};

export default VoucherManagement;
