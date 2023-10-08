package com.example.shose.server.service.impl;


import com.example.shose.server.dto.request.bill.BillRequest;
import com.example.shose.server.dto.request.bill.ChangAllStatusBillByIdsRequest;
import com.example.shose.server.dto.request.bill.ChangStatusBillRequest;
import com.example.shose.server.dto.request.bill.CreateBillOfflineRequest;
import com.example.shose.server.dto.request.bill.CreateBillRequest;
import com.example.shose.server.dto.request.bill.FindNewBillCreateAtCounterRequest;
import com.example.shose.server.dto.request.bill.UpdateBillRequest;
import com.example.shose.server.dto.request.bill.billaccount.CreateBillAccountOnlineRequest;
import com.example.shose.server.dto.request.bill.billcustomer.BillDetailOnline;
import com.example.shose.server.dto.request.bill.BillRequest;
import com.example.shose.server.dto.request.bill.ChangAllStatusBillByIdsRequest;
import com.example.shose.server.dto.request.bill.ChangStatusBillRequest;
import com.example.shose.server.dto.request.bill.CreateBillOfflineRequest;
import com.example.shose.server.dto.request.bill.CreateBillRequest;
import com.example.shose.server.dto.request.bill.FindNewBillCreateAtCounterRequest;
import com.example.shose.server.dto.request.bill.UpdateBillRequest;
import com.example.shose.server.dto.request.bill.billcustomer.CreateBillCustomerOnlineRequest;
import com.example.shose.server.dto.response.bill.BillResponse;
import com.example.shose.server.dto.response.bill.BillResponseAtCounter;
import com.example.shose.server.dto.response.bill.UserBillResponse;
import com.example.shose.server.dto.response.billdetail.BillDetailResponse;
import com.example.shose.server.entity.Account;
import com.example.shose.server.entity.Address;
import com.example.shose.server.entity.Bill;
import com.example.shose.server.entity.BillDetail;
import com.example.shose.server.entity.BillHistory;
import com.example.shose.server.entity.Cart;
import com.example.shose.server.entity.CartDetail;
import com.example.shose.server.entity.PaymentsMethod;
import com.example.shose.server.entity.ProductDetail;
import com.example.shose.server.entity.User;
import com.example.shose.server.entity.Voucher;
import com.example.shose.server.entity.VoucherDetail;
import com.example.shose.server.infrastructure.constant.Message;
import com.example.shose.server.infrastructure.constant.StatusBill;
import com.example.shose.server.infrastructure.constant.StatusMethod;
import com.example.shose.server.infrastructure.constant.StatusPayMents;
import com.example.shose.server.infrastructure.constant.TypeBill;
import com.example.shose.server.infrastructure.constant.Message;
import com.example.shose.server.infrastructure.constant.Roles;
import com.example.shose.server.infrastructure.constant.Status;
import com.example.shose.server.infrastructure.constant.StatusBill;
import com.example.shose.server.infrastructure.constant.StatusMethod;
import com.example.shose.server.infrastructure.constant.StatusPayMents;
import com.example.shose.server.infrastructure.constant.TypeBill;
import com.example.shose.server.infrastructure.exception.rest.RestApiException;
import com.example.shose.server.repository.AccountRepository;
import com.example.shose.server.repository.AddressRepository;
import com.example.shose.server.repository.BillDetailRepository;
import com.example.shose.server.repository.BillHistoryRepository;
import com.example.shose.server.repository.BillRepository;
import com.example.shose.server.repository.CartDetailRepository;
import com.example.shose.server.repository.CartRepository;
import com.example.shose.server.repository.CustomerRepository;
import com.example.shose.server.repository.PaymentsMethodRepository;
import com.example.shose.server.repository.ProductDetailRepository;
import com.example.shose.server.repository.UserReposiory;
import com.example.shose.server.repository.VoucherDetailRepository;
import com.example.shose.server.repository.VoucherRepository;
import com.example.shose.server.service.BillService;
import com.example.shose.server.util.ConvertDateToLong;
import com.example.shose.server.util.RandomNumberGenerator;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;




