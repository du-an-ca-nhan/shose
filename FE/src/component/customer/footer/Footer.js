import { Col } from "antd";
import "./style-footer.css";
import logoFooter from "./../../../assets/images/logo_admin.png";
import { Link } from "react-router-dom";
import {
  FacebookOutlined,
  GoogleOutlined,
  InstagramOutlined,
  LinkedinOutlined,
  TwitterOutlined,
  YoutubeOutlined,
} from "@ant-design/icons";
import ReactPlayer from "react-player";
import { Row } from "antd/es";
function Footer() {
  return (
    <>
      <div className="footer">
        <Row>
          <Col className="header-footer" lg={{ span: 16, offset: 4 }}>
            <Link>
              <img style={{ width: 120 }} src={logoFooter} alt="..." />
            </Link>
          </Col>
        </Row>
        <Row>
          <Col className="content-footer" lg={{ span: 16, offset: 4 }}>
            <div className="category-footer">
              <div style={{ display: "flex" }}>
                <Link className="title-content-footer">TRANG CHỦ</Link>
                <Link className="title-content-footer">SẢN PHẨM</Link>
                <Link className="title-content-footer">VỀ CHÚNG TÔI</Link>
                <Link className="title-content-footer">BLOG</Link>
                <Link className="title-content-footer" to="/policy">
                  NỘI DUNG CHÍNH SÁCH
                </Link>
                <Link className="title-content-footer">LIÊN HỆ CHÚNG TÔI</Link>
              </div>
              <div style={{ marginLeft: "auto" }}>
                <Link className="icon-content-footer">
                  <FacebookOutlined />
                </Link>
                <Link className="icon-content-footer">
                  <TwitterOutlined />
                </Link>
                <Link className="icon-content-footer">
                  <YoutubeOutlined />
                </Link>
                <Link className="icon-content-footer">
                  <InstagramOutlined />
                </Link>
                <Link className="icon-content-footer">
                  <LinkedinOutlined />
                </Link>
                <Link className="icon-content-footer">
                  <GoogleOutlined />
                </Link>
              </div>
            </div>
            <Row justify={"space-between"}>
              <Col span={12}>
                <div className="category-footer1">
                  © 2023 <Link style={{ color: "#ff4400" }}>BEESNEAKER</Link>.
                  ALL RIGHTS RESERVED | PH (+09) 71833489
                </div>
                <div className="category-footer1">
                  GPĐKKD: 0315508125 do Sở KH và ĐT TPHCM cấp ngày 30/01/2019
                  Đăng ký thay đổi lần thứ 7, ngày 07 tháng 06 năm 2021
                </div>
                <div className="category-footer1">
                  Hotline: 0906.880.960 (9h-18h từ Thứ 2 đến Thứ 6) Email:
                  beesneakerfpthn@gmail.com
                </div>
              </Col>
              <Col>
                <ReactPlayer
                  url="https://youtu.be/GdlSWFyYA8s?si=n-bxZFROzNHAUr5s"
                  width={"250px"}
                  height={"150px"}
                />
              </Col>
            </Row>
          </Col>
        </Row>
      </div>
    </>
  );
}

export default Footer;
