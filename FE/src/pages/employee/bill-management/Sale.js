import { Button, Col, Row, Table, Tabs } from "antd";
import Search from "antd/es/input/Search";
import React, { useCallback, useRef, useState } from "react";
import { PlusOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import { useEffect } from "react";
import { BillApi } from "../../../api/employee/bill/bill.api";
import { useAppDispatch } from "../../../app/hook";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
  addBillAtCounTer,
  addBillWait,
  deleteBillWait,
  getAllBillAtCounter,
  getAllBillWait,
  updateKeyBillAtCounter,
} from "../../../app/reducer/Bill.reducer";
import moment from "moment";
import CreateBill from "./CreateBill";
import './sale.css'

function Sale() {
  const [invoiceNumber, setInvoiceNumber] = useState(0);

  const [activeKey, setActiveKey] = useState(0);
  const [changeTab, setChangTab] = useState();
  const [dataKey, setDataKey] = useState([]);
  const items= useSelector((state) => state.bill.billWaits.value);
  // console.log(items);
  const newTabIndex = useRef(1);
  const dispatch = useAppDispatch();

  const onChange = (key) => {
    setActiveKey(key);
    setChangTab(key);
    dispatch(updateKeyBillAtCounter(key))
  };

  useEffect(() => {
    BillApi.fetchAllBillAtCounter().then((res) => {
      if (res.data.data.length == 0) {
        BillApi.getCodeBill().then((res) => {
          const newActiveKey = `${newTabIndex.current}`;
          setActiveKey(newActiveKey);
          setInvoiceNumber(1);
          dispatch(getAllBillWait([
            ...items,
            {
              label: `Hóa đơn ${newTabIndex.current++}`,
              children: (
                  <CreateBill
                      code={res.data.data.code}
                      key={changeTab}
                      id={res.data.data.id}
                      style={{ width: "100%" }}
                      invoiceNumber={1}
                      removePane={remove}
                      targetKey={newActiveKey}
                  />
              ),
              key: newActiveKey,
            },
          ]));
          dispatch(addBillAtCounTer(`Hóa đơn ${newTabIndex.current}`));
          setInvoiceNumber(invoiceNumber + 1);
          setDataKey([...dataKey, res.data.data.code])
        });
      } else {
        setInvoiceNumber(res.data.data.length);
        const defaultPanes = res.data.data.map((item, index) => {
          const id = String(index + 1);
          setDataKey([...dataKey, item.code])
          const newActiveKey = `${newTabIndex.current}`;
          return {
            label: `Hóa đơn ${newTabIndex.current++}`,
            children: (
                <CreateBill
                    code={item.code}
                    id={item.id}
                    key={changeTab}
                    invoiceNumber={res.data.data.length}
                    style={{ width: "100%" }}
                    removePane={remove}
                    targetKey={newActiveKey}
                />
            ),
            key: newActiveKey,
          };
        });
       
        console.log(defaultPanes);
        dispatch(getAllBillWait(defaultPanes));
        setActiveKey('1');
      }
    });
  }, []);


  const add = (e) => {
    if (items.length >= 5) {
      toast.warning(`Không thể tạo thêm hóa đơn`);
    } else {

      BillApi.getCodeBill().then((res) => {
        setDataKey([...dataKey, res.data.data.code])
        const newActiveKey = `${newTabIndex.current}`;
        dispatch(addBillWait( {
          label: `Hóa đơn ${newTabIndex.current++}`,
          children: (
              <CreateBill
                  code={res.data.data.code}
                  key={changeTab}
                  id={res.data.data.id}
                  invoiceNumber={invoiceNumber}
                  style={{ width: "100%" }}
                  removePane={remove}
                  targetKey={newActiveKey}
              />
          ),
          key: newActiveKey,
        },)
         
        );
        dispatch(addBillAtCounTer(`Hóa đơn ${newTabIndex.current}`));
        setActiveKey(newActiveKey);
        setChangTab(newActiveKey);
        setInvoiceNumber(invoiceNumber + 1);
        dispatch(updateKeyBillAtCounter(newActiveKey))
      });
    }
  };

  const remove = (targetKey, invoiceNumbers, items) => {
    if(items.length > 1){
      const targetIndex = items.findIndex((pane) => pane.key === targetKey);
      const newPanes = items.filter((pane) => pane.key !== targetKey);
      console.log(newPanes)
      if (newPanes.length > 0 && targetIndex >= 0) {
        const { key } =
            newPanes[
                targetIndex === newPanes.length ? targetIndex - 1 : targetIndex
                ];
        setActiveKey(key);
        setChangTab(key);
        dispatch(updateKeyBillAtCounter(key))
        dispatch(getAllBillWait(newPanes));
      }
      // dispatch(deleteBillWait(targetIndex));
      console.log();
      setInvoiceNumber(invoiceNumber - 1);
    }else{
      dispatch(getAllBillWait([]));
      
      BillApi.getCodeBill().then((res) => {
        const newActiveKey = `${newTabIndex.current}`;
        dispatch(addBillWait( {
          label: `Hóa đơn ${newTabIndex.current++}`,
          children: (
              <CreateBill
                  code={res.data.data.code}
                  key={changeTab}
                  id={res.data.data.id}
                  invoiceNumber={1}
                  style={{ width: "100%" }}
                  removePane={remove}
                  targetKey={newActiveKey}
              />
          ),
          key: newActiveKey,
        }));
        dispatch(addBillAtCounTer(`Hóa đơn ${newTabIndex.current}`));
        setActiveKey(newActiveKey);
        setChangTab(newActiveKey);
        setInvoiceNumber(invoiceNumber + 1);
        dispatch(updateKeyBillAtCounter(newActiveKey))
      });
    }

  };
  const onEdit = (targetKey, action) => {
    if (action === "add") {
      add();
    } 
    else {
      remove(activeKey,invoiceNumber, items);
    }
  };


  return (
      <div>
        <Row style={{ background: "white", width: "100%" }}>
          <Row style={{ width: "100%", marginTop: "10px" }}>
            <Col span={12}></Col>
            <Col span={12} align={"end"}>
              <Button
                  type="primary"
                  onClick={(e) => add(e)}
                  icon={<PlusOutlined />}
                  size={"large"}
                  style={{ marginRight: "20px" }}
              >
                Tạo hóa đơn
              </Button>
            </Col>
          </Row>
          <Row style={{ width: "100%", marginTop: "40px" }}>
            <Tabs
                hideAdd
                onChange={onChange}
                activeKey={activeKey}
                style={{ width: "100%", marginLeft: "10px" }}
                type="editable-card"
                onEdit={onEdit}
                items={items}
            />
          </Row>
        </Row>
        <ToastContainer
        position="top-right"
        autoClose={100}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
      {/* Same as */}
      <ToastContainer />
      </div>
  );
}

export default Sale;


