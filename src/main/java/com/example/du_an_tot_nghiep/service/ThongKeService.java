package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.repository.DonHangRepository;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class ThongKeService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private DonHangRepository donHangRepository;

    // Lấy số người dùng mới trong khoảng thời gian
    public Long demNguoiDungMoi(Date startDate, Date endDate) {
        return nguoiDungRepository.countByNgayTaoBetween(startDate, endDate);  // Giả sử có phương thức countByNgayTaoBetween trong repository
    }

    // Lấy số đơn hàng trong ngày
    public Long demDonHangHomNay(Date startDate, Date endDate) {
        return donHangRepository.countByNgayTaoBetween(startDate, endDate);  // Giả sử có phương thức countByNgayTaoBetween trong repository
    }

    // Lấy tổng doanh thu trong ngày
    public Double tongDoanhThuHomNay(Date startDate, Date endDate) {
        return donHangRepository.sumTongTienBetween(startDate, endDate);  // Giả sử có phương thức sumTongTienBetween trong repository
    }

    // Lấy ngày bắt đầu và kết thúc của ngày
    public Date[] getStartAndEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        // Bắt đầu ngày (00:00:00)
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();

        // Kết thúc ngày (23:59:59)
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date endDate = cal.getTime();

        return new Date[]{startDate, endDate};
    }

    // Lấy ngày bắt đầu và kết thúc của tuần
    public Date[] getStartAndEndOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // Set to the start of the week
        Date startDate = cal.getTime();

        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.add(Calendar.DAY_OF_WEEK, -1); // Set to the end of the week (Sunday)
        Date endDate = cal.getTime();

        return new Date[]{startDate, endDate};
    }

    // Lấy ngày bắt đầu và kết thúc của tháng
    public Date[] getStartAndEndOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        // Lấy ngày đầu tháng
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = cal.getTime();

        // Lấy ngày cuối tháng
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = cal.getTime();

        return new Date[]{startDate, endDate};
    }
}

