package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
public class ThongKeController {

    @Autowired
    private ThongKeService thongKeService;
//    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/thong-ke")
    public String thongKe(Model model) {
        Date now = new Date();

        // Định dạng ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStartStr = dateFormat.format(now) + " 00:00:00";
        String todayEndStr = dateFormat.format(now) + " 23:59:59";

        try {
            Date todayStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(todayStartStr);
            Date todayEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(todayEndStr);

            // Tính doanh số hôm nay
            Double doanhSoHomNay = thongKeService.getDoanhSoHomNay(todayStart, todayEnd);

            // Tính doanh số tháng
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date startOfMonth = calendar.getTime();
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            Date endOfMonth = calendar.getTime();

            Double doanhSoThang = thongKeService.getDoanhSoThang(startOfMonth, endOfMonth);

            // Chuyển Date sang LocalDate
            LocalDate startLocalDate = startOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocalDate = endOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Gọi phương thức lấy sản phẩm bán được trong tháng
            List<Object[]> sanPhamBanChay = thongKeService.getSanPhamBanChay();  // Đảm bảo phương thức này trả về kết quả đúng
            List<Object[]> sanPhamBanDuoc = thongKeService.getSanPhamBanDuocTrongThang(startLocalDate, endLocalDate);

            // Đưa dữ liệu vào Model
            model.addAttribute("doanhSoHomNay", doanhSoHomNay != null ? doanhSoHomNay : 0.0);
            model.addAttribute("doanhSoThang", doanhSoThang != null ? doanhSoThang : 0.0);
            model.addAttribute("sanPhamBanChay", sanPhamBanChay);  // Sản phẩm bán chạy
            model.addAttribute("sanPhamBanDuoc", sanPhamBanDuoc);  // Sản phẩm bán trong tháng
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi lấy dữ liệu thống kê.");
        }

        return "DangNhap/thongke";  // Đảm bảo tên template chính xác
    }

    @GetMapping("/api/chart-data")
    @ResponseBody
    public Map<String, Object> getChartData() {
        Map<String, Object> response = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfMonth = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        Date endOfMonth = calendar.getTime();

        List<String> days = new ArrayList<>();
        List<Double> salesData = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        Calendar temp = Calendar.getInstance();
        temp.setTime(startOfMonth);

        while (!temp.getTime().after(endOfMonth)) {
            Date currentDate = temp.getTime();
            Double dailySales = thongKeService.getDoanhSoTheoNgay(currentDate);

            days.add(sdf.format(currentDate));
            salesData.add(dailySales != null ? dailySales : 0.0);

            temp.add(Calendar.DATE, 1);
        }

        response.put("labels", days);
        response.put("data", salesData);

        return response;
    }

}
