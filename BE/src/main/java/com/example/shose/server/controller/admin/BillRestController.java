package com.example.shose.server.controller.admin;

import com.example.shose.server.dto.request.bill.*;
import com.example.shose.server.service.BillService;
import com.example.shose.server.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping("/admin/bill")
public class BillRestController {

    @Autowired
    private BillService billService;

    @Value("${user}")
    private String userId;


    @GetMapping
    public ResponseObject getAll(BillRequest request){
        return  new ResponseObject(billService.getAll(request));
    }

    @GetMapping("/detail/{id}")
    public ResponseObject detail(@PathVariable("id") String id){
        return  new ResponseObject(billService.detail(id));
    }

    @GetMapping("/user-bill")
    public ResponseObject getAllUserInBill(){
        return  new ResponseObject(billService.getAllUserInBill());
    }

    @PostMapping("")
    public ResponseObject save(@RequestBody  CreateBillOfflineRequest request){
        return  new ResponseObject(billService.save(userId, request));
    }

    @PutMapping("/change-status/{id}")
    public ResponseObject changStatusBill(@PathVariable("id") String id, ChangStatusBillRequest request){
        return  new ResponseObject(billService.changedStatusbill(id, userId, request));
    }

    @PutMapping("/cancel-status/{id}")
    public ResponseObject cancelStatusBill(@PathVariable("id") String id, ChangStatusBillRequest request){
        return  new ResponseObject(billService.cancelBill(id, userId, request));
    }

    @GetMapping("/details-invoices-counter")
    public ResponseObject findAllBillAtCounterAndStatusNewBill(FindNewBillCreateAtCounterRequest request) {
        return  new ResponseObject(billService.findAllBillAtCounterAndStatusNewBill(request));
    }

    @GetMapping("/count-paymet-post-paid/{id}")
    public ResponseObject countPayMentPostpaidByIdBill(@PathVariable("id") String id) {
        return  new ResponseObject(billService.countPayMentPostpaidByIdBill(id));
    }

    @PutMapping("/update-offline/{id}")
    public ResponseObject updateBillOffline(@PathVariable("id") String id, @RequestBody UpdateBillRequest request) {
        return  new ResponseObject(billService.updateBillOffline(id, request));
    }

    @PutMapping("/change-status-bill")
    public ResponseObject changeStatusAllBillByIds(@RequestBody ChangAllStatusBillByIdsRequest request) {
        return  new ResponseObject(billService.changeStatusAllBillByIds(request, userId));
    }

    @GetMapping("/code-bill")
    public ResponseObject CreateCodeBill() {
        return  new ResponseObject(billService.CreateCodeBill(userId));
    }

    @PutMapping("/update-bill-wait")
    public ResponseObject updateBillWait(@RequestBody CreateBillOfflineRequest request) {
        return  new ResponseObject(billService.updateBillWait(request));
    }

}
