package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
@Controller
@RequestMapping("/thong-ke")
public class ThongKeController {

    @Autowired
    private ThongKeService thongKeService;

    @GetMapping("/thongke")
    public String thongKe(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date ngayChon,
            @RequestParam(required = false) Integer tuan,
            @RequestParam(required = false) Integer thang,
            Model model) {

        // Xử lý nếu không có ngày chọn
        if (ngayChon == null) {
            ngayChon = new Date();  // Nếu không chọn ngày, mặc định là ngày hiện tại
        }

        // Xử lý thống kê theo tuần hoặc tháng
        Date[] range = thongKeService.getStartAndEndOfDay(ngayChon);

        if (tuan != null) {
            range = thongKeService.getStartAndEndOfWeek(ngayChon);  // Lấy ngày bắt đầu và kết thúc của tuần
        }

        if (thang != null) {
            range = thongKeService.getStartAndEndOfMonth(ngayChon);  // Lấy ngày bắt đầu và kết thúc của tháng
        }

        // Thống kê
        Long soNguoiDungMoi = thongKeService.demNguoiDungMoi(range[0], range[1]);
        Long soDonHangHomNay = thongKeService.demDonHangHomNay(range[0], range[1]);
        Double tongDoanhThu = thongKeService.tongDoanhThuHomNay(range[0], range[1]);

        // Thêm các thống kê vào model
        model.addAttribute("soNguoiDungMoi", soNguoiDungMoi);
        model.addAttribute("soDonHangHomNay", soDonHangHomNay);
        model.addAttribute("tongDoanhThu", tongDoanhThu);
        model.addAttribute("ngayChon", ngayChon);
        model.addAttribute("tuan", tuan);
        model.addAttribute("thang", thang);

        return "DangNhap/thongke";  // Đảm bảo tên template chính xác
    }
}


