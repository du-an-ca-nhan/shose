import React, { useEffect, useState } from "react";
import "./../style-voucher.css";
import {
  Form,
  Input,
  Button,
  Select,
  Table,
  Modal,
  InputNumber,
  Popconfirm,
  DatePicker,
} from "antd";
import { VoucherApi } from "../../../../api/employee/voucher/Voucher.api";
import { CreateVoucher } from "../../../../app/reducer/Voucher.reducer";
import dayjs from "dayjs";
import utc from "dayjs/plugin/utc";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useAppDispatch, useAppSelector } from "../../../../app/hook";
dayjs.extend(utc);
function CreateVoucherManagement({ modalCreate, setModalCreate }) {
  const [formData, setFormData] = useState({});
  const [formErrors, setFormErrors] = useState({});
  const dispatch = useAppDispatch();
  const inputChange = (name, value) => {
    setFormData({ ...formData, [name]: value });
  };

  useEffect(() => {
  }, [formData]);
  const convertToLong = () => {
    const convertedFormData = { ...formData };
    if (formData.startDate) {
      convertedFormData.startDate = dayjs(formData.startDate).unix() * 1000;
    }
    if (formData.endDate) {
      convertedFormData.endDate = dayjs(formData.endDate).unix() * 1000;
    }
    return convertedFormData;
  };

  const handleSubmit = () => {
    
    const isFormValid =
    formData.code &&
      formData.name &&
      formData.value &&
      formData.quantity &&
      formData.startDate &&
      formData.endDate &&
      formData.startDate < formData.endDate;

    if (!isFormValid) {
      const errors = {
        code: !formData.code ? "Vui lòng nhập mã khuyễn mãi" : "",
        name: !formData.name ? "Vui lòng nhập tên khuyễn mãi" : "",
        value: !formData.value ? "Vui lòng nhập giá giảm" : "",
        startDate: !formData.startDate ? "Vui lòng chọn ngày bắt đầu" : "",
        quantity: !formData.quantity ? "Vui lòng nhập số lượng" : "",
        endDate: !formData.endDate
          ? "Vui lòng chọn ngày kết thúc"
          : formData.startDate >= formData.endDate
          ? "Ngày kết thúc phải lớn hơn ngày bắt đầu"
          : "",
      };
      setFormErrors(errors);
      return;
    }

    VoucherApi.create(convertToLong())
      .then((res) => {
        dispatch(CreateVoucher(res.data.data));
        toast.success("Thêm thành công!", {
          autoClose: 5000,
        });
        closeModal();
      })
      .catch((error) => {
        if (error.response.data.message === "BS-400") {
          toast.success("Vui lòng nhập đầy đủ!", {
            autoClose: 5000,
          });
          return;
        }
      });
  };
  const closeModal = () => {
    setModalCreate(false);
    setFormData([]);
    setFormErrors([]);
  };

  return (
    <div>
      <Modal
        title="Thêm khuyến mãi"
        visible={modalCreate}
        onCancel={closeModal}
        okButtonProps={{ style: { display: "none" } }}
        cancelButtonProps={{ style: { display: "none" } }}
      >
        <Form layout="vertical">
        <Form.Item
            label="Mã khuyến mãi"
            validateStatus={formErrors["code"] ? "error" : ""}
            help={formErrors["code"] || ""}
          >
            <Input
              name="code"
              className="input-create-voucher"
              placeholder="Tên khuyến mãi"
              value={formData["code"]}
              onChange={(e) => {
                inputChange("code", e.target.value);
              }}
            />
          </Form.Item>
          <Form.Item
            label="Tên khuyến mãi"
            validateStatus={formErrors["name"] ? "error" : ""}
            help={formErrors["name"] || ""}
          >
            <Input
              name="name"
              className="input-create-voucher"
              placeholder="Tên khuyến mãi"
              value={formData["name"]}
              onChange={(e) => {
                inputChange("name", e.target.value);
              }}
            />
          </Form.Item>
          <Form.Item
            label="Giá trị giảm"
            validateStatus={formErrors["value"] ? "error" : ""}
            help={formErrors["value"] || ""}
          >
            <InputNumber
              name="value"
              placeholder="Giá trị giảm"
              className="input-create-voucher"
              value={formData["value"]}
              onChange={(value) => {
                inputChange("value", value);
              }}
              min='1'
            />
          </Form.Item>
          <Form.Item
            label="Số lượng"
            validateStatus={formErrors["quantity"] ? "error" : ""}
            help={formErrors["quantity"] || ""}
          >
            <InputNumber
              name="quantity"
              placeholder="Số lượng"
              className="input-create-voucher"
              value={formData["quantity"]}
              onChange={(value) => {
                inputChange("quantity", value);
              }}
              min='1'
            />
          </Form.Item>
          <Form.Item
            label="Ngày bắt đầu"
            validateStatus={formErrors["startDate"] ? "error" : ""}
            help={formErrors["startDate"] || ""}
          >
            <DatePicker
              showTime
              name="startDate"
              placeholder="Ngày bắt đầu"
              className="input-create-voucher"
              value={formData["startDate"]}
              onChange={(value) => {
                inputChange("startDate", value);
              }}
            />
          </Form.Item>
          <Form.Item
            label="Ngày kết thúc"
            validateStatus={formErrors["endDate"] ? "error" : ""}
            help={formErrors["endDate"] || ""}
          >
            <DatePicker
              showTime
              name="endDate"
              placeholder="Ngày kết thúc"
              className="input-create-voucher"
              value={formData["endDate"]}
              onChange={(value) => {
                inputChange("endDate", value);
              }}
            />
          </Form.Item>

          <Form.Item>
            <Button onClick={closeModal}>Hủy</Button>
            <Popconfirm
              title="Thông báo"
              description={"Bạn có chắc chắn muốn thêm không ?"}
              onConfirm={() => {
                handleSubmit();
              }}
              okText="Có"
              cancelText="Không"
            >
              <Button className="button-submit" key="submit" title="Thêm">
                Thêm
              </Button>
            </Popconfirm>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

export default CreateVoucherManagement;
