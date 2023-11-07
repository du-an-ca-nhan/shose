import { LeftOutlined, RightOutlined } from "@ant-design/icons";
import { Col, InputNumber, Row } from "antd";
import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { CartClientApi } from "../../../api/customer/cart/cartClient.api";
import { useParams } from "react-router";
import { ProductDetailClientApi } from "../../../api/customer/productdetail/productDetailClient.api";
import "./style-detail-product.css"
import dayjs from "dayjs";

function DetailProduct() {
    const idAccountLocal = sessionStorage.getItem("idAccount");
    const id = useParams()
    const [detailProduct, setDetailProduct] = useState({
        codeColor: "",
        idProductDetail: "",
        image: "",
        nameSize: "",
        nameProduct: "",
        price: 0,
        quantity: 0,
    });
    const initialCartLocal = JSON.parse(localStorage.getItem("cartLocal")) || [];

    const [cartLocal, setCartLocal] = useState(initialCartLocal);

    useEffect(() => {
        localStorage.setItem("cartLocal", JSON.stringify(cartLocal));
    }, [cartLocal]);
    const [quantity, setQuantity] = useState(1);
    useEffect(() => {
        console.log(id.id);
        getDetailProduct(id.id)
    }, [])
    const getDetailProduct = (idProductDetail) => {

        ProductDetailClientApi.getDetailProductOfClient(idProductDetail).then(
            (res) => {
                console.log(res.data.data);
                setDetailProduct(res.data.data);
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
    const addToCard = () => {
        if (detailProduct.quantity === 0) {
            toast.error("Sản phẩm đã hết hàng", {
                autoClose: 3000,
            });
            return;
        }
        if (idAccountLocal === null) {
            const newCartItem = {
                idProductDetail: detailProduct.idProductDetail,
                image: detailProduct.image,
                price: detailProduct.price,
                quantity: quantity,
                nameProduct: detailProduct.nameProduct,
                codeColor: detailProduct.codeColor,
                nameSize: detailProduct.nameSize,
            };

            setCartLocal((prev) => {
                const exists = prev.find(
                    (item) => item.idProductDetail === newCartItem.idProductDetail
                );
                if (!exists) {
                    return [...prev, newCartItem];
                } else {
                    return prev.map((item) =>
                        item.idProductDetail === newCartItem.idProductDetail
                            ? { ...item, quantity: item.quantity + newCartItem.quantity }
                            : item
                    );
                }
            });
            window.location.href = "/cart";
        } else {
            const newCartItem = {
                idAccount: idAccountLocal,
                idProductDetail: detailProduct.idProductDetail,
                price: detailProduct.price,
                quantity: quantity,
            };

            CartClientApi.addCart(newCartItem).then(
                (res) => {
                    window.location.href = "/home";

                },
                (err) => {
                    console.log(err);
                }
            );

        }
    };

    const [currentImageIndex, setCurrentImageIndex] = useState(0);

    useEffect(() => {
        // Thiết lập một interval để tự động chuyển ảnh sau một khoảng thời gian
        const intervalId = setInterval(() => {
            nextImage();
        }, 3000); // Thay đổi số 3000 để đặt khoảng thời gian chuyển ảnh (tính bằng mili giây)

        // Xóa interval khi component unmount để tránh lỗi memory leak
        return () => clearInterval(intervalId);
    }, [currentImageIndex]);
    const previousImage = () => {
        if (currentImageIndex === 0) {
            // Nếu đang ở ảnh đầu tiên, chuyển đến ảnh cuối cùng
            setCurrentImageIndex(detailProduct.image.split(",").length - 1);
        } else {
            setCurrentImageIndex(currentImageIndex - 1);
        }
    };

    const nextImage = () => {
        if (currentImageIndex === detailProduct.image.split(",").length - 1) {
            // Nếu đang ở ảnh cuối cùng, chuyển đến ảnh đầu tiên
            setCurrentImageIndex(0);
        } else {
            setCurrentImageIndex(currentImageIndex + 1);
        }
    };
    return (<React.Fragment>
        <div className="box-detail-product">
            <Row>
                <Col
                    lg={{ span: 14, offset: 5 }}
                    style={{ display: "flex", justifyContent: "center", padding: 50 }}

                >
                    <div className="box-image-pd">
                        <div style={{ position: "relative", display: "flex", alignItems: "center" }}>
                            <LeftOutlined
                                className="button-prev-card-pd"
                                onClick={previousImage}
                            />
                            <img
                                className="img-detail-product-pd"
                                src={detailProduct.image.split(",")[currentImageIndex]}
                                alt="..."
                            />
                            <RightOutlined className="button-next-card-pd" onClick={nextImage} />
                        </div>


                        <div className="box-info-detail">
                            <div className="box-title-info-detail">
                                THÔNG TIN CHI TIẾT
                            </div>

                            <ul className="ul-info-pd" style={{ padding: 20 }}>
                                <li className="li-info-pd">
                                    <span className="title-info-pd"> Tên sản phẩm</span>
                                    <span className="content-info-pd"> {detailProduct.nameProduct}</span>
                                </li>
                                <li className="li-info-pd">
                                    <span className="title-info-pd"> Giá</span>
                                    <span className="content-info-pd"> {formatMoney(detailProduct.price)}</span>
                                </li>
                                <li className="li-info-pd">
                                    <span className="title-info-pd"> Thương hiệu</span>
                                    <span className="content-info-pd"> {detailProduct.nameBrand}</span>
                                </li>
                                <li className="li-info-pd">
                                    <span className="title-info-pd"> Loại sản phẩm</span>
                                    <span className="content-info-pd"> {detailProduct.nameCategory}</span>
                                </li>
                                <li className="li-info-pd">
                                    <span className="title-info-pd"> Chất liệu</span>
                                    <span className="content-info-pd"> {detailProduct.nameMaterial}</span>
                                </li>
                                <li className="li-info-pd">
                                    <span className="title-info-pd"> Kích thước</span>
                                    <span className="content-info-pd"> {detailProduct.nameSize}</span>
                                </li>
                                <li className="li-info-pd">
                                    <span className="title-info-pd"> Đế giày</span>
                                    <span className="content-info-pd"> {detailProduct.nameSole}</span>
                                </li>

                            </ul>
                        </div>
                    </div>
                    <div className="box-info-pd" >
                        <div className="name-pd">{detailProduct.nameProduct}</div>
                        <div className="price-product-pd">
                            {" "}
                            Giá: {formatMoney(detailProduct.price)}
                        </div>
                        <div className="box-color-pd">

                            <div>Màu:</div>
                            <div className="list-color-detail-pd">
                                <div
                                    className="color-product-pd"

                                    style={{
                                        backgroundColor: detailProduct.codeColor,
                                    }}
                                ></div>
                            </div>
                        </div>

                        <div className="box-size-pd">
                            <div>Size:</div>
                            <div className="list-size-product-pd" tabIndex="0">
                                <div
                                    className="size-product-pd "
                                    tabIndex="0"
                                    style={{ border: "1px solid black" }}
                                >
                                    {detailProduct.nameSize}
                                </div>
                            </div>
                        </div>

                        <div>
                            <div style={{ marginBottom: "10px", color: "black" }}>
                                Số lượng tồn:{" "}
                                <span style={{ color: "#ff4400" }}>
                                    {detailProduct.quantity} sản phẩm
                                </span>
                            </div>
                        </div>
                        <div className="add-to-card-pd">
                            <InputNumber
                                className="input-quantity-card-pd"
                                name="quantity"
                                type="number"
                                disabled={detailProduct.quantity === 0}
                                min={1}
                                defaultValue={1}
                                max={detailProduct.quantity}
                                onChange={(value) => setQuantity(value)}
                            ></InputNumber>
                            <div className="button-add-to-card-pd" onClick={addToCard}>
                                Thêm vào Giỏ hàng
                            </div>
                        </div>
                    </div>
                </Col>
            </Row>
        </div>
    </React.Fragment>);
}

export default DetailProduct;