package com.example.du_an_tot_nghiep.controller.hoadon;

import com.example.du_an_tot_nghiep.entity.DonHang;
import com.example.du_an_tot_nghiep.model.DonHangRequest;
import com.example.du_an_tot_nghiep.repository.DonHangRepository;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import com.example.du_an_tot_nghiep.service.DonHang.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/HienThiDonHang")
public class DonHangController {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private DonHangService donHangService;

    @GetMapping("/GetAll")
    public String showDH(Model model) {
        model.addAttribute("listDonHang", donHangRepository.findAll());
        return "donhang/index"; // Cập nhật đường dẫn tới view tương ứng
    }

    @GetMapping("/create")
    private String getProjectDH(Model model) {
        model.addAttribute("listNguoiDung", nguoiDungRepository.findAll());
        return "donhang/add"; // Đường dẫn tới view
    }

    @PostMapping("/create")
    public String createDonHang(@ModelAttribute DonHangRequest donHangRequest) {
        // Tạo một đối tượng DonHang mới từ donHangRequest
        DonHang donHang = new DonHang();
        donHang.setTrangThai(donHangRequest.getTrangThai());
        donHang.setTongTien(donHangRequest.getTongTien());
        donHang.setTrangThaiThanhToan(donHangRequest.getTrangThaiThanhToan());
        donHang.setPhuongThucThanhToan(donHangRequest.getPhuongThucThanhToan());
        donHang.setDiaChi(donHangRequest.getDiaChi());
        donHang.setNguoiDung(nguoiDungRepository.findById(donHangRequest.getNguoiDungId()).orElse(null)); // Thêm người dùng vào đơn hàng

        // Chuyển đổi LocalDateTime sang Date
        donHang.setNgayTao(Timestamp.valueOf(LocalDateTime.now())); // Gán giá trị hiện tại cho ngayTao

        // Lưu đơn hàng vào database
        donHangRepository.save(donHang);

        return "redirect:/HienThiDonHang/GetAll"; // Chuyển hướng về danh sách đơn hàng
    }
    @GetMapping("/listDonHang/edit/id/{id}")
    public String putDonHang(@PathVariable("id") Long id, Model model) {
        DonHang donHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại")); // Kiểm tra xem đơn hàng có tồn tại hay không
        model.addAttribute("detail", donHang); // Thêm thông tin đơn hàng vào model
        model.addAttribute("listNguoiDung", nguoiDungRepository.findAll()); // Thêm danh sách người dùng vào model
        return "donhang/update"; // Trả về view cập nhật
    }

    // Phương thức để xử lý cập nhật đơn hàng
    @PostMapping("/listDonHang/edit/id/{id}")
    public String updateDonHang(@PathVariable("id") Long id, @ModelAttribute DonHangRequest donHangRequest) {
        // Tìm đơn hàng theo ID
        DonHang donHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại")); // Kiểm tra xem đơn hàng có tồn tại hay không

        // Cập nhật thông tin đơn hàng
        donHang.setTrangThai(donHangRequest.getTrangThai());
        donHang.setTongTien(donHangRequest.getTongTien());
        donHang.setTrangThaiThanhToan(donHangRequest.getTrangThaiThanhToan());
        donHang.setPhuongThucThanhToan(donHangRequest.getPhuongThucThanhToan());
        donHang.setDiaChi(donHangRequest.getDiaChi());
        // Thêm các trường khác nếu cần thiết

        // Lưu cập nhật vào database
        donHangRepository.save(donHang);

        return "redirect:/HienThiDonHang/GetAll"; // Quay lại danh sách đơn hàng
    }

    // Thêm phương thức xóa nếu cần
    @PostMapping("/listDonHang/delete/id/{id}")
    private String delete(@PathVariable("id") Long id) {
        donHangRepository.deleteById(id); // Xóa đơn hàng theo ID
        return "redirect:/HienThiDonHang/GetAll"; // Quay lại danh sách đơn hàng
    }
}