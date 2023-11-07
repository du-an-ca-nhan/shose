import React, { useState, useEffect } from "react";
import { Modal, Input, Button, Form, Row, Col, Table } from "antd";
import "../dashboard/style-dashboard.css";
import { CChart } from "@coreui/react-chartjs";
import { StatisticalApi } from "../../../api/employee/statistical/statistical.api";
import * as am5 from "@amcharts/amcharts5";
import * as am5xy from "@amcharts/amcharts5/xy";
import am5themes_Animated from "@amcharts/amcharts5/themes/Animated";
import * as am5percent from "@amcharts/amcharts5/percent";
import moment from "moment";

const DashBoard = () => {
  const [totalBillMonth, setTotalBillMonth] = useState(0);
  const [totalBillAmoutMonth, setTotalBillAmoutMonth] = useState(0);
  const [totalProductMonth, setTotalProductMonth] = useState(0);
  const [totalBillDay, setTotalBillDay] = useState(0);
  const [totalBillAmountDay, setTotalBillAmoutDay] = useState(0);

  const [dataPie, setDataPie] = useState([]);
  const [listSellingProduct, setListSellingProduct] = useState([]);
  const [dataColumn, setDataColumn] = useState([]);

  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);

  const loadData = () => {
    StatisticalApi.fetchAllStatisticalMonth().then(
      (res) => {
        const data = res.data.data[0];
        setTotalBillMonth(data.totalBill);
        setTotalBillAmoutMonth(formatCurrency(data.totalBillAmount));
        setTotalProductMonth(data.totalProduct);
      },
      (err) => {
        console.log(err);
      }
    );
    StatisticalApi.fetchAllStatisticalDay().then(
      (res) => {
        const data = res.data.data[0];
        setTotalBillDay(data.totalBillToday);
        setTotalBillAmoutDay(formatCurrency(data.totalBillAmountToday));
      },
      (err) => {
        console.log(err);
      }
    );

    StatisticalApi.fetchAllStatisticalBestSellingProduct().then(
      (res) => {
        const data = res.data.data.map((dataBestSell, index) => ({
          ...dataBestSell,
          stt: index + 1,
        }));
        setListSellingProduct(data);
      },
      (err) => {
        console.log(err);
      }
    );
    StatisticalApi.fetchAllStatisticalStatusBill().then(
      (res) => {
        const data = res.data.data;
        setDataPie(data);
        // drawChartPie(data)
        const statusMapping = {
          TAO_HOA_DON: "Tạo hóa đơn",
          CHO_XAC_NHAN: "Chờ xác nhận",
          CHO_VAN_CHUYEN: "Chờ vận chuyển",
          VAN_CHUYEN: "vận chuyển",
          DA_THANH_TOAN: "Đã thanh toán",
          THANH_CONG: "Thành công",
          TRA_HANG: "Trả hàng",
          DA_HUY: "Đã Hủy",
        };

        const statusColors = {
          TAO_HOA_DON: "#E46651",
          CHO_XAC_NHAN: "#00D8FF",
          CHO_VAN_CHUYEN: "#FFCE56",
          VAN_CHUYEN: "#9C27B0",
          DA_THANH_TOAN: "#41B883",
          THANH_CONG: "#4CAF50",
          TRA_HANG: "##FF5733",
          DA_HUY: "#DD1B16",
        };

        const newDataPie = data.map(item => ({
          category: statusMapping[item.statusBill] || item.statusBill,
          value: item.totalStatusBill,
          color: statusColors[item.statusBill] || item.statusBill,
        }));
        drawChartPie(newDataPie)
      },
      (err) => {
        console.log(err);
      }
    );

    StatisticalApi.fetchBillByDate().then(
      (res) => {
        const dataBill = res.data.dataBill;
        const dataProduct = res.data.dataProduct;
        const dateBillList = [];
        const dateProductList = [];
        const groupBill = new Map();
        const groupProduct = new Map();
        dataBill.forEach((item) => {
          const date = new Date(Number(item.billDate));
          const formattedDate = `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
          dateBillList.push({ totalBillDate: item.totalBillDate, billDate: formattedDate });
        });
        dataProduct.forEach((item) => {
          const date = new Date(Number(item.billDate));
          const formattedDate = `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
          dateProductList.push({ totalProductDate: item.totalProductDate, billDate: formattedDate });
        });


        dateProductList.forEach(item => {
          const { totalProductDate, billDate } = item;
          if (groupProduct.has(billDate)) {
            const existingItem = groupProduct.get(billDate);
            existingItem.totalProductDate += totalProductDate;
          } else {
            groupProduct.set(billDate, { totalProductDate, billDate });
          }
        });

        dateBillList.forEach(item => {
          const { totalBillDate, billDate } = item;
          if (groupBill.has(billDate)) {
            const existingItem = groupBill.get(billDate);
            existingItem.totalBillDate += totalBillDate;
          } else {
            groupBill.set(billDate, { totalBillDate, billDate });
          }
        });
        drawChartEnergy(Array.from(groupBill.values()), Array.from(groupProduct.values()));
      },
      (err) => {
        console.log(err);
      }
    );
  };
  useEffect(() => {
    loadData();
  }, []);

  const formatCurrency = (value) => {
    const formatter = new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
      currencyDisplay: "code",
    });
    return formatter.format(value);
  };

  const drawChartPie = (data) => {
    am5.array.each(am5.registry.rootElements, function (root) {
      if (root) {
        if (root.dom.id == "chartdivPie") {
          root.dispose();
        }
      }
    });

    let root = am5.Root.new("chartdivPie");
    root.setThemes([
      am5themes_Animated.new(root)
    ]);

    // Create chart
    var chart = root.container.children.push(am5percent.PieChart.new(root, {
      layout: root.verticalLayout
    }));

    // Create series
    var series = chart.series.push(am5percent.PieSeries.new(root, {
      valueField: "value",
      categoryField: "category"
    }));

    // Gán dữ liệu đã được cập nhật cho series
    series.data.setAll(data);

    // Create legend
    var legend = chart.children.push(am5.Legend.new(root, {
      centerX: am5.percent(50),
      x: am5.percent(50),
      marginTop: 15,
      marginBottom: 15
    }));

    legend.data.setAll(series.dataItems);

    // Play initial series animation
    series.appear(1000, 100);
  }

  const drawChart = (dataX) => {
    am5.array.each(am5.registry.rootElements, function (root) {
      if (root) {
        if (root.dom.id == "chartdivChart") {
          root.dispose();
        }
      }
    });

    let root = am5.Root.new("chartdivChart");
    root.setThemes([
      am5themes_Animated.new(root)
    ]);

    // Create chart
    // https://www.amcharts.com/docs/v5/charts/xy-chart/
    var chart = root.container.children.push(am5xy.XYChart.new(root, {
      panX: false,
      panY: false,
      wheelX: "panX",
      wheelY: "zoomX"
    }));


    // Add cursor
    // https://www.amcharts.com/docs/v5/charts/xy-chart/cursor/
    var cursor = chart.set("cursor", am5xy.XYCursor.new(root, {
      behavior: "zoomX"
    }));
    cursor.lineY.set("visible", false);

    var date = new Date();
    date.setHours(0, 0, 0, 0);
    var value = 100;

    function generateData() {
      value = Math.round((Math.random() * 10 - 5) + value);
      am5.time.add(date, "day", 1);
      return {
        date: date.getTime(),
        value: value
      };
    }

    function generateDatas(count) {
      var data = [];
      for (var i = 0; i < count; ++i) {
        data.push(generateData());
      }
      return data;
    }


    // Create axes
    // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
    var xAxis = chart.xAxes.push(am5xy.DateAxis.new(root, {
      maxDeviation: 0,
      baseInterval: {
        timeUnit: "day",
        count: 1
      },
      renderer: am5xy.AxisRendererX.new(root, {
        minGridDistance: 60
      }),
      tooltip: am5.Tooltip.new(root, {})
    }));

    var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
      renderer: am5xy.AxisRendererY.new(root, {})
    }));


    // Add series
    // https://www.amcharts.com/docs/v5/charts/xy-chart/series/
    var series = chart.series.push(am5xy.ColumnSeries.new(root, {
      name: "Series",
      xAxis: xAxis,
      yAxis: yAxis,
      valueYField: "totalBillDate",
      valueXField: "billDate",
      tooltip: am5.Tooltip.new(root, {
        labelText: "Hóa đơn: {valueY}"
      })
    }));

    series.columns.template.setAll({ strokeOpacity: 0 })

    // Add scrollbar
    // https://www.amcharts.com/docs/v5/charts/xy-chart/scrollbars/
    chart.set("scrollbarX", am5.Scrollbar.new(root, {
      orientation: "horizontal"
    }));
    var data = generateDatas(50);
    series.data.setAll(dataX);
    console.log(dataX);

    // Make stuff animate on load
    // https://www.amcharts.com/docs/v5/concepts/animations/
    series.appear(1000);
    chart.appear(1000, 100);

  };

  const columns = [
    {
      title: "STT",
      dataIndex: "stt",
      key: "stt",
      sorter: (a, b) => a.stt - b.stt,
      width: 50
    },
    {
      title: "Ảnh",
      dataIndex: "image",
      key: "image",
      render: (text, record) => (
        <div style={{ position: "relative", display: "inline-block" }}>
          <img
            src={text}
            alt="Ảnh sản phẩm"
            style={{ width: "90px", borderRadius: "10%", height: "90px" }}
          />
        </div>
      ),
    },
    {
      title: "Tên Sản Phẩm",
      dataIndex: "nameProduct",
      key: "nameProduct",
      sorter: (a, b) => a.nameProduct.localeCompare(b.nameProduct),
    },
    {
      title: "Giá Bán",
      dataIndex: "price",
      key: "price",
      sorter: (a, b) => a.price - b.price,
      render: (text) => formatCurrency(text),
    },
    {
      title: "Số lượng đã bán",
      dataIndex: "sold",
      key: "sold",
      sorter: (a, b) => a.sold - b.sold,
      align: "center",
      width: 60
    },
    {
      title: "Doanh số",
      dataIndex: "sales",
      key: "sales",
      sorter: (a, b) => a.seles - b.seles,
      render: (text) => formatCurrency(text),
      align: "center",
    },
  ];
  const getRowClassName = (record, index) => {
    return index % 2 === 0 ? "even-row" : "odd-row";
  };

  const handleStartDateChange = (event) => {
    const startDate = event.target.value;
    const startDateLong = new Date(startDate).getTime();
    setStartDate(startDateLong);
    loadDataChartColumn(startDateLong, endDate);
  };

  const handleEndDateChange = (event) => {
    const endDate = event.target.value;
    const endDateLong = new Date(endDate).getTime();
    setEndDate(endDateLong);
    loadDataChartColumn(startDate, endDateLong);
  };

  const loadDataChartColumn = (startDate, endDate) => {
    StatisticalApi.fetchBillByDate(startDate, endDate).then(
      (res) => {
        const dataBill = res.data.dataBill;
        const dataProduct = res.data.dataProduct;
        const dateBillList = [];
        const dateProductList = [];
        const groupBill = new Map();
        const groupProduct = new Map();
        dataBill.forEach((item) => {
          const date = new Date(Number(item.billDate));
          const formattedDate = `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
          dateBillList.push({ totalBillDate: item.totalBillDate, billDate: formattedDate });
        });
        dataProduct.forEach((item) => {
          const date = new Date(Number(item.billDate));
          const formattedDate = `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
          dateProductList.push({ totalProductDate: item.totalProductDate, billDate: formattedDate });
        });


        dateProductList.forEach(item => {
          const { totalProductDate, billDate } = item;
          if (groupProduct.has(billDate)) {
            const existingItem = groupProduct.get(billDate);
            existingItem.totalProductDate += totalProductDate;
          } else {
            groupProduct.set(billDate, { totalProductDate, billDate });
          }
        });

        dateBillList.forEach(item => {
          const { totalBillDate, billDate } = item;
          if (groupBill.has(billDate)) {
            const existingItem = groupBill.get(billDate);
            existingItem.totalBillDate += totalBillDate;
          } else {
            groupBill.set(billDate, { totalBillDate, billDate });
          }
        });
        drawChartEnergy(Array.from(groupBill.values()), Array.from(groupProduct.values()));
      },
      (err) => {
        console.log(err);
      }
    );
  };

  const drawChartEnergy = (dataBill, dataProduct) => {

    var colorsSES11 = ""
    var colorsSES12 = ""
    var colorsSES21 = ""
    var colorsSES22 = ""

    colorsSES12 = 0x9D92AF
    colorsSES22 = 0x0f105f
    colorsSES21 = 0xF37021
    colorsSES11 = 0xFFD4A6


    am5.array.each(am5.registry.rootElements, function (root) {
      if (root) {
        if (root.dom.id == "chartdivChart") {
          root.dispose();
        }
      }
    });
    am5.ready(function () {
      let root = am5.Root.new("chartdivChart");
      // Create root element
      // https://www.amcharts.com/docs/v5/getting-started/#Root_element

      // Set themes
      // https://www.amcharts.com/docs/v5/concepts/themes/
      root.setThemes([am5themes_Animated.new(root)]);

      // Create chart
      // https://www.amcharts.com/docs/v5/charts/xy-chart/
      let chart = root.container.children.push(
        am5xy.XYChart.new(root, {
          panX: false,
          panY: false,
          wheelX: "panX",
          wheelY: "zoomX",
          layout: root.verticalLayout
        })
      );

      // Add scrollbar
      // https://www.amcharts.com/docs/v5/charts/xy-chart/scrollbars/
      chart.set(
        "scrollbarX",
        am5.Scrollbar.new(root, {
          orientation: "horizontal"
        })
      );
      let scrollbarX = chart.get("scrollbarX");

      scrollbarX.thumb.setAll({
        fill: am5.color(0x550000),
        fillOpacity: 0.1
      });

      scrollbarX.startGrip.setAll({
        visible: true
      });

      scrollbarX.endGrip.setAll({
        visible: true
      });

      // Create axes
      // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/

      let xRenderer = am5xy.AxisRendererX.new(root, {
        minGridDistance: 10,
        cellStartLocation: 0.2,
        cellEndLocation: 0.8
      });

      xRenderer.labels.template.setAll({
        rotation: -70,
        paddingTop: -20,
        paddingRight: 10,
        fontSize: 10
      });

      let xAxis = chart.xAxes.push(
        am5xy.CategoryAxis.new(root, {
          categoryField: "billDate",
          maxDeviation: 0,
          renderer: xRenderer,
          tooltip: am5.Tooltip.new(root, {})
        })
      );
      let xAxis2 = chart.xAxes.push(
        am5xy.CategoryAxis.new(root, {
          categoryField: "billDate",
          maxDeviation: 0,
          renderer: xRenderer,
          tooltip: am5.Tooltip.new(root, {})
        })
      );
      var nameComp = "Hóa đơn"
      var nameNow = "Sản phẩm"

      xAxis.data.setAll(dataBill);
      xAxis2.data.setAll(dataProduct);
      console.log(dataProduct);

      let yRenderer = am5xy.AxisRendererY.new(root, {
        strokeOpacity: 0.1
      });

      let yAxis = chart.yAxes.push(
        am5xy.ValueAxis.new(root, {
          maxDeviation: 1,
          min: 0,
          renderer: yRenderer
        })
      );
      yAxis.children.moveValue(am5.Label.new(root, { text: `Số lượng`, rotation: -90, y: am5.p50, centerX: am5.p50 }), 0);
      var series1 = chart.series.push(
        am5xy.ColumnSeries.new(root, {
          name: nameComp,
          xAxis: xAxis,
          yAxis: yAxis,
          valueYField: "totalBillDate",
          categoryXField: "billDate",
          tooltip: am5.Tooltip.new(root, {
            pointerOrientation: "horizontal",
            labelText: "Hóa đơn: {valueY}"
          }),
          fill: am5.color(colorsSES11)
        })
      );

      yRenderer.grid.template.set("strokeOpacity", 0.05);
      yRenderer.labels.template.set("fill", series1.get("fill"));
      yRenderer.setAll({
        stroke: series1.get("fill"),
        strokeOpacity: 1,
        opacity: 1
      });

      series1.columns.template.setAll({
        width: am5.percent(40),
        tooltipY: am5.percent(30),
        templateField: "columnSettings",
        dx: -25
      });

      series1.columns.template.set("fillGradient", am5.LinearGradient.new(root, {
        stops: [{
          color: am5.color(0x297373),
          offset: 0.7
        }, {
          color: am5.color(0x946b49)
        }],
        rotation: 90
      }));

      series1.data.setAll(dataBill);


      // Add series
      // https://www.amcharts.com/docs/v5/charts/xy-chart/series/

      var series2 = chart.series.push(am5xy.ColumnSeries.new(root, {
        name: nameNow,
        xAxis: xAxis2,
        yAxis: yAxis,
        valueYField: "totalProductDate",
        categoryXField: "billDate",
        clustered: false,
        tooltip: am5.Tooltip.new(root, {
          labelText: "Sản phẩm: {valueY}"
        }),
        fill: am5.color(colorsSES21)
      }));

      series2.columns.template.setAll({
        width: am5.percent(35),
        templateField: "columnSettings",
        dx: 0
      });

      series2.columns.template.set("fillGradient", am5.LinearGradient.new(root, {
        stops: [{
          color: am5.color(0xFF621F)
        }, {
          color: am5.color(0x946B49)
        }],
        rotation: 90
      }));

      series2.data.setAll(dataProduct);

      // Add cursor
      // https://www.amcharts.com/docs/v5/charts/xy-chart/cursor/
      let cursor = chart.set("cursor", am5xy.XYCursor.new(root, {
        behavior: "zoomX"
      }));
      cursor.lineY.set("visible", false);

      // Add legend
      // https://www.amcharts.com/docs/v5/charts/xy-chart/legend-xy-series/
      let legend = chart.children.push(
        am5.Legend.new(root, {
          centerX: am5.p50,
          x: am5.p50
        })
      );
      legend.data.setAll(chart.series.values);

      // Make stuff animate on load
      // https://www.amcharts.com/docs/v5/concepts/animations/
      chart.appear(1000, 100);
      series1.appear();

      // xAxis.events.once("datavalidated", function (ev) {
      //   ev.target.zoomToIndexes(dataBill.length - 20, dataProduct.length);
      // });

    });
  };
  return (

    <div>
      <div
        className="content-wrapper"
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <span
          style={{
            fontSize: "25px",
            fontWeight: "bold",
            marginTop: "10px",
            marginBottom: "20px",
          }}
        >
          THỐNG KÊ
        </span>
      </div>
      <div>
        <Row className="row-header">
          <Col span={7} className="col-header">
            <div className="content-header">
              <h2 className="color-text-topic">Doanh số tháng này</h2>
              <h3 className="color-text-content">
                {totalBillMonth} đơn hàng / {totalBillAmoutMonth}
              </h3>
            </div>
          </Col>

          <Col span={7} className="col-header">
            <div className="content-header">
              <h2 className="color-text-topic">Doanh số hôm nay</h2>
              <h3 className="color-text-content">
                {totalBillDay} đơn hàng / {totalBillAmountDay}
              </h3>
            </div>
          </Col>

          <Col span={7} className="col-header">
            <div className="content-header">
              <h2 className="color-text-topic">Hàng bán được tháng này</h2>
              <h3 className="color-text-content">
                {totalProductMonth} sản phẩm
              </h3>
            </div>
          </Col>
        </Row>
        <Row className="row-body">
          <h2>Biểu đồ thống kê</h2>
          <div className="row-body-container">
            <div class="header-date">
              <br />
              <div className="header-date">
                <label htmlFor="startDate" style={{ marginRight: "10px" }}>
                  Ngày bắt đầu:
                </label>
                <Input
                  id="startDate"
                  style={{ width: "27%", height: "45px" }}
                  type="date"
                  onChange={handleStartDateChange}
                />
                <label
                  htmlFor="endDate"
                  style={{ marginLeft: "20px", marginRight: "10px" }}
                >
                  Ngày kết thúc:
                </label>
                <Input
                  id="endDate"
                  style={{ width: "27%", height: "45px" }}
                  type="date"
                  onChange={handleEndDateChange}
                />
              </div>
            </div>
            <div style={{ marginLeft: "50px" }} >
              <div id="chartdivChart">
              </div>
            </div>
          </div>

        </Row>
        <Row className="row-footer">
          <Col className="row-footer-left">
            <h2 style={{ textAlign: "center", margin: " 2%" }}>
              Top sản phẩm bán chạy
            </h2>
            <Table
              style={{ marginTop: "30px" }}
              dataSource={listSellingProduct}
              rowKey="stt"
              columns={columns}
              pagination={{ pageSize: 3 }}
              scroll={{ y: 400 }}
              rowClassName={getRowClassName}
            />
          </Col>
          <Col className="row-footer-right">
            <h2 style={{ textAlign: "center", margin: " 3%" }}>
              Trạng thái đơn hàng
            </h2>
            <div id="chartdivPie">

            </div>
            {/* <CChart
              type="doughnut"
              className="chart-container"
              data={{
                datasets: [
                  {
                    backgroundColor: chartPieColor,
                    data: chartPieData,
                  },
                ],
                labels: chartPieLabels,
              }}
              options={{
                borderRadius: 2,
                borderWidth: 5,
                plugins: {
                  legend: {
                    position: "bottom",
                    labels: {
                      color: "#333",
                      font: {
                        size: 19,
                      },
                    },
                  },
                },
              }}
            /> */}
          </Col>
        </Row>
      </div>
    </div>

  );
};

export default DashBoard;
