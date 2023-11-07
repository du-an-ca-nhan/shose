package com.example.shose.server.service.impl;

import com.example.shose.server.dto.request.statistical.FindBillDateRequest;
import com.example.shose.server.dto.response.statistical.StatisticalBestSellingProductResponse;
import com.example.shose.server.dto.response.statistical.StatisticalBillDateResponse;
import com.example.shose.server.dto.response.statistical.StatisticalDayResponse;
import com.example.shose.server.dto.response.statistical.StatisticalMonthlyResponse;
import com.example.shose.server.dto.response.statistical.StatisticalProductDateResponse;
import com.example.shose.server.dto.response.statistical.StatisticalStatusBillResponse;
import com.example.shose.server.repository.BillRepository;
import com.example.shose.server.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


@Service
public class StatisticalServiceImpl implements StatisticalService {
    // Lấy thời điểm hiện tại
    private long currentTimeMillis = System.currentTimeMillis();

    // Chuyển đổi thời điểm hiện tại sang đối tượng Date
    private Date currentDate = new Date(currentTimeMillis);
    @Autowired
    private BillRepository billRepository;

    @Override
    public List<StatisticalDayResponse> getAllStatisticalDay() {
        // Tạo đối tượng Calendar và đặt giờ, phút, giây, mili-giây thành 0
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC")); // Để tránh ảnh hưởng của múi giờ
        calendar.setTime(currentDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Lấy thời điểm 00:00 của ngày hôm nay dưới dạng currentTimeMillis
        long startOfDayMillis = calendar.getTimeInMillis();
        return billRepository.getAllStatisticalDay(startOfDayMillis);
    }

    @Override
    public List<StatisticalMonthlyResponse> getAllStatisticalMonth() {
        return billRepository.getAllStatisticalMonthly(getStartMonth(), getEndMonth());
    }

    @Override
    public List<StatisticalStatusBillResponse> getAllStatisticalStatusBill() {
        return billRepository.getAllStatisticalStatusBill();
    }

    @Override
    public List<StatisticalBestSellingProductResponse> getAllStatisticalBestSellingProduct() {
        return billRepository.getAllStatisticalBestSellingProduct();
    }

    @Override
    public List<StatisticalBillDateResponse> getAllStatisticalBillDate(FindBillDateRequest req) {
        if (req.getStartDate() == null && req.getEndDate() == null) {
            req.setStartDate(getStartMonth());
            req.setEndDate(getEndMonth());
            return billRepository.getAllStatisticalBillDate(req);
        } else if (req.getStartDate() == null && req.getEndDate() != null) {
            req.setStartDate(getStartMonth());
            return billRepository.getAllStatisticalBillDate(req);
        } else if (req.getStartDate() != null && req.getEndDate() == null) {
            req.setEndDate(getEndMonth());
            return billRepository.getAllStatisticalBillDate(req);
        } else {
            return billRepository.getAllStatisticalBillDate(req);
        }
    }

    @Override
    public List<StatisticalProductDateResponse> getAllStatisticalProductDate(FindBillDateRequest req) {
        if (req.getStartDate() == null && req.getEndDate() == null) {
            req.setStartDate(getStartMonth());
            req.setEndDate(getEndMonth());
            return billRepository.getAllStatisticalProductDate(req);
        } else if (req.getStartDate() == null && req.getEndDate() != null) {
            req.setStartDate(getStartMonth());
            return billRepository.getAllStatisticalProductDate(req);
        } else if (req.getStartDate() != null && req.getEndDate() == null) {
            req.setEndDate(getEndMonth());
            return billRepository.getAllStatisticalProductDate(req);
        } else {
            return billRepository.getAllStatisticalProductDate(req);
        }
    }

    public Long getStartMonth() {
        // Tạo đối tượng Calendar và đặt ngày trong tháng thành 1
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeZone(TimeZone.getTimeZone("UTC")); // Để tránh ảnh hưởng của múi giờ
        calendarStart.setTime(currentDate);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);

        // Lấy thời điểm đầu tháng dưới dạng currentTimeMillis
        return calendarStart.getTimeInMillis();


    }

    public Long getEndMonth() {
        // Tạo đối tượng Calendar và đặt ngày trong tháng thành ngày cuối cùng
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeZone(TimeZone.getTimeZone("UTC")); // Để tránh ảnh hưởng của múi giờ
        calendarEnd.setTime(currentDate);
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        calendarEnd.set(Calendar.MILLISECOND, 999);

        // Lấy thời điểm cuối tháng dưới dạng currentTimeMillis
        return calendarEnd.getTimeInMillis();
    }
}
