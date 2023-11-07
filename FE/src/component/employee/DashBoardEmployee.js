import React, { useState } from "react";
import {
  BellOutlined,
  LogoutOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  SettingOutlined,
  UserOutlined,
} from "@ant-design/icons";
import {
  Layout,
  Menu,
  Button,
  theme,
  Dropdown,
  Badge,
  Modal,
  Form,
  Input,
  Checkbox,
} from "antd";
import "./style-dashboard-employee.css";
import { Link, useNavigate } from "react-router-dom";
import Logo from "../../assets/images/logo_admin.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faChartLine,
  faDumpsterFire,
  faMoneyBill1Wave,
  faTags,
  faUserGroup,
  faBoxesPacking,
  faPercent,
} from "@fortawesome/free-solid-svg-icons";
import SubMenu from "antd/es/menu/SubMenu";
import {
  deleteToken,
  deleteUserToken,
  getUserToken,
} from "../../helper/useCookies";
import { toast } from "react-toastify";
import { LoginApi } from "../../api/employee/login/Login.api";

const { Header, Sider, Content } = Layout;
const notificationCount = 5; // Số lượng thông báo chưa đọc

const DashBoardEmployee = ({ children }) => {
  const [collapsed, setCollapsed] = useState(false);
  const nav = useNavigate();
  const [form] = Form.useForm();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const showModal = () => {
    setIsModalOpen(true);
  };
  const handleOk = () => {
    form
      .validateFields()
      .then((values) => {
        return new Promise((resolve, reject) => {
          Modal.confirm({
            title: "Xác nhận",
            content: "Bạn có đồng ý thêm không?",
            okText: "Đồng ý",
            cancelText: "Hủy",
            onOk: () => resolve(values),
            onCancel: () => reject(),
          });
        });
      })
      .then((value) => {
        console.log(value);
        if (value.resetPassword !== value.newPassword) {
          toast.warning("Xác nhận lại mật khẩu sai.");
          return;
        }
        LoginApi.changePassword(value)
          .then((value) => {
            toast.success("Đổi mật khẩu thành công.");
            handleCancel();
          })
          .catch((err) => {});
      })
      .catch((error) => {});
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const {
    token: { colorBgContainer },
  } = theme.useToken();

  const userToken = JSON.parse(getUserToken());
  const handleLogout = () => {
    deleteToken();
    deleteUserToken();
    nav("/login-management");
    toast.success("Đăng xuất thành công");
  };

  const menu = (
    <Menu>
      <Menu.Item icon={<UserOutlined />} key="1">
        Thông tin người dùng
      </Menu.Item>
      <Menu.Item icon={<SettingOutlined />} key="2" onClick={showModal}>
        Đổi mật khẩu
      </Menu.Item>
      <Menu.Item icon={<LogoutOutlined />} key="3" onClick={handleLogout}>
        Đăng xuất
      </Menu.Item>
    </Menu>
  );

  return (
    <Layout className="layout-employee">
      <Sider trigger={null} collapsible width={225} collapsed={collapsed}>
        <Link to="/dashboard">
          <div className="logo">
            <img src={Logo} className="logo-content" alt="Logo" />
          </div>
        </Link>
        <div className="demo-logo-vertical" />
        <Menu theme="dark" mode="inline">
          <Menu.Item
            key="1"
            className="menu-item"
            icon={
              <FontAwesomeIcon icon={faChartLine} style={{ color: "white" }} />
            }
          >
            <Link to="/dashboard">Thống Kê</Link>
          </Menu.Item>
          <Menu.Item
            key="3"
            icon={
              <FontAwesomeIcon
                icon={faDumpsterFire}
                style={{ color: "white" }}
              />
            }
          >
            <Link to="/sale-counter">Bán Hàng Tại Quầy</Link>
          </Menu.Item>
          <Menu.Item
            key="4.1"
            icon={
              <FontAwesomeIcon
                icon={faMoneyBill1Wave}
                style={{ color: "white" }}
              />
            }
          >
            <Link to="/bill-management">Quản Lý Thu Chi</Link>
          </Menu.Item>

          <SubMenu
            key="6"
            icon={
              <FontAwesomeIcon
                icon={faBoxesPacking}
                style={{ color: "white" }}
              />
            }
            title="Quản Lý Sản Phẩm"
          >
            <Menu.Item key="6.0">
              <Link to="/product-management">Sản Phẩm</Link>
            </Menu.Item>
            <Menu.Item key="6.1">
              <Link to="/category-management">Thể Loại</Link>
            </Menu.Item>
            <Menu.Item key="6.2">
              <Link to="/sole-management">Đế Giày</Link>
            </Menu.Item>
            <Menu.Item key="6.3">
              <Link to="/brand-management">Thương Hiệu</Link>
            </Menu.Item>
            <Menu.Item key="6.4">
              <Link to="/material-management">Chất Liệu</Link>
            </Menu.Item>
          </SubMenu>

          <SubMenu
            key="7"
            icon={
              <FontAwesomeIcon icon={faUserGroup} style={{ color: "white" }} />
            }
            title="Quản Lý Tài Khoản"
          >
            <Menu.Item key="7.0">
              <Link to="/staff-management">Nhân Viên</Link>
            </Menu.Item>
            <Menu.Item key="7.1">
              <Link to="/customer-management">Khách Hàng</Link>
            </Menu.Item>
          </SubMenu>
          <Menu.Item
            key="8"
            icon={
              <FontAwesomeIcon icon={faPercent} style={{ color: "white" }} />
            }
          >
            <Link to="/promotion-management">Khuyến Mại</Link>
          </Menu.Item>
          <Menu.Item
            key="9"
            icon={<FontAwesomeIcon icon={faTags} style={{ color: "white" }} />}
          >
            <Link to="/voucher-management">Khuyến Mãi</Link>
          </Menu.Item>
          {/* <Menu.Item
            key="10"
            icon={<FontAwesomeIcon icon={faMap} style={{ color: "white" }} />}
          >
            <Link to="/address">Quản lý địa chỉ</Link>
          </Menu.Item> */}
        </Menu>
      </Sider>
      <Layout>
        <Header
          style={{
            padding: 0,
            background: colorBgContainer,
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
          className="header-layout"
        >
          <Button
            type="text"
            icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            onClick={() => setCollapsed(!collapsed)}
            style={{
              fontSize: "16px",
              width: 64,
              height: 64,
            }}
          />
          <div style={{ display: "flex", alignItems: "center" }}>
            <Badge count={notificationCount} style={{ backgroundColor: "red" }}>
              <Button type="text">
                <BellOutlined />
              </Button>
            </Badge>
            <span style={{ fontWeight: "bold", marginLeft: "20px" }}>
              {userToken.fullName}
            </span>
            <Dropdown overlay={menu} placement="bottomRight">
              <img
                src={userToken.avata}
                alt="User Avatar"
                style={{
                  width: "32px",
                  height: "32px",
                  borderRadius: "50%",
                  marginRight: "40px",
                  marginLeft: "20px",
                }}
              />
            </Dropdown>
          </div>
        </Header>
        <Content
          style={{
            paddingTop: 24,
            paddingBottom: 24,
            paddingLeft: 24,
            paddingRight: 7,
            minHeight: 280,
            borderRadius: "15px",
            overflowY: "auto",
          }}
        >
          {children}
        </Content>
      </Layout>
      <Modal
        title="Đổi mật khẩu"
        open={isModalOpen}
        onCancel={handleCancel}
        footer={[
          <Button key="cancel" onClick={handleCancel}>
            Hủy
          </Button>,
          <Button key="submit" type="primary" onClick={handleOk}>
            cập nhập
          </Button>,
        ]}
      >
        <Form
          style={{
            maxWidth: 600,
            marginTop: "30px",
          }}
          name="validateOnly"
          layout="vertical"
          form={form}
        >
          <Form.Item
            name="password"
            label="Mật khẩu cũ : "
            rules={[
              {
                required: true,
                message: "Vui lòng nhập mật khẩu cũ.",
              },
            ]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Mật khẩu mới :"
            name="resetPassword"
            rules={[
              {
                required: true,
                message: "Vui lòng nhập mật khẩu mới.",
              },
            ]}
          >
            <Input.Password />
          </Form.Item>
          <Form.Item
            label="Xác nhận lại mật khẩu :"
            name="newPassword"
            rules={[
              {
                required: true,
                message: "Vui lòng nhập lại mật khẩu mới.",
              },
            ]}
          >
            <Input.Password />
          </Form.Item>
        </Form>
      </Modal>
    </Layout>
  );
};

export default DashBoardEmployee;
