package com.example.shose.server.infrastructure.exportPdf;

import com.example.shose.server.dto.response.bill.InvoiceItemResponse;
import com.example.shose.server.dto.response.bill.InvoicePaymentResponse;
import com.example.shose.server.dto.response.bill.InvoiceResponse;
import com.example.shose.server.dto.response.billdetail.BillDetailResponse;
import com.example.shose.server.entity.Bill;
import com.example.shose.server.entity.BillHistory;
import com.example.shose.server.entity.PaymentsMethod;
import com.example.shose.server.infrastructure.constant.StatusBill;
import com.example.shose.server.infrastructure.constant.StatusMethod;
import com.example.shose.server.infrastructure.constant.StatusPayMents;
import com.example.shose.server.infrastructure.email.SendEmailService;
import com.example.shose.server.repository.*;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class ExportFilePdfFormHtml {

    @Autowired
    private BillHistoryRepository billHistoryRepository;

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private PaymentsMethodRepository paymentsMethodRepository;

    public Context setData(InvoiceResponse invoice) {

        Context context = new Context();

        Map<String, Object> data = new HashMap<>();

        data.put("invoice", invoice);

        context.setVariables(data);

        return context;
    }

    public Context setDataSendMail(InvoiceResponse invoice, String url) {

        Context context = new Context();

        Map<String, Object> data = new HashMap<>();

        data.put("invoice", invoice);
        data.put("url", url);
        context.setVariables(data);

        return context;
    }

    public String htmlToPdf(String processedHtml, HttpServletRequest request, String code) {

//        Principal principal = request.getUserPrincipal();
//        String downloadPath = principal.getPath();
        String downloadPath = System.getProperty("user.home") + "/Downloads";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {

            PdfWriter pdfwriter = new PdfWriter(byteArrayOutputStream);

            DefaultFontProvider defaultFont = new DefaultFontProvider(false, true, false);

            ConverterProperties converterProperties = new ConverterProperties();

            converterProperties.setFontProvider(defaultFont);

            HtmlConverter.convertToPdf(processedHtml, pdfwriter, converterProperties);

            FileOutputStream fout = new FileOutputStream(downloadPath + "/"+code+".pdf");

            byteArrayOutputStream.writeTo(fout);
            byteArrayOutputStream.close();

            byteArrayOutputStream.flush();
            fout.close();

            return null;

        } catch(Exception ex) {

            //exception occured
        }

        return null;
    }

    public  NumberFormat formatCurrency() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
        formatter.setCurrency(Currency.getInstance("VND"));
        return formatter;
    }



    public InvoiceResponse getInvoiceResponse(Bill bill){

        List<BillDetailResponse> billDetailResponses = billDetailRepository.findAllByIdBill(bill.getId());
        List<BillHistory> findAllByBill = billHistoryRepository.findAllByBill(bill);
        List<PaymentsMethod> paymentsMethods = paymentsMethodRepository.findAllByBill(bill);

        NumberFormat formatter = formatCurrency();
        InvoiceResponse invoice = InvoiceResponse.builder()
                .phoneNumber(bill.getPhoneNumber())
                .address(bill.getAddress())
                .userName(bill.getUserName())
                .code(bill.getCode())
                .ship(formatter.format(bill.getMoneyShip()))
                .itemDiscount(formatter.format(bill.getItemDiscount()))
                .totalMoney(formatter.format(bill.getTotalMoney()))
                .note(bill.getNote())
                .checkShip(false)
                .moneyShip(formatter.format(bill.getMoneyShip()))
                .build();
        List<String> findAllPayMentByIdBillAndMethod = paymentsMethodRepository.findAllPayMentByIdBillAndMethod(bill.getId());
        if(bill.getTotalMoney().add(bill.getMoneyShip()).subtract(bill.getItemDiscount()).compareTo(BigDecimal.ZERO) > 0 ){
            invoice.setTotalBill(formatter.format(bill.getTotalMoney().add(bill.getMoneyShip()).subtract(bill.getItemDiscount())));
        }else{
            invoice.setTotalBill("0 đ");
        }
        if(billDetailRepository.quantityProductByIdBill(bill.getId()) != null){
            invoice.setQuantity(Integer.valueOf(billDetailRepository.quantityProductByIdBill(bill.getId())));
        }
        List<InvoiceItemResponse> items = new ArrayList<>();
        billDetailResponses.forEach(billDetailRequest -> {
            InvoiceItemResponse invoiceItemResponse = InvoiceItemResponse.builder()
                    .sum(formatter.format(billDetailRequest.getPrice().multiply(new BigDecimal(billDetailRequest.getQuantity()))))
                    .name(billDetailRequest.getProductName())
                    .priceVn(formatter.format(billDetailRequest.getPrice()))
                    .quantity(billDetailRequest.getQuantity())
                    .promotion(billDetailRequest.getPromotion())
                    .build();
            if(billDetailRequest.getPromotion() != null){
                invoiceItemResponse.setPriceBeforePromotion(formatter.format(billDetailRequest.getPrice().multiply(BigDecimal.ONE.subtract(new BigDecimal(billDetailRequest.getPromotion()).divide(BigDecimal.valueOf(100))))));
            }
            items.add(invoiceItemResponse);
        });
        List<InvoicePaymentResponse> paymentsMethodRequests = new ArrayList<>();

        paymentsMethods.forEach(item -> {
            InvoicePaymentResponse invoicePaymentResponse = InvoicePaymentResponse.builder()
                    .total(formatter.format(item.getTotalMoney()))
                    .method(item.getMethod() == StatusMethod.TIEN_MAT ? "Tiền mặt" : item.getMethod() == StatusMethod.CHUYEN_KHOAN ? "Chuyển khoản": "Thẻ")
                    .status(item.getStatus() == StatusPayMents.THANH_TOAN ? "Thanh toán" : item.getStatus() == StatusPayMents.TRA_SAU ? "Trả sau" : "Hoàn tiền" )
                    .vnp_TransactionNo(item.getVnp_TransactionNo())
                    .build();
            paymentsMethodRequests.add(invoicePaymentResponse);
        });
        BigDecimal totalPayMnet = paymentsMethodRepository.sumTotalMoneyByIdBill(bill.getId());
        invoice.setTotalPayment(formatter.format(totalPayMnet));
        invoice.setChange(formatter.format(totalPayMnet.subtract(bill.getTotalMoney().add(bill.getMoneyShip()).subtract(bill.getItemDiscount()))));
        invoice.setPaymentsMethodRequests(paymentsMethodRequests);
        invoice.setItems(items);


        if(findAllPayMentByIdBillAndMethod.size() > 0 ){
            invoice.setMethod(true);
        }else{
            invoice.setMethod(false);
        }
        invoice.setTypeBill(false);
        Date date = new Date(bill.getCreatedDate());

        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = formatterDate.format(date);
        invoice.setDate(formattedDate);
        return invoice;
    }

}
