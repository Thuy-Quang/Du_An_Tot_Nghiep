package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.repository.DonHangRepository;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ThongKeService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private DonHangRepository donHangRepository;

    public Double getDoanhSoHomNay(Date startDate, Date endDate) {
        return donHangRepository.tinhDoanhSoHomNay(startDate, endDate);
    }

    public Double getDoanhSoThang(Date startDate, Date endDate) {
        return donHangRepository.tinhDoanhSoThang(startDate, endDate);
    }
    public List<Object[]> getSanPhamBanDuocTrongThang(LocalDate startOfMonth, LocalDate endOfMonth) {
        // Chuyển đổi LocalDate thành LocalDateTime
        LocalDateTime startOfMonthTime = startOfMonth.atStartOfDay();  // 00:00:00
        LocalDateTime endOfMonthTime = endOfMonth.atTime(23, 59, 59); // 23:59:59

        return sanPhamRepository.countSanPhamBanDuocTrongThang(startOfMonthTime, endOfMonthTime);
    }
    public List<Object[]> getSanPhamBanChay() {
        // Lấy ngày đầu tháng và cuối tháng hiện tại
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();

        return donHangRepository.thongKeSanPhamBanChay(startDate, endDate);
    }



    // Phương thức mới để lấy doanh thu theo ngày trong tháng
    public Double getDoanhSoTheoNgay(Date date) {
        // Chuyển đổi ngày thành chuỗi chỉ có ngày (bỏ giờ)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);

        try {
            // Chuyển lại chuỗi thành đối tượng Date nếu cần
            Date formattedDateObj = sdf.parse(formattedDate);

            // Truy vấn doanh số theo ngày
            return donHangRepository.tinhDoanhSoTheoNgay(formattedDateObj);
        } catch (ParseException e) {
            // Xử lý nếu không thể phân tích ngày
            e.printStackTrace();
            return 0.0; // Hoặc một giá trị mặc định nếu gặp lỗi
        }
    }
    // Phương thức lấy doanh thu theo ngày trong tháng
    public List<Double> getDoanhSoThangTheoNgay(Date startOfMonth, Date endOfMonth) {
        List<Double> doanhSoTheoNgay = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startOfMonth);

        // Lặp qua các ngày trong tháng để lấy doanh thu
        while (calendar.getTime().before(endOfMonth) || calendar.getTime().equals(endOfMonth)) {
            Double dailySales = donHangRepository.tinhDoanhSoTheoNgay(calendar.getTime());
            doanhSoTheoNgay.add(dailySales != null ? dailySales : 0.0);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return doanhSoTheoNgay;
    }
}
