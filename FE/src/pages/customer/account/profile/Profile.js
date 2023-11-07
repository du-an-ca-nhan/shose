import React, { useEffect, useRef, useState } from "react";
import "./style-profile.css"
import { DatePicker, Form, Radio } from "antd";
import avatar from "../../../../assets/images/trending_banner03.jpg";
import { AccountClientApi } from "../../../../api/customer/account/accountClient.api";
import dayjs from "dayjs";
import { toast } from "react-toastify";
import { AccountApi } from "../../../../api/employee/account/account.api";
function Profile() {
    const [formInfo, setFormInfo] = useState({});
    const [showImage, setShowImage] = useState('');

    const fileInputRef = useRef(null);
    const id = sessionStorage.getItem("idAccount")
    useEffect(() => {
        AccountClientApi.getById(id).then((res) => {
            const data = res.data.data.user
            const formData = new FormData();

            setFormInfo({
                id: data.id,
                fullName: data.fullName,
                dateOfBirth: dayjs(data.dateOfBirth) ,
                phoneNumber: data.phoneNumber,
                email: data.email,
                gender: data.gender,
            })

            console.log(formData);
            setShowImage(data.avata)
            console.log(dayjs(res.data.data.user.dateOfBirth).format("MM/DD/YYYY"));
        })
    }, [])

    useEffect(() => {
        console.log(formInfo);
    }, [formInfo])
    const handleFormInfo = (name, item) => {
        setFormInfo((prev) => ({
            ...prev,
            [name]: item,
        }));
    };
    const handleButtonClick = () => {
        fileInputRef.current.click(); // Khi click vào phần tử tùy chỉnh, mở hộp thoại chọn tệp
    };

    const handleFileChange = (file) => {
        console.log(file);
        if (file) {
            if (file.type !== "image/jpeg" && file.type !== "image/png") {
                toast.error("Bạn chỉ có thể tải lên tệp JPG/PNG!");
                return;
            }
            const reader = new FileReader();
            reader.onload = (event) => {
                const imageDataURL = event.target.result;
                setShowImage(imageDataURL);
                handleFormInfo('avata', file)
            };

            reader.readAsDataURL(file);
        } else {
            handleFormInfo('avata', '')
        }
    };
    const convertToLong = () => {
        const convertedFormData = { ...formInfo };
        if (formInfo.dateOfBirth) {
          convertedFormData.dateOfBirth = dayjs(formInfo.dateOfBirth).unix() * 1000;
        }
        return convertedFormData;
      };
    const handleUpdateInfoUser = () => {
        AccountClientApi.updateInfoUser(convertToLong()).then((res) => {
            toast.success("Cập nhập thành công");
        })
    }
    return (<React.Fragment>
        <div className="profile-account">
            <div className="title-profile-account">
                <p className="title-profile">Hồ sơ của tôi</p>
                <p className="sub-title-profile"> Quản lý thông tin để bảo mật tài khoản</p>
            </div>
            <div className="content-profile-account">
                <div className="form-profile-account">

                    <Form
                        labelCol={{
                            span: 5,
                        }}

                    >
                        <Form.Item
                            label="Họ và tên">

                            <input
                                value={formInfo["fullName"]}
                                className="input-info-profile" placeholder="Điền họ và tên"
                                onChange={(e) => handleFormInfo("fullName", e.target.value)}
                            />
                        </Form.Item>
                        <Form.Item
                            label="Email">

                            <input
                                value={formInfo["email"]}
                                className="input-info-profile" placeholder="Điền email"
                                onChange={(e) => handleFormInfo("email", e.target.value)}
                            />

                        </Form.Item>
                        <Form.Item
                            label="Số điện thoại">
                            <input
                                value={formInfo["phoneNumber"]}
                                className="input-info-profile" placeholder="Điền số điện thoại"
                                onChange={(e) => handleFormInfo("phoneNumber", e.target.value)}
                            />

                        </Form.Item>
                        <Form.Item
                            label="Giới tính"

                        >
                            <Radio.Group
                                style={{ display: "flex" }}
                                className="box-input-info-profile"
                                value={formInfo["gender"]}
                                name="gender"
                                onChange={(e) => handleFormInfo("gender", e.target.value)}

                            >
                                <Radio checked={formInfo["gender"] === true} value={true}>
                                    Nam
                                </Radio>
                                <Radio checked={formInfo["gender"] === false} value={false}>Nữ</Radio>
                            </Radio.Group>

                        </Form.Item>
                        <Form.Item
                            label="Ngày sinh">
                            <input
                                value= {dayjs(formInfo["dateOfBirth"]).format("YYYY-MM-DD")}
                                className="input-date-of-birth-profile" type="date"
                                onChange={(e) => handleFormInfo("dateOfBirth",e.target.value)}
                             
                            />
                        </Form.Item>

                    </Form>

                    <div style={{ display: "flex", justifyContent: "center" }}>
                        <div className="button-update-profile" onClick={handleUpdateInfoUser}>
                            Cập nhập
                        </div>
                    </div>
                </div>

                <div className="form-image-account">
                    <img src={showImage} alt="..." className="img-form-edit-profile" />
                    <input
                        type='file'
                        ref={fileInputRef}
                        style={{ display: 'none' }} // Ẩn input
                        onChange={(e) => handleFileChange(e.target.files[0])}
                    />
                    <div style={{ display: "flex", justifyContent: "center" }}>
                        <p className="button-upload-image-profile" onClick={handleButtonClick}>Chọn ảnh</p>
                    </div>
                </div>

            </div>
        </div>
    </React.Fragment>);
}

export default Profile;