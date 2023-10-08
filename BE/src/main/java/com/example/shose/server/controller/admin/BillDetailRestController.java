package com.example.shose.server.controller.admin;

import com.example.shose.server.dto.request.billdetail.CreateBillDetailRequest;
import com.example.shose.server.dto.request.billdetail.RefundProductRequest;
import com.example.shose.server.service.BillDetailService;
import com.example.shose.server.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@CrossOrigin("*")
@RequestMapping("/admin/bill-detail")
public class BillDetailRestController {

    @Autowired
    private BillDetailService billDetailService;

    @GetMapping("/{id}")
    public ResponseObject findAllByIdBill(@PathVariable("id") String id){
        return  new ResponseObject(billDetailService.findAllByIdBill(id));
    }

    @GetMapping("/detail/{id}")
    public ResponseObject findBillById(@PathVariable("id") String id){
        return  new ResponseObject(billDetailService.findBillById(id));
    }

    @PutMapping("/refund")
    public ResponseObject refundProduct(@RequestBody RefundProductRequest request){
        return  new ResponseObject(billDetailService.refundProduct(request));
    }

    @PutMapping("/{id}")
    public ResponseObject update(@PathVariable("id")String id, @RequestBody CreateBillDetailRequest request){
        return  new ResponseObject(billDetailService.update(id, request));
    }

    @PostMapping("/add-product")
    public ResponseObject addProduct(@RequestBody CreateBillDetailRequest request){
        return  new ResponseObject(billDetailService.create(request));
    }

    @DeleteMapping("/remove/{id}/{productDetail}")
    public ResponseObject removeProductInBill(@PathVariable("id") String id, @PathVariable("productDetail") String productDetail){
        return  new ResponseObject(billDetailService.delete(id, productDetail));
    }
}
