import React, { useState } from "react";
import "./Style-sign-up.css"
import { Link } from "react-router-dom";
import logoLogin from "./../../../assets/images/logo_client.png";
import Footer from "../../../component/customer/footer/Footer";
import { Form, Input } from "antd";
import { LoginApi } from "../../../api/employee/login/Login.api";
import { toast } from "react-toastify";
function SignUp() {
    const [formSignUp, setFormSignUp] = useState({
        roles:"ROLE_USER"
    })
    const changeFormSignUp = (name, value) => {
        setFormSignUp((prev) => ({
            ...prev,
            [name]: value
        }))
    }
    const handleSignUp = ()=>{
        LoginApi.authenticationUp(formSignUp).then((res)=>{
            toast.success("Đăng ký thành công");
        })
    }
    return (<React.Fragment>
        <div className="sign-up">
            <div className="header-login">
                <Link style={{ marginLeft: "20%" }} to="/home">
                    <img className="logo-login" src={logoLogin} alt="..." />
                </Link>
            </div>
            <div className="box-sign-up">
                <div className="box-form-sign-up">
                    <div className="title-signup">
                        Đăng ký
                    </div>
                    <div className="form-signup">


                        <Form>
                            <Form.Item >
                                <Input
                                    className="input-form-signup" placeholder="Email"
                                    onChange={(e)=>changeFormSignUp("email", e.target.value)}
                                    />
                            </Form.Item>
                            <Form.Item >
                                <Input.Password
                                type="password"
                                    className="input-form-signup" placeholder="Mật khẩu"
                                    onChange={(e)=>changeFormSignUp("password", e.target.value)} />
                            </Form.Item>

                            <Form.Item >
                                <div className="button-signup" onClick={handleSignUp} >
                                    Đăng ký
                                </div>
                            </Form.Item>
                            <Form.Item >
                                <div >
                                    Bạn đã có tài khoản?  <Link style={{ color: "#e7511a" }} to={"/login"} >Đăng nhập</Link>
                                </div>
                            </Form.Item>
                        </Form>


                    </div>
                </div>
            </div>
            <Footer />
        </div>
    </React.Fragment>);
}

export default SignUp;