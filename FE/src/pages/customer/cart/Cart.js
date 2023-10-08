import imgShoe from "./../../../assets/images/third_slider_img03.png";
import logoHidden from "./../../../assets/images/logo_client.png";
import imgShoe1 from "./../../../assets/images/trending_banner02.jpg";
import "./style-cart.css";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";
import { useCart } from "./CartContext";
import { useState, useEffect } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faHeader,
  faHeart,
  faHeartBroken,
  faHeartCrack,
  faHeartPulse,
  faTags,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";
import {
  Row,
  Col,
  Input,
  Checkbox,
  InputNumber,
  Modal,
  Button,
  Radio,
  Table,
  Tooltip,
} from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import { ProductDetailClientApi } from "./../../../api/customer/productdetail/productDetailClient.api";
import { VoucherClientApi } from "./../../../api/customer/voucher/voucherClient.api";
import { CartClientApi } from "./../../../api/customer/cart/cartClient.api";
import { CartDetailClientApi } from "./../../../api/customer/cartdetail/cartDetailClient.api";
import dayjs from "dayjs";
import { Header } from "antd/es/layout/layout";

function Cart() {
  const idAccountLocal = localStorage.getItem("idAccount");
  const cartLocal = JSON.parse(localStorage.getItem("cartLocal"));
  const [cart, setCart] = useState([]);
  const [chooseItemCart, setChooseItemCart] = useState([]);
  const [totalPrice, setTotalPrice] = useState(0);
  const [totalBill, setTotalBill] = useState(0);
  const { updateTotalQuantity } = useCart();
  const [modalSize, setModalSize] = useState(false);
  const [modalVoucher, setModalVoucher] = useState(false);
  const [listSize, setListSize] = useState([]);
  const [listVoucher, setListVoucher] = useState([]);
  const [detailProductNew, setDetailProductNew] = useState({});
  const [detailProductOld, setDetailProductOld] = useState({});
  const [selectAllChecked, setSelectAllChecked] = useState(false);
  const [clickedIndex, setClickedIndex] = useState(-1);
  const [formSearch, setFormSearch] = useState("");
  const [voucher, setVoucher] = useState({
    value: 0,
    idVoucher: "",
  });
  const [formChangeSize, setFormChangeSise] = useState({});
  const [selectedItem, setSelectedItem] = useState({});

  const handleRadioChange = (item) => {
    setSelectedItem((prev) => ({
      ...prev,
      idVoucher: item.id,
      value: item.value,
    }));
  };
  const submitVoucher = () => {
    setModalVoucher(false);
    setVoucher(selectedItem);
  };
  useEffect(() => {
    console.log(idAccountLocal);
    if (idAccountLocal === null) {
      setCart(cartLocal);
    } else {
      getListCart(idAccountLocal);
      setTotalPrice(0);
    }
  }, []);
  // useEffect(() => {
  //   console.log(selectedItem);
  // }, [selectedItem]);
  useEffect(() => {
    console.log(listSize);
  }, [listSize]);
  useEffect(() => {
    console.log(listVoucher);
  }, [listVoucher]);

  useEffect(() => {
    if (idAccountLocal === null) {
      localStorage.setItem("cartLocal", JSON.stringify(cart));
      const total = cart.reduce((acc, item) => acc + item.quantity, 0);
      updateTotalQuantity(total);
    } else {
      getQuantityInCart(idAccountLocal);
    }
    console.log(cart);
  }, [cart]);

  useEffect(() => {
    console.log("item của bill", chooseItemCart);
  }, [chooseItemCart]);
  useEffect(() => {
    console.log("new", detailProductNew);
  }, [detailProductNew]);
  useEffect(() => {
    console.log("old", detailProductOld);
  }, [detailProductOld]);
  useEffect(() => {
    console.log(formSearch);
  }, [formSearch]);
  useEffect(() => {
    setTotalBill(totalPrice - voucher.value);
  }, [totalPrice]);
  useEffect(() => {
    setTotalBill(totalPrice - voucher.value);
    sessionStorage.setItem("voucher", JSON.stringify(voucher));
    console.log(voucher);
  }, [voucher]);
  useEffect(() => {
    console.log(formChangeSize);
  }, [formChangeSize]);

  const getQuantityInCart = (id) => {
    CartClientApi.quantityInCart(id).then(
      (res) => {
        const respone = res.data.data;
        updateTotalQuantity(respone);
      },
      (err) => {
        console.log(err);
      }
    );
  };
  const getListCart = (id) => {
    CartClientApi.listCart(id).then(
      (res) => {
        const respone = res.data.data;
        console.log(respone);
        setCart(respone);
      },
      (err) => {
        console.log(err);
      }
    );
  };
  const handleSelectAllChange = () => {
    if (selectAllChecked) {
      // Nếu checkbox "Select All" đã được chọn, bỏ chọn tất cả các sản phẩm
      setChooseItemCart([]);
      setTotalPrice(0);
    } else {
      // Nếu checkbox "Select All" chưa được chọn, chọn tất cả các sản phẩm
      const allItems = cart.map((item) => ({
        nameProduct: item.nameProduct,
        idProductDetail: item.idProductDetail,
        price: item.price,
        quantity: item.quantity,
        nameSize: item.nameSize,
        image: item.image,
      }));
      setChooseItemCart(allItems);
      const totalPrice = cart.reduce(
        (total, item) => total + parseInt(item.price) * item.quantity,
        0
      );
      setTotalPrice(totalPrice);
    }
    setSelectAllChecked((prevChecked) => !prevChecked);
  };
  const closeModalSize = () => {
    setModalSize(false);
    setClickedIndex(-1);
    setDetailProductOld({});
  };
  const closeModalVoucher = () => {
    setModalVoucher(false);
  };
  const openListSize = (item) => {
    setDetailProductOld(item);
    setModalSize(true);
    ProductDetailClientApi.getListSizeCart(item.idProduct, item.codeColor).then(
      (res) => {
        const exist = res.data.data.filter(
          (itemSize) => itemSize.nameSize !== item.nameSize
        );
        setListSize(exist);
      },
      (err) => {
        console.log(err);
      }
    );
  };
  const openListVoucher = (idAcccount) => {
    if (chooseItemCart.length === 0) {
      setVoucher(setDefaultVoucher);
      toast.warning("Vui lòng chọn sản phẩm trước khi nhập khuyến mại!", {
        autoClose: 3000,
      });
    } else {
      setModalVoucher(true);
      VoucherClientApi.getListVoucherByAccount(idAcccount).then(
        (res) => {
          setListVoucher(res.data.data);
          console.log(listVoucher);
        },
        (err) => {
          console.log(err);
        }
      );
    }
  };
  const getDetailProduct = (index, item) => {
    setClickedIndex(index);
    if (idAccountLocal === null) {
      ProductDetailClientApi.getDetailProductOfClient(
        item.idProduct,
        item.codeColor,
        item.nameSize
      ).then(
        (res) => {
          console.log(res.data.data);
          const newCartItem = {
            idProductDetail: res.data.data.idProductDetail,
            name: res.data.data.nameProduct,
            image: res.data.data.image,
            price: res.data.data.price,
            quantity: detailProductOld.quantity,
            idProduct: res.data.data.idProduct,
            codeColor: res.data.data.codeColor,
            nameSize: res.data.data.nameSize,
          };
          setDetailProductNew(newCartItem);
        },
        (err) => {
          console.log(err);
        }
      );
    } else {
      ProductDetailClientApi.getDetailProductOfClient(
        item.idProduct,
        item.codeColor,
        item.nameSize
      ).then(
        (res) => {
          console.log(res.data.data);
          const newCartItem = {
            idCartDetail: detailProductOld.idCartDetail,
            price: res.data.data.price,
            quantity: detailProductOld.quantity,
            idProductDetail: res.data.data.idProductDetail,
          };
          setFormChangeSise(newCartItem);
        },
        (err) => {
          console.log(err);
        }
      );
    }
  };
  const changeSize = () => {
    if (listSize.length !== 0) {
      if (idAccountLocal === null) {
        if (Object.keys(detailProductNew).length === 0) {
          toast.success("Vui lòng chọn size!", {
            autoClose: 3000,
          });
        } else {
          setCart((prev) => [
            ...prev.filter(
              (item) =>
                item.idProductDetail !== detailProductOld.idProductDetail
            ),
            detailProductNew,
          ]);
          const itemNewChoose = chooseItemCart.filter(
            (item) => item.idProductDetail !== detailProductOld.idProductDetail
          );
          console.log(itemNewChoose);
          setChooseItemCart(itemNewChoose);
          const total = itemNewChoose.reduce((acc, item) => {
            const itemTotal = item.price * item.quantity;
            return acc + itemTotal;
          }, 0);
          setTotalPrice(total);
          closeModalSize();
        }
      } else {
        if (Object.keys(formChangeSize).length === 0) {
          toast.success("Vui lòng chọn size!", {
            autoClose: 3000,
          });
        } else {
          console.log(formChangeSize);
          CartDetailClientApi.changeSize(formChangeSize).then(
            (res) => {
              getListCart(idAccountLocal);
            },
            (err) => {
              console.log(err);
            }
          );
          const itemNewChoose = chooseItemCart.filter(
            (item) => item.idProductDetail !== detailProductOld.idProductDetail
          );
          console.log(itemNewChoose);
          setChooseItemCart(itemNewChoose);
          const total = itemNewChoose.reduce((acc, item) => {
            const itemTotal = item.price * item.quantity;
            return acc + itemTotal;
          }, 0);
          setTotalPrice(total);
          closeModalSize();
        }
      }
    } else {
      closeModalSize();
    }
  };

  const formatMoney = (price) => {
    return (
      parseInt(price)
        .toString()
        .replace(/\B(?=(\d{3})+(?!\d))/g, ".") + " VND"
    );
  };
  const handleInputChange = (value) => {
    setFormSearch(value);
  };
  const deleteItemCart = (itemOld) => {
    Modal.confirm({
      title: "Xác nhận xóa",
      content: "Bạn có chắc chắn muốn xóa sản phẩm này?",
      okText: "Xóa",
      okType: "danger",
      cancelText: "Hủy",
      onOk() {
        const exist = chooseItemCart.find(
          (item) => item.idProductDetail === itemOld.idProductDetail
        );
        if (!exist) {
          if (idAccountLocal === null) {
            const updatedCart = cart.filter(
              (item) => item.idProductDetail !== itemOld.idProductDetail
            );
            setCart(updatedCart);
          } else {
            CartDetailClientApi.deleteCartDetail(itemOld.idCartDetail).then(
              (res) => {
                getListCart(idAccountLocal);
              },
              (err) => {
                console.log(err);
              }
            );
          }
        } else {
          toast.success("Sản phẩm đang được chọn!", {
            autoClose: 3000,
          });
        }
      },
    });
  };
  const changeQuantity = (itemOld, value) => {
    chooseItemCart.map((item) => {
      if (item.idProductDetail === itemOld.idProductDetail) {
        if (itemOld.quantity < value) {
          setTotalPrice(totalPrice + parseInt(itemOld.price));
        } else {
          setTotalPrice(totalPrice - parseInt(itemOld.price));
        }
      }
    });

    if (idAccountLocal === null) {
      const updatedCart = cart.map((item) => {
        if (item.idProductDetail === itemOld.idProductDetail) {
          return { ...item, quantity: value };
        }
        return item;
      });
      console.log("updateCart", updatedCart);
      setCart(updatedCart);
    } else {
      const formChange = {
        idCartDetail: itemOld.idCartDetail,
        quantity: value,
      };
      console.log("formChange", formChange);
      CartDetailClientApi.changeQuantity(formChange).then(
        (res) => {
          getListCart(idAccountLocal);
        },
        (err) => {
          console.log(err);
        }
      );
    }
  };
  const chooseCartForBill = (item, value) => {
    const itemDetail = {
      nameProduct: item.nameProduct,
      idProductDetail: item.idProductDetail,
      price: item.price,
      quantity: item.quantity,
      nameSize: item.nameSize,
      image: item.image,
    };
    if (value) {
      setChooseItemCart([...chooseItemCart, itemDetail]);
      setTotalPrice(totalPrice + parseInt(item.price) * item.quantity);
    } else {
      setChooseItemCart(
        chooseItemCart.filter(
          (itemId) => itemId.idProductDetail !== item.idProductDetail
        )
      );
      setTotalPrice(totalPrice - parseInt(item.price) * item.quantity);
    }
  };
  const setDefaultVoucher = {
    value: 0,
    idVoucher: "",
  };
  const getVoucher = (code) => {
    console.log(chooseItemCart.length);
    if (chooseItemCart.length === 0) {
      setVoucher(setDefaultVoucher);
      toast.warning("Vui lòng chọn sản phẩm trước khi nhập khuyến mại!", {
        autoClose: 3000,
      });
    } else {
      if (code.trim() === "") {
        setVoucher(setDefaultVoucher);
        toast.warning("Bạn chưa nhập mã khuyễn mãi!", {
          autoClose: 3000,
        });
      } else {
        VoucherClientApi.getByCode(code.trim()).then(
          (res) => {
            const voucher = res.data.data;
            if (voucher === null) {
              setVoucher(setDefaultVoucher);
              toast.warning("Khuyến mãi không tồn tại!", {
                autoClose: 3000,
              });
            } else {
              toast.success("Áp dụng khuyến mại thành công .", {
                autoClose: 3000,
              });
              setModalVoucher(false);
              setVoucher((prev) => ({
                ...prev,
                value: res.data.data.value,
                idVoucher: res.data.data.id,
              }));
            }
          },
          (err) => {
            console.log(err);
          }
        );
        console.log(voucher);
      }
    }
  };
  const payment = () => {
    if (chooseItemCart.length === 0) {
      toast.success("Quý khách chưa chọn sản phẩm ạ!", {
        autoClose: 3000,
      });
    } else {
      if (idAccountLocal === null) {
        window.location.href = "/payment";
        sessionStorage.setItem("bill", JSON.stringify(chooseItemCart));
      } else {
        window.location.href = "/payment-acc";
        sessionStorage.setItem("bill", JSON.stringify(chooseItemCart));
      }
    }
  };
  const modalProps = {
    draggable: false, // Tắt tính năng kéo thả
  };

  // tessment
  const columns = [
    {
      title: "Name",
      dataIndex: "name",
      render: (text) => <a>{text}</a>,
    },
    {
      title: "Age",
      dataIndex: "age",
    },
    {
      title: "Address",
      dataIndex: "address",
    },
  ];
  const data = [
    {
      key: "1",
      name: "John Brown",
      age: 32,
      address: "New York No. 1 Lake Park",
    },
    {
      key: "2",
      name: "Jim Green",
      age: 42,
      address: "London No. 1 Lake Park",
    },
    {
      key: "3",
      name: "Joe Black",
      age: 32,
      address: "Sydney No. 1 Lake Park",
    },
    {
      key: "4",
      name: "Disabled User",
      age: 99,
      address: "Sydney No. 1 Lake Park",
    },
  ];

  // rowSelection object indicates the need for row selection
  const rowSelection = {
    onChange: (selectedRowKeys, selectedRows) => {
      console.log(
        `selectedRowKeys: ${selectedRowKeys}`,
        "selectedRows: ",
        selectedRows
      );
    },
    getCheckboxProps: (record) => ({
      disabled: record.name === "Disabled User",
      // Column configuration not to be checked
      name: record.name,
    }),
  };
  const [selectionType, setSelectionType] = useState("checkbox");

  return (
    <div className="cart">
      <div className="img-banner">
        <img className="img-shoe" src={imgShoe} alt="..." />
        <h1 className="text-welcome">Chào mừng đến với giỏ hàng bạn</h1>
      </div>
      <div className="content-cart">
        <div className="title-cart">
          <p className="cart-text">Giỏ Hàng</p>
          <img className=" hidden-text" src={logoHidden} alt="..." />
        </div>

        <Row>
          <Col lg={{ span: 18, offset: 3 }}>
            <div className="form-content-cart">
              <div className="info-cart">
                <div
                  style={{
                    height: "30px",
                    display: "flex",
                    padding: "30px",
                    alignItems: "center",
                    marginTop: "60px",
                    borderRadius: "10px",
                    backgroundColor: "#FF6600",
                  }}
                >
                  <div>
                    <Checkbox
                      className="custom-checkbox-all"
                      onChange={handleSelectAllChange}
                      checked={selectAllChecked}
                    />
                  </div>
                  <div
                    style={{
                      marginLeft: "7%",
                      fontWeight: "bold",
                      color: "white",
                      fontSize: "15px",
                    }}
                  >
                    Hình ảnh
                  </div>
                  <div
                    style={{
                      marginLeft: "20%",
                      fontWeight: "bold",
                      color: "white",
                      fontSize: "15px",
                    }}
                  >
                    Sản phẩm
                  </div>
                  <div
                    style={{
                      marginLeft: "35%",
                      fontWeight: "bold",
                      color: "white",
                      fontSize: "15px",
                    }}
                  >
                    Tổng cộng
                  </div>
                </div>

                <div>
                  {cart.length === 0 ? (
                    <div className="cart-is-empty">
                      <div> Giỏ hàng trống! </div>
                      <Link to={"/home"}>Mua hàng</Link>
                    </div>
                  ) : (
                    <>
                      {cart.map((item, index) => (
                        <div
                          className={`item-cart ${
                            index === cart.length - 1 ? "last-item" : ""
                          }`}
                          key={index}
                        >
                          <div key={index} className="box-cart-img">
                            <Checkbox
                              className="custom-checkbox"
                              onChange={(e) =>
                                chooseCartForBill(item, e.target.checked)
                              }
                              checked={chooseItemCart.some(
                                (cartItem) =>
                                  cartItem.idProductDetail ===
                                  item.idProductDetail
                              )}
                            />
                            <img
                              style={{
                                width: "150px",
                                height: "150px",
                                marginRight: 50,
                                marginLeft: 50,
                                borderRadius: "10px",
                              }}
                              src={item.image.split(",")[0]}
                              alt="..."
                            />
                          </div>
                          <div className="info-product-detail">
                            <div className="cart-name">
                              {" "}
                              {item.nameProduct} - [{item.nameSize}]
                            </div>
                            <div className="cart-price">
                              Giá: {formatMoney(item.price)}
                            </div>

                            <div
                              style={{
                                display: "flex",
                                alignItems: "center",
                                marginTop: "10px",
                              }}
                            >
                              <p
                                style={{ fontWeight: "bold", marginRight: 10 }}
                              >
                                Số lượng
                              </p>
                              <InputNumber
                                className="input-quantity-cart"
                                name="quantity"
                                type="number"
                                //  value={item.quantity}
                                defaultValue={item.quantity}
                                min="1"
                                onChange={(value) =>
                                  changeQuantity(item, value)
                                }
                              ></InputNumber>
                            </div>
                            <div className="button-delete-cart">
                              <Tooltip title="Xóa sản phẩm">
                                <FontAwesomeIcon
                                  icon={faTrash}
                                  size="xl"
                                  onClick={() => {
                                    deleteItemCart(item);
                                  }}
                                />
                              </Tooltip>
                            </div>
                          </div>
                          <div className="form-status-cart">
                            <div
                              style={{
                                fontSize: "17px",
                                fontWeight: "500",
                                textAlign: "center",
                                marginTop: "50%",
                                width: "150px",
                              }}
                            >
                              {formatMoney(item.quantity * item.price)}
                            </div>
                          </div>
                        </div>
                      ))}

                      <div style={{ display: "flex" }}>
                        <div className="button-delete-all-cart">Xóa tất cả</div>
                        <Link to="/home" style={{ marginLeft: "48%" }}>
                          <Button className="button-continue-to-buy">
                            Tiếp tục mua hàng
                          </Button>
                        </Link>
                      </div>
                    </>
                  )}
                </div>
              </div>
              {/* bill of cart */}
              <div className="bill-of-cart" style={{ borderRadius: "20px" }}>
                <div className="content-bill-of-cart">
                  <div className="text-bill-in-cart"> ĐƠN HÀNG</div>
                  {idAccountLocal === null ? (
                    <div className="voucher-of-cart">
                      <h3>THÊM MÃ KHUYẾN MÃI</h3>
                      <div style={{ display: "flex", marginTop: "15px" }}>
                        <Input
                          // readOnly
                          type="text"
                          style={{ borderRadius: "5px" }}
                          onChange={(e) => {
                            handleInputChange(e.target.value);
                          }}
                        />
                        <div
                          className="button-add-voucher-cart"
                          onClick={() => {
                            getVoucher(formSearch);
                          }}
                        >
                          ÁP DỤNG
                        </div>
                      </div>
                    </div>
                  ) : (
                    <div className="voucher-of-cart-acc">
                      <span>
                        <FontAwesomeIcon icon={faTags} /> Voucher
                      </span>{" "}
                      <span
                        style={{
                          marginLeft: "auto",
                          color: "blue",
                          cursor: "pointer",
                        }}
                        onClick={() => openListVoucher(idAccountLocal)}
                      >
                        {voucher.value !== 0
                          ? "Chọn lại mã giảm giá"
                          : " Chọn mã giảm giá"}
                      </span>
                    </div>
                  )}

                  <div
                    className={`value-bill-of-cart ${
                      idAccountLocal !== null ? "acc" : ""
                    }`}
                  >
                    <div style={{ display: "flex" }}>
                      <div
                        style={{
                          color: "#21201f",
                          fontFamily: "700",
                          textAlign: "center",
                          display: "flex",
                          justifyContent: "center",
                        }}
                      >
                        Đơn hàng :
                      </div>{" "}
                      <div
                        style={{
                          marginLeft: "auto",
                          color: "#21201f",
                          fontFamily: "700",
                        }}
                      >
                        {formatMoney(totalPrice)}
                      </div>
                    </div>
                    <div style={{ display: "flex", marginTop: "20px" }}>
                      <span>Giảm : </span>{" "}
                      <span style={{ marginLeft: "auto" }}>
                        {formatMoney(voucher.value)}
                      </span>
                    </div>
                  </div>
                  <div style={{ padding: "20px" }}>
                    <h3>Tổng tiền : {formatMoney(totalBill)}</h3>
                  </div>
                  <div className="button-pay" onClick={payment}>
                    TIẾP TỤC THANH TOÁN
                  </div>
                </div>
              </div>
            </div>
          </Col>
        </Row>
      </div>

      <Modal
        onCancel={closeModalSize}
        open={modalSize}
        okButtonProps={{ style: { display: "none" } }}
        cancelButtonProps={{ style: { display: "none" } }}
        style={{ padding: "20px" }}
      >
        {listSize.length !== 0 && <h3>Chọn size</h3>}
        <div className="list-size-of-cart">
          {listSize.length !== 0 ? (
            listSize.sort().map((item, index) => (
              <div
                key={index}
                className={`item-size-of-cart ${
                  clickedIndex === index ? "clicked" : ""
                }`}
                tabIndex="0"
                onClick={() => getDetailProduct(index, item)}
              >
                {item.nameSize}
              </div>
            ))
          ) : (
            <div>Xin lỗi sản phẩm đã hết size khác!</div>
          )}
        </div>
        <Button
          onClick={changeSize}
          style={{
            marginTop: "20px",
            marginLeft: "80%",
            backgroundColor: "#ff4400",
            color: "white",
          }}
        >
          Ok
        </Button>
      </Modal>
      <Modal
        onCancel={closeModalVoucher}
        open={modalVoucher}
        okButtonProps={{ style: { display: "none" } }}
        cancelButtonProps={{ style: { display: "none" } }}
        width={600}
        height={1000}
        {...modalProps}
      >
        <div className="category-voucher">
          <h1>Chọn mã khuyến mãi</h1>
          <div className="content-code-voucher-cart">
            Mã voucher{" "}
            <input
              className="input-code-voucher-cart"
              placeholder="Mã voucher"
              onChange={(e) => {
                handleInputChange(e.target.value);
              }}
            />
            <div
              className={`button-submit-voucher-cart ${
                !formSearch ? "" : "show"
              }`}
              onClick={() => {
                getVoucher(formSearch);
              }}
            >
              ÁP DỤNG
            </div>
          </div>

          <p>Chọn 1 voucher</p>
          <div className="voucher-list">
            {listVoucher.map((item, index) => (
              <div className="item-voucher">
                <div style={{ marginRight: "5%" }}>
                  <img className="img-voucher-cart" src={imgShoe1} alt="..." />
                </div>
                <div>
                  <p>{item.name}</p>
                  <p>Giảm: {formatMoney(item.value)}</p>
                  <p style={{ fontSize: "11px", marginTop: "5%" }}>
                    HSD: {dayjs(item.endDate).format("DD-MM-YYYY")}{" "}
                  </p>
                </div>
                <div style={{ marginLeft: "auto", paddingRight: 30 }}>
                  <Radio.Group
                    name="radiogroup"
                    value={selectedItem.idVoucher}
                    onChange={() => handleRadioChange(item)}
                  >
                    <Radio value={item.id}></Radio>
                  </Radio.Group>
                </div>
              </div>
            ))}
          </div>
        </div>
        <div style={{ display: "flex" }}>
          <div
            className="button-cancel-voucher-cart"
            onClick={closeModalVoucher}
          >
            Trở lại
          </div>
          <div className="button-ok-voucher-cart" onClick={submitVoucher}>
            Ok
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default Cart;