@Service
@Transactional
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BillHistoryRepository billHistoryRepository;

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private PaymentsMethodRepository paymentsMethodRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherDetailRepository voucherDetailRepository;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserReposiory userReposiory;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Override
    public List<BillResponse> getAll(BillRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        request.setConverStatus(Arrays.toString(request.getStatus()));
        try {
            if (!request.getStartTimeString().isEmpty()) {
                request.setStartTime(simpleDateFormat.parse(request.getStartTimeString()).getTime());
            }
            if (!request.getEndTimeString().isEmpty()) {
                request.setEndTime(simpleDateFormat.parse(request.getEndTimeString()).getTime());
            }
            if (!request.getStartDeliveryDateString().isEmpty()) {
                request.setStartDeliveryDate(simpleDateFormat.parse(request.getStartDeliveryDateString()).getTime());
            }
            if (!request.getEndDeliveryDateString().isEmpty()) {
                request.setEndDeliveryDate(simpleDateFormat.parse(request.getEndDeliveryDateString()).getTime());
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return billRepository.getAll(request);
    }

    @Override
    public List<UserBillResponse> getAllUserInBill() {

        Map<String, UserBillResponse> list = new HashMap<>();
        billRepository.getAllUserInBill().forEach(item -> {
            list.put(item.getUserName(), item);
        });
        List<UserBillResponse> users = new ArrayList<>(list.values());
        return users;
    }

    @Override
    public List<BillResponseAtCounter> findAllBillAtCounterAndStatusNewBill(FindNewBillCreateAtCounterRequest request) {
        return billRepository.findAllBillAtCounterAndStatusNewBill(request);
    }

    @Override
    public Bill save(String id, CreateBillOfflineRequest request) {
        Optional<Bill> optional = billRepository.findByCode(request.getCode());
        if (!optional.isPresent()) {
            throw new RestApiException(Message.NOT_EXISTS);
        }
        optional.get().setNote(request.getNote());
        optional.get().setUserName(request.getUserName());
        optional.get().setAddress(request.getAddress());
        optional.get().setPhoneNumber(request.getPhoneNumber());
        optional.get().setItemDiscount(new BigDecimal(request.getItemDiscount()));
        optional.get().setTotalMoney(new BigDecimal(request.getTotalMoney()));
        optional.get().setMoneyShip(new BigDecimal(request.getMoneyShip()));

        List<BillDetailResponse> billDetailResponse = billDetailRepository.findAllByIdBill(optional.get().getId());
        billDetailResponse.forEach(item -> {
            Optional<ProductDetail> productDetail = productDetailRepository.findById(item.getIdProduct());
            if (!productDetail.isPresent()) {
                throw new RestApiException(Message.NOT_EXISTS);
            }

            productDetail.get().setQuantity(item.getQuantity() + productDetail.get().getQuantity());
            productDetailRepository.save(productDetail.get());
        });
        billHistoryRepository.deleteAllByIdBill(optional.get().getId());
        billDetailRepository.deleteAllByIdBill(optional.get().getId());
        paymentsMethodRepository.deleteAllByIdBill(optional.get().getId());
        voucherDetailRepository.deleteAllByIdBill(optional.get().getId());


        if (request.getIdUser() != null) {
            Optional<Account> user = accountRepository.findById(request.getIdUser());
            if (user.isPresent()) {
                optional.get().setAccount(user.get());
            }
        }
        if (!request.getDeliveryDate().isEmpty()) {
            optional.get().setDeliveryDate(new ConvertDateToLong().dateToLong(request.getDeliveryDate()));
        }
        if (TypeBill.valueOf(request.getTypeBill()) != TypeBill.OFFLINE || !request.isOpenDelivery()) {
            optional.get().setStatusBill(StatusBill.THANH_CONG);
            optional.get().setCompletionDate(Calendar.getInstance().getTimeInMillis());
            billRepository.save(optional.get());
            billHistoryRepository.save(BillHistory.builder().statusBill(optional.get().getStatusBill()).bill(optional.get()).employees(optional.get().getEmployees()).build());
        } else {
            optional.get().setStatusBill(StatusBill.CHO_VAN_CHUYEN);
            optional.get().setCompletionDate(Calendar.getInstance().getTimeInMillis());
            billRepository.save(optional.get());
            billHistoryRepository.save(BillHistory.builder().statusBill(StatusBill.XAC_NHAN).bill(optional.get()).employees(optional.get().getEmployees()).build());
            billHistoryRepository.save(BillHistory.builder().statusBill(StatusBill.CHO_VAN_CHUYEN).bill(optional.get()).employees(optional.get().getEmployees()).build());

        }

        request.getPaymentsMethodRequests().forEach(item -> {
            if (item.getMethod() != StatusMethod.CHUYEN_KHOAN && item.getTotalMoney() != null) {
                if (item.getTotalMoney().signum() != 0) {
                    PaymentsMethod paymentsMethod = PaymentsMethod.builder()
                            .method(item.getMethod())
                            .status(StatusPayMents.valueOf(request.getStatusPayMents()))
                            .employees(optional.get().getEmployees())
                            .totalMoney(item.getTotalMoney())
                            .description(item.getActionDescription())
                            .bill(optional.get())
                            .build();
                    paymentsMethodRepository.save(paymentsMethod);
                }
            }
        });

        request.getBillDetailRequests().forEach(billDetailRequest -> {
            Optional<ProductDetail> productDetail = productDetailRepository.findById(billDetailRequest.getIdProduct());
            if (!productDetail.isPresent()) {
                throw new RestApiException(Message.NOT_EXISTS);
            }
            if (productDetail.get().getQuantity() < billDetailRequest.getQuantity()) {
                throw new RestApiException(Message.ERROR_QUANTITY);
            }
            BillDetail billDetail = BillDetail.builder().statusBill(StatusBill.TAO_HOA_DON).bill(optional.get()).productDetail(productDetail.get()).price(new BigDecimal(billDetailRequest.getPrice())).quantity(billDetailRequest.getQuantity()).build();
            billDetailRepository.save(billDetail);
            productDetail.get().setQuantity(productDetail.get().getQuantity() - billDetailRequest.getQuantity());
            productDetailRepository.save(productDetail.get());
        });
        request.getVouchers().forEach(voucher -> {
            Optional<Voucher> Voucher = voucherRepository.findById(voucher.getIdVoucher());
            if (!Voucher.isPresent()) {
                throw new RestApiException(Message.NOT_EXISTS);
            }
            if (Voucher.get().getQuantity() <= 0 && Voucher.get().getEndDate() < Calendar.getInstance().getTimeInMillis()) {
                throw new RestApiException(Message.VOUCHER_NOT_USE);
            }
            Voucher.get().setQuantity(Voucher.get().getQuantity() - 1);
            voucherRepository.save(Voucher.get());

            VoucherDetail voucherDetail = VoucherDetail.builder().voucher(Voucher.get()).bill(optional.get()).afterPrice(new BigDecimal(voucher.getAfterPrice())).beforPrice(new BigDecimal(voucher.getBeforPrice())).discountPrice(new BigDecimal(voucher.getDiscountPrice())).build();
            voucherDetailRepository.save(voucherDetail);
        });

        return optional.get();
    }

    @Override
    public Bill saveOnline(CreateBillRequest request) {
        Optional<Account> account = accountRepository.findById(request.getIdUser());
        if (!account.isPresent()) {
            throw new RestApiException(Message.ACCOUNT_NOT_EXIT);
        }
        Bill bill = billRepository.save(Bill.builder().account(account.get()).userName(request.getName()).address(request.getAddress()).phoneNumber(request.getPhoneNumber()).statusBill(StatusBill.TAO_HOA_DON).typeBill(TypeBill.OFFLINE).code("HD" + RandomStringUtils.randomNumeric(6)).build());
        billHistoryRepository.save(BillHistory.builder().statusBill(bill.getStatusBill()).bill(bill).build());
        return bill;
    }

    @Override
    public Bill CreateCodeBill(String idEmployees) {
        Optional<Account> account = accountRepository.findById(idEmployees);
//        if (account.get().getRoles() == Roles.USER) {
//        }
        Bill bill = Bill.builder()
                .employees(account.get())
                .typeBill(TypeBill.OFFLINE)
                .statusBill(StatusBill.TAO_HOA_DON)
                .code("HD" + RandomStringUtils.randomNumeric(6))
                .itemDiscount(new BigDecimal("0"))
                .totalMoney(new BigDecimal("0"))
                .moneyShip(new BigDecimal("0")).build();
        billRepository.save(bill);
        billHistoryRepository.save(BillHistory.builder().statusBill(bill.getStatusBill()).bill(bill).employees(bill.getEmployees()).build());
        return bill;
    }

    @Override
    public boolean updateBillWait(CreateBillOfflineRequest request) {
        Optional<Bill> optional = billRepository.findByCode(request.getCode());
        if (!optional.isPresent()) {
            throw new RestApiException(Message.NOT_EXISTS);
        }
        optional.get().setNote(request.getNote());
        optional.get().setUserName(request.getUserName());
        optional.get().setAddress(request.getAddress());
        optional.get().setPhoneNumber(request.getPhoneNumber());
        optional.get().setItemDiscount(new BigDecimal(request.getItemDiscount()));
        optional.get().setTotalMoney(new BigDecimal(request.getTotalMoney()));
        optional.get().setTotalMoney(new BigDecimal(request.getMoneyShip()));
        billRepository.save(optional.get());

        List<BillDetailResponse> billDetailResponse = billDetailRepository.findAllByIdBill(optional.get().getId());
        billDetailResponse.forEach(item -> {
            Optional<ProductDetail> productDetail = productDetailRepository.findById(item.getIdProduct());
            if (!productDetail.isPresent()) {
                throw new RestApiException(Message.NOT_EXISTS);
            }
            productDetail.get().setQuantity(item.getQuantity() + productDetail.get().getQuantity());
            productDetailRepository.save(productDetail.get());
        });
        billDetailRepository.deleteAllByIdBill(optional.get().getId());
        paymentsMethodRepository.deleteAllByIdBill(optional.get().getId());
        voucherDetailRepository.deleteAllByIdBill(optional.get().getId());


        if (request.getIdUser() != null) {
            Optional<Account> user = accountRepository.findById(request.getIdUser());

            if (user.isPresent()) {
                optional.get().setAccount(user.get());
            }
        }
        if (!request.getDeliveryDate().isEmpty()) {
            optional.get().setDeliveryDate(new ConvertDateToLong().dateToLong(request.getDeliveryDate()));
        }
        optional.get().setStatusBill(StatusBill.TAO_HOA_DON);
        billRepository.save(optional.get());
        request.getPaymentsMethodRequests().forEach(item -> {
            if (item != null) {
                if (item.getMethod() != StatusMethod.CHUYEN_KHOAN) {
                    PaymentsMethod paymentsMethod = PaymentsMethod.builder()
                            .method(item.getMethod())
                            .status(StatusPayMents.valueOf(request.getStatusPayMents()))
                            .employees(optional.get().getEmployees())
                            .totalMoney(item.getTotalMoney())
                            .description(item.getActionDescription())
                            .bill(optional.get())
                            .build();
                    paymentsMethodRepository.save(paymentsMethod);
                }
            }

        });
        billRepository.save(optional.get());

        billDetailResponse.forEach(item -> {
            Optional<ProductDetail> productDetail = productDetailRepository.findById(item.getIdProduct());
            if (!productDetail.isPresent()) {
                throw new RestApiException(Message.NOT_EXISTS);
            }
            productDetail.get().setQuantity(item.getQuantity() + productDetail.get().getQuantity());
            productDetailRepository.save(productDetail.get());
        });
        billDetailRepository.deleteAllByIdBill(optional.get().getId());
        paymentsMethodRepository.deleteAllByIdBill(optional.get().getId());
        voucherDetailRepository.deleteAllByIdBill(optional.get().getId());

        request.getBillDetailRequests().forEach(billDetailRequest -> {
            Optional<ProductDetail> productDetail = productDetailRepository.findById(billDetailRequest.getIdProduct());
            if (!productDetail.isPresent()) {
                throw new RestApiException(Message.NOT_EXISTS);
            }
            if (productDetail.get().getQuantity() < billDetailRequest.getQuantity()) {
                throw new RestApiException(Message.ERROR_QUANTITY);
            }
            BillDetail billDetail = BillDetail.builder().statusBill(StatusBill.TAO_HOA_DON).bill(optional.get()).productDetail(productDetail.get()).price(new BigDecimal(billDetailRequest.getPrice())).quantity(billDetailRequest.getQuantity()).build();
            billDetailRepository.save(billDetail);
            productDetail.get().setQuantity(productDetail.get().getQuantity() - billDetailRequest.getQuantity());
            if (productDetail.get().getQuantity() == 0) {
                productDetail.get().setStatus(Status.HET_SAN_PHAM);
            }
            productDetailRepository.save(productDetail.get());
        });
        request.getVouchers().forEach(voucher -> {
            Optional<Voucher> Voucher = voucherRepository.findById(voucher.getIdVoucher());
            if (!Voucher.isPresent()) {
                throw new RestApiException(Message.NOT_EXISTS);
            }
            if (Voucher.get().getQuantity() <= 0 && Voucher.get().getEndDate() < Calendar.getInstance().getTimeInMillis()) {
                throw new RestApiException(Message.VOUCHER_NOT_USE);
            }
            Voucher.get().setQuantity(Voucher.get().getQuantity() - 1);
            voucherRepository.save(Voucher.get());

            VoucherDetail voucherDetail = VoucherDetail.builder().voucher(Voucher.get()).bill(optional.get()).afterPrice(new BigDecimal(voucher.getAfterPrice())).beforPrice(new BigDecimal(voucher.getBeforPrice())).discountPrice(new BigDecimal(voucher.getDiscountPrice())).build();
            voucherDetailRepository.save(voucherDetail);
        });

        request.getPaymentsMethodRequests().forEach(item -> {
            if (item.getMethod() != StatusMethod.CHUYEN_KHOAN && item.getTotalMoney() != null) {
                if (item.getTotalMoney().signum() != 0) {
                    PaymentsMethod paymentsMethod = PaymentsMethod.builder()
                            .method(item.getMethod())
                            .status(StatusPayMents.valueOf(request.getStatusPayMents()))
                            .employees(optional.get().getEmployees())
                            .totalMoney(item.getTotalMoney())
                            .description(item.getActionDescription())
                            .bill(optional.get())
                            .build();
                    paymentsMethodRepository.save(paymentsMethod);
                }
            }
        });
        return true;
    }

    @Override
    public Bill updateBillOffline(String id, UpdateBillRequest request) {
        Optional<Bill> updateBill = billRepository.findById(id);
        if (!updateBill.isPresent()) {
            throw new RestApiException(Message.BILL_NOT_EXIT);
        }
        updateBill.get().setMoneyShip(new BigDecimal(request.getMoneyShip()));
        updateBill.get().setAddress(request.getAddress().trim());
        updateBill.get().setUserName(request.getName().trim());
        updateBill.get().setPhoneNumber(request.getPhoneNumber().trim());
        updateBill.get().setNote(request.getNote().trim());
        return billRepository.save(updateBill.get());
    }

    @Override
    public Bill detail(String id) {
        Optional<Bill> bill = billRepository.findById(id);
        if (!bill.isPresent()) {
            throw new RestApiException(Message.BILL_NOT_EXIT);
        }
        return bill.get();
    }

    @Override
    public Bill changedStatusbill(String id, String idEmployees, ChangStatusBillRequest request) {
        Optional<Bill> bill = billRepository.findById(id);
        Optional<Account> account = accountRepository.findById(idEmployees);
        if (!bill.isPresent()) {
            throw new RestApiException(Message.BILL_NOT_EXIT);
        }
        if (!account.isPresent()) {
            throw new RestApiException(Message.NOT_EXISTS);
        }
        StatusBill statusBill[] = StatusBill.values();
        int nextIndex = (bill.get().getStatusBill().ordinal() + 1) % statusBill.length;
        bill.get().setStatusBill(StatusBill.valueOf(statusBill[nextIndex].name()));
        if (nextIndex > 6) {
            throw new RestApiException(Message.CHANGED_STATUS_ERROR);
        }
//        if (StatusBill.valueOf(statusBill[nextIndex].name()) ==  StatusBill.DA_THANH_TOAN && ) {
//            throw new RestApiException(Message.CHANGED_STATUS_ERROR);
//        }
        if (bill.get().getStatusBill() == StatusBill.CHO_XAC_NHAN) {
            bill.get().setConfirmationDate(Calendar.getInstance().getTimeInMillis());
        } else if (bill.get().getStatusBill() == StatusBill.VAN_CHUYEN) {
            bill.get().setDeliveryDate(Calendar.getInstance().getTimeInMillis());
        } else if (bill.get().getStatusBill() == StatusBill.DA_THANH_TOAN) {
            bill.get().setReceiveDate(Calendar.getInstance().getTimeInMillis());
//            if(bill.get().getTotalMoney().compareTo(new BigDecimal(request.getTotalMoney())) > 0){
//                throw new RestApiException(Message.ERROR_TOTALMONEY);
//            }
//            int checkPayMent = paymentsMethodRepository.countPayMentPostpaidByIdBill(id);
//            if(checkPayMent == 0){
//                PaymentsMethod paymentsMethod = PaymentsMethod.builder().method(request.getMethod()).employees(account.get()).bill(bill.get()).description(request.getActionDescription()).totalMoney(new BigDecimal(request.getTotalMoney())).build();
//                paymentsMethodRepository.save(paymentsMethod);
//            }
            

        } else if (bill.get().getStatusBill() == StatusBill.THANH_CONG) {
            paymentsMethodRepository.updateAllByIdBill(id);
            bill.get().setCompletionDate(Calendar.getInstance().getTimeInMillis());
        }

        BillHistory billHistory = new BillHistory();
        billHistory.setBill(bill.get());
        billHistory.setStatusBill(StatusBill.valueOf(statusBill[nextIndex].name()));
        billHistory.setActionDescription(request.getActionDescription());
        billHistory.setEmployees(account.get());
        billHistoryRepository.save(billHistory);

//        if (bill.get().getStatusBill() == StatusBill.VAN_CHUYEN && paymentsMethodRepository.countPayMentPostpaidByIdBill(id) == 0) {
//            bill.get().setStatusBill(StatusBill.DA_THANH_TOAN);
//            BillHistory billHistoryPayMent = new BillHistory();
//            billHistoryPayMent.setBill(bill.get());
//            billHistoryPayMent.setStatusBill(StatusBill.DA_THANH_TOAN);
//            billHistoryPayMent.setActionDescription(request.getActionDescription());
//            billHistoryPayMent.setEmployees(account.get());
//            billHistoryRepository.save(billHistoryPayMent);
//        }
        return billRepository.save(bill.get());
    }

    @Override
    public int countPayMentPostpaidByIdBill(String id) {
        return paymentsMethodRepository.countPayMentPostpaidByIdBill(id);
    }

    @Override
    public boolean changeStatusAllBillByIds(ChangAllStatusBillByIdsRequest request, String idEmployees) {
        request.getIds().forEach(id -> {
            Optional<Bill> bill = billRepository.findById(id);
            Optional<Account> account = accountRepository.findById(idEmployees);
            if (!bill.isPresent()) {
                throw new RestApiException(Message.BILL_NOT_EXIT);
            }
            if (!account.isPresent()) {
                throw new RestApiException(Message.NOT_EXISTS);
            }
            bill.get().setStatusBill(StatusBill.valueOf(request.getStatus()));
            if (bill.get().getStatusBill() == StatusBill.CHO_XAC_NHAN) {
                bill.get().setConfirmationDate(Calendar.getInstance().getTimeInMillis());
            } else if (bill.get().getStatusBill() == StatusBill.VAN_CHUYEN) {
                bill.get().setDeliveryDate(Calendar.getInstance().getTimeInMillis());
            } else if (bill.get().getStatusBill() == StatusBill.DA_THANH_TOAN) {
                bill.get().setReceiveDate(Calendar.getInstance().getTimeInMillis());
            } else if (bill.get().getStatusBill() == StatusBill.THANH_CONG) {
                paymentsMethodRepository.updateAllByIdBill(id);
                bill.get().setCompletionDate(Calendar.getInstance().getTimeInMillis());
            }
            BillHistory billHistory = new BillHistory();
            billHistory.setBill(bill.get());
            billHistory.setStatusBill(StatusBill.valueOf(request.getStatus()));
//            billHistoryPayMent.setActionDescription(request.getActionDescription());
            billHistory.setEmployees(account.get());
            billHistoryRepository.save(billHistory);
//            if (bill.get().getStatusBill() == StatusBill.VAN_CHUYEN && paymentsMethodRepository.countPayMentPostpaidByIdBill(id) == 0) {
//                bill.get().setStatusBill(StatusBill.DA_THANH_TOAN);
//                BillHistory billHistoryPayMent = new BillHistory();
//                billHistoryPayMent.setBill(bill.get());
//                billHistoryPayMent.setStatusBill(StatusBill.DA_THANH_TOAN);
////                billHistoryPayMent.setActionDescription(request.getActionDescription());
//                billHistoryPayMent.setEmployees(account.get());
//                billHistoryRepository.save(billHistoryPayMent);
//            }


            billRepository.save(bill.get());
        });
        return true;
    }

    @Override
    public Bill cancelBill(String id, String idEmployees, ChangStatusBillRequest request) {
        Optional<Bill> bill = billRepository.findById(id);
        Optional<Account> account = accountRepository.findById(idEmployees);
        if (!bill.isPresent()) {
            throw new RestApiException(Message.BILL_NOT_EXIT);
        }
        if (!account.isPresent()) {
            throw new RestApiException(Message.NOT_EXISTS);
        }
        bill.get().setStatusBill(StatusBill.DA_HUY);
        BillHistory billHistory = new BillHistory();
        billHistory.setBill(bill.get());
        billHistory.setStatusBill(bill.get().getStatusBill());
        billHistory.setActionDescription(request.getActionDescription());
        billHistory.setEmployees(account.get());
        billHistoryRepository.save(billHistory);
        return billRepository.save(bill.get());
    }

    @Override
    public String createBillCustomerOnlineRequest(CreateBillCustomerOnlineRequest request) {
        User user = User.builder()
                .fullName(request.getUserName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .status(Status.DANG_SU_DUNG)
                .points(0).build();
        userReposiory.save(user);
        Account account = Account.builder()
                .user(user)
                .email(request.getEmail())
                .status(Status.DANG_SU_DUNG)
                .password(new RandomNumberGenerator().randomPassword())
                .roles(Roles.USER).build();

        accountRepository.save(account);
        Address address = Address.builder()
                .status(Status.DANG_SU_DUNG)
                .user(user)
                .line(request.getAddress())
                .ward(request.getWard())
                .district(request.getDistrict())
                .province(request.getProvince())
                .provinceId(request.getProvinceId())
                .wardCode(request.getWardCode())
                .toDistrictId(request.getDistrictId()).build();
        addressRepository.save(address);

        Bill bill = Bill.builder()
                .code(new RandomNumberGenerator().randomToString("HD"))
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress() + ',' + request.getWard() + '-' + request.getDistrict() + '-' + request.getProvince())
                .userName(request.getUserName())
                .moneyShip(request.getMoneyShip())
                .itemDiscount(request.getItemDiscount())
                .totalMoney(request.getTotalMoney())
                .typeBill(TypeBill.ONLINE)
                .statusBill(StatusBill.CHO_XAC_NHAN)
                .account(account).build();
        billRepository.save(bill);
        BillHistory billHistory = BillHistory.builder()
                .bill(bill)
                .statusBill(StatusBill.CHO_XAC_NHAN)
                .actionDescription(request.getPaymentMethod().equals("paymentReceive") ? "Chưa thanh toán" : "Đã thanh toán").build();
        billHistoryRepository.save(billHistory);

        for (BillDetailOnline x : request.getBillDetail()) {
            ProductDetail productDetail = productDetailRepository.findById(x.getIdProductDetail()).get();
            BillDetail billDetail = BillDetail.builder()
                    .statusBill(StatusBill.CHO_XAC_NHAN)
                    .productDetail(productDetail)
                    .price(x.getPrice())
                    .quantity(x.getQuantity())
                    .bill(bill).build();
            billDetailRepository.save(billDetail);
        }

        PaymentsMethod paymentsMethod = PaymentsMethod.builder()
                .method(request.getPaymentMethod().equals("paymentReceive") ? StatusMethod.TIEN_MAT : StatusMethod.CHUYEN_KHOAN)
                .bill(bill)
                .totalMoney(request.getTotalMoney())
                .status(StatusPayMents.THANH_TOAN).build();

        paymentsMethodRepository.save(paymentsMethod);

        if (!request.getIdVoucher().isEmpty()) {
            Voucher voucher = voucherRepository.findById(request.getIdVoucher()).get();

            VoucherDetail voucherDetail = VoucherDetail.builder()
                    .voucher(voucher)
                    .bill(bill)
                    .beforPrice(request.getTotalMoney())
                    .afterPrice(request.getAfterPrice())
                    .discountPrice(request.getItemDiscount())
                    .build();
            voucherDetailRepository.save(voucherDetail);
        }


        return "thanh toán ok";
    }

    @Override
    public String createBillAccountOnlineRequest(CreateBillAccountOnlineRequest request) {

        Account account = accountRepository.findById(request.getIdAccount()).get();
        Bill bill = Bill.builder()
                .code(new RandomNumberGenerator().randomToString("Bill"))
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .userName(request.getUserName())
                .moneyShip(request.getMoneyShip())
                .itemDiscount(request.getItemDiscount())
                .totalMoney(request.getTotalMoney())
                .typeBill(TypeBill.ONLINE)
                .statusBill(StatusBill.DA_THANH_TOAN)
                .account(account).build();
        billRepository.save(bill);
        BillHistory billHistory = BillHistory.builder()
                .bill(bill)
                .statusBill(request.getPaymentMethod().equals("paymentReceive") ? StatusBill.CHO_XAC_NHAN : StatusBill.DA_THANH_TOAN)
                .actionDescription("Đã thanh toán").build();
        billHistoryRepository.save(billHistory);

        for (BillDetailOnline x : request.getBillDetail()) {
            ProductDetail productDetail = productDetailRepository.findById(x.getIdProductDetail()).get();
            BillDetail billDetail = BillDetail.builder()
                    .statusBill(request.getPaymentMethod().equals("paymentReceive") ? StatusBill.CHO_XAC_NHAN : StatusBill.DA_THANH_TOAN)
                    .productDetail(productDetail)
                    .price(x.getPrice())
                    .quantity(x.getQuantity())
                    .bill(bill).build();
            billDetailRepository.save(billDetail);
        }

        PaymentsMethod paymentsMethod = PaymentsMethod.builder()
                .method(request.getPaymentMethod().equals("paymentReceive") ? StatusMethod.TIEN_MAT : StatusMethod.CHUYEN_KHOAN)
                .bill(bill)
                .totalMoney(request.getTotalMoney())
                .status(StatusPayMents.THANH_TOAN).build();

        paymentsMethodRepository.save(paymentsMethod);

        if (!request.getIdVoucher().isEmpty()) {
            Voucher voucher = voucherRepository.findById(request.getIdVoucher()).get();

            VoucherDetail voucherDetail = VoucherDetail.builder()
                    .voucher(voucher)
                    .bill(bill)
                    .beforPrice(request.getTotalMoney())
                    .afterPrice(request.getAfterPrice())
                    .discountPrice(request.getItemDiscount())
                    .build();
            voucherDetailRepository.save(voucherDetail);
        }

        Cart cart = cartRepository.getCartByAccount_Id(request.getIdAccount());
        for (BillDetailOnline x : request.getBillDetail()) {
            List<CartDetail> cartDetail = cartDetailRepository.getCartDetailByCart_IdAndProductDetail_Id(cart.getId(), x.getIdProductDetail());
            cartDetail.forEach(detail -> cartDetailRepository.deleteById(detail.getId()));
        }
        return "thanh toán ok";
    }

}
