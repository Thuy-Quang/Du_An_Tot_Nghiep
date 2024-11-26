package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.DonHang;
import com.example.du_an_tot_nghiep.entity.NguoiDung;
import com.example.du_an_tot_nghiep.model.DonHangRequest;
import com.example.du_an_tot_nghiep.repository.DonHangRepository;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/HienThiDonHang")
public class DonHangController {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    // Hiển thị danh sách đơn hàng
    @GetMapping("/GetAll")
    public String showDH(Model model) {
        model.addAttribute("listDonHang", donHangRepository.findAll());
        return "donhang/index"; // Trả về giao diện danh sách đơn hàng
    }

    // Hiển thị form thêm đơn hàng
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Lấy danh sách tất cả khách hàng
        List<NguoiDung> listNguoiDung = nguoiDungRepository.findAll();

        // Gửi danh sách khách hàng đến view
        model.addAttribute("listNguoiDung", listNguoiDung);
        model.addAttribute("donHangRequest", new DonHangRequest());
        return "donhang/add"; // Giao diện thêm đơn hàng
    }

    // Xử lý thêm đơn hàng
    @PostMapping("/create")
    public String createDonHang(@ModelAttribute DonHangRequest donHangRequest) {
        // Tạo một đối tượng DonHang mới từ donHangRequest
        DonHang donHang = new DonHang();
        donHang.setTrangThai(donHangRequest.getTrangThai());
        donHang.setTongTien(donHangRequest.getTongTien());
        donHang.setTrangThaiThanhToan(donHangRequest.getTrangThaiThanhToan());
        donHang.setPhuongThucThanhToan(donHangRequest.getPhuongThucThanhToan());
        donHang.setNguoiDung(nguoiDungRepository.findById(donHangRequest.getNguoiDungId()).orElse(null)); // Thêm người dùng vào đơn hàng

        // Chuyển đổi LocalDateTime sang Date
        donHang.setNgayTao(Timestamp.valueOf(LocalDateTime.now())); // Gán giá trị hiện tại cho ngayTao

        // Lưu đơn hàng vào database
        donHangRepository.save(donHang);

        return "redirect:/HienThiDonHang/GetAll"; // Chuyển hướng về danh sách đơn hàng
    }

    // Hiển thị form chỉnh sửa đơn hàng
    @GetMapping("/listDonHang/edit/id/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        DonHang donHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        DonHangRequest donHangRequest = new DonHangRequest();
        donHangRequest.setTrangThai(donHang.getTrangThai());
        donHangRequest.setTongTien(donHang.getTongTien());
        donHangRequest.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
        donHangRequest.setPhuongThucThanhToan(donHang.getPhuongThucThanhToan());
        donHangRequest.setNguoiDungId(donHang.getNguoiDung().getId());

        model.addAttribute("detail", donHang); // Thêm thông tin đơn hàng vào model
        model.addAttribute("donHangRequest", donHangRequest); // Thêm thông tin để chỉnh sửa
        model.addAttribute("listNguoiDung", nguoiDungRepository.findAll()); // Thêm danh sách người dùng

        return "donhang/update"; // Trả về giao diện cập nhật đơn hàng
    }

    // Xử lý cập nhật đơn hàng
    @PostMapping("/listDonHang/edit/id/{id}")
    public String updateDonHang(@PathVariable("id") Long id, @ModelAttribute DonHangRequest donHangRequest) {
        DonHang donHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        donHang.setTrangThai(donHangRequest.getTrangThai());
        donHang.setTongTien(donHangRequest.getTongTien());
        donHang.setTrangThaiThanhToan(donHangRequest.getTrangThaiThanhToan());
        donHang.setPhuongThucThanhToan(donHangRequest.getPhuongThucThanhToan());
        donHang.setNguoiDung(nguoiDungRepository.findById(donHangRequest.getNguoiDungId()).orElse(null));

        donHangRepository.save(donHang); // Lưu cập nhật
        return "redirect:/HienThiDonHang/GetAll"; // Chuyển hướng về danh sách đơn hàng
    }

    // Xóa đơn hàng
    @PostMapping("/listDonHang/delete/id/{id}")
    public String deleteDonHang(@PathVariable("id") Long id) {
        donHangRepository.deleteById(id); // Xóa đơn hàng
        return "redirect:/HienThiDonHang/GetAll"; // Chuyển hướng về danh sách đơn hàng
    }
}
