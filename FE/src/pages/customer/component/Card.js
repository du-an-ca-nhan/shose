import { LeftOutlined, RightOutlined } from "@ant-design/icons";
import { Col, InputNumber, Modal, Row } from "antd";
import dayjs from "dayjs";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { CartClientApi } from "./../../../api/customer/cart/cartClient.api";
import { ProductDetailClientApi } from "./../../../api/customer/productdetail/productDetailClient.api";
import "./style-card.css";

function CardItem({ item, index }) {
  const now = dayjs();
  const [modal, setModal] = useState(false);
  const [clickedIndex, setClickedIndex] = useState(-1);
  const nav = useNavigate();

  console.log(item);
  const [detailProduct, setDetailProduct] = useState({
    codeColor: "",
    idProductDetail: "",
    image: "",
    nameSize: "",
    nameProduct: "",
    price: 0,
    quantity: 0,
  });

  const idAccountLocal = sessionStorage.getItem("idAccount");
  const [quantity, setQuantity] = useState(1);
  const initialCartLocal = JSON.parse(localStorage.getItem("cartLocal")) || [];

  const [cartLocal, setCartLocal] = useState(initialCartLocal);

  useEffect(() => {
    localStorage.setItem("cartLocal", JSON.stringify(cartLocal));
  }, [cartLocal]);
  useEffect(() => {
    console.log(now.format("HH:mm:ss DD-MM-YYYY"));
    console.log(
      now.subtract(15, "day").format("DD-MM-YYYY"),
      dayjs.unix(item.createdDate / 1000).format("DD-MM-YYYY"),
      now.format("DD-MM-YYYY")
    );
  }, []);

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
        console.log(cartLocal);
        const exists = prev.find(
          (item) => item.idProductDetail === newCartItem.idProductDetail
        );
        if (!exists) {
          console.log("mới");
          return [...prev, newCartItem];
        } else {
          console.log("trùng");
          return prev.map((item) =>
            item.idProductDetail === newCartItem.idProductDetail
              ? { ...item, quantity: item.quantity + newCartItem.quantity }
              : item
          );
        }
      });
      window.location.href = "/cart";
      toast.success("Add cart không login", {
        autoClose: 3000,
      });
    } else {
      const newCartItem = {
        idAccount: idAccountLocal,
        idProductDetail: detailProduct.idProductDetail,
        price: detailProduct.price,
        quantity: quantity,
      };

      CartClientApi.addCart(newCartItem).then(
        (res) => {
          console.log(res.data.data);
          // setListProductDetailByCategory(res.data.data);
        },
        (err) => {
          console.log(err);
        }
      );
      window.location.href = "/cart";
      toast.success("Add cart có login!", {
        autoClose: 3000,
      });
    }
  };

  const getDetailProduct = (idProductDetail) => {
    console.log(idProductDetail);
    console.log(detailProduct);
    ProductDetailClientApi.getDetailProductOfClient(idProductDetail).then(
      (res) => {
        console.log(res.data.data);
        setDetailProduct(res.data.data);
      },
      (err) => {
        console.log(err);
      }
    );
    setModal(true);
  };

  const handleClickDetail = (idProductDetail) => {
    setClickedIndex(-1);
    getDetailProduct(idProductDetail);
  };
  const closeModal = () => {
    setModal(false);
    setClickedIndex(-1);
    setQuantity(1);
  };

  const formatMoney = (price) => {
    return (
      parseInt(price)
        .toString()
        .replace(/\B(?=(\d{3})+(?!\d))/g, ".") + " VND"
    );
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
  const itemTimestamp = dayjs.unix(item.createdDate / 1000);
  const nowTimestampReduce = now.subtract(15, "day");
  return (
    <>
      <div
        to="/detail"
        className="card-item"
        data-slick-index="-1"
        tabindex="0"
      >
        <div>
          <div
            className="link-card-item"
            onClick={() => nav(`/detail-product/${item.idProductDetail}`)}
          >
            <div className="box-img-product">
              <div
                style={{
                  backgroundImage: `url(${item.image.split(",")[0]})`,
                }}
                className="image-product"
              >
                {item.valuePromotion !== null && (
                  <div className="value-promotion">
                    Giảm {parseInt(item.valuePromotion)}%
                  </div>
                )}
                {nowTimestampReduce <= itemTimestamp && (
                  <div className="new-product">Mới</div>
                )}
              </div>
            </div>
            <div>
              <p className="name-product">
                {item.nameProduct} - [{item.nameSize}]
              </p>
            </div>
            <p className="price-product">{formatMoney(item.price)}</p>
          </div>
        </div>
        <div
          className="button-buy-now"
          onClick={() => {
            handleClickDetail(item.idProductDetail);
          }}
        >
          Mua ngay
        </div>
      </div>

      <Modal
        className="modal-detail-product"
        width={1000}
        onCancel={closeModal}
        open={modal}
        closeIcon={null}
        okButtonProps={{ style: { display: "none" } }}
        cancelButtonProps={{ style: { display: "none" } }}
      >
        <div className="modal-detail-product">
          <Row justify="center">
            <Col
              lg={{ span: 11, offset: 0 }}
              style={{ height: 500, display: "flex", alignItems: "center" }}
            >
              <LeftOutlined
                className="button-prev-card"
                onClick={previousImage}
              />
              <img
                className="img-detail-product"
                src={detailProduct.image.split(",")[currentImageIndex]}
                alt="..."
              />
              <RightOutlined className="button-next-card" onClick={nextImage} />
            </Col>
            <Col
              lg={{ span: 12, offset: 1 }}
              style={{ height: 500, paddingLeft: 20 }}
            >
              <h1>{detailProduct.nameProduct}</h1>
              <div className="price-product">
                {" "}
                Giá: {formatMoney(detailProduct.price)}
              </div>
              <div>
                <div>
                  ------------------------------------------------------------------------
                </div>
                <div>Màu:</div>
                <div className="list-color-detail">
                  <div
                    className="color-product"
                    key={index}
                    style={{
                      backgroundColor: detailProduct.codeColor,
                    }}
                  ></div>
                </div>
              </div>

              <div>
                ------------------------------------------------------------------------
              </div>
              <div>
                <div>Size:</div>
                <div className="list-size-product" tabIndex="0">
                  <div
                    className="size-product "
                    key={index}
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
              <div className="add-to-card">
                <InputNumber
                  className="input-quantity-card"
                  name="quantity"
                  type="number"
                  value={quantity}
                  disabled={detailProduct.quantity === 0}
                  min={1}
                  max={detailProduct.quantity}
                  onChange={(value) => setQuantity(value)}
                ></InputNumber>
                <div className="button-add-to-card" onClick={addToCard}>
                  Thêm vào Giỏ hàng
                </div>
              </div>
            </Col>
          </Row>
        </div>
      </Modal>
    </>
  );
}

export default CardItem;
