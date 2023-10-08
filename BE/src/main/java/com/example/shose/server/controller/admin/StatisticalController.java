package com.example.shose.server.controller;

import com.example.shose.server.dto.request.color.FindColorRequest;
import com.example.shose.server.dto.request.statistical.FindBillDateRequest;
import com.example.shose.server.service.StatisticalService;
import com.example.shose.server.util.ResponseObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin("*")
@RequestMapping("/admin/statistical")
public class StatisticalController {
    @Autowired
    private StatisticalService statisticalService;

    @GetMapping("/day")
    public ResponseObject statisticalDay() {
        return new ResponseObject(statisticalService.getAllStatisticalDay());
    }

    @GetMapping("/month")
    public ResponseObject statisticalMonth() {
        return new ResponseObject(statisticalService.getAllStatisticalMonth());
    }

    @GetMapping("/status-bill")
    public ResponseObject statisticalStatusBill() {
        return new ResponseObject(statisticalService.getAllStatisticalStatusBill());
    }
    @GetMapping("/best-selling-product")
    public ResponseObject statisticalBestSellingProduct() {
        return new ResponseObject(statisticalService.getAllStatisticalBestSellingProduct());
    }
    @GetMapping("/bill-date")
    public ResponseObject statisticalBillDate(final FindBillDateRequest req) {
            return new ResponseObject(statisticalService.getAllStatisticalBillDate(req));
    }
}
