package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.GioHang;
import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.entity.SanPhamTrongGioHang;
import com.example.du_an_tot_nghiep.service.GioHangService;
import com.example.du_an_tot_nghiep.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GioHangController {
    @Autowired
    SanPhamService sanPhamService;
    @Autowired
    private GioHangService gioHangService;



    @GetMapping("/giohang")
    public String hienThiDanhSachSanPham(Model model) {
        List<SanPham> sanPhams = sanPhamService.getAllSanPhams();
        Long currentUserId = getCurrentUserId(); // Giả sử phương thức này trả về ID của người dùng hiện tại
        model.addAttribute("sanPhams", sanPhams);
        model.addAttribute("currentUserId", currentUserId); // Truyền userId vào model
        return "giohang/giohang";  // Trả về trang danh sách sản phẩm
    }

    private Long getCurrentUserId() {
        // Giả lập: Trả về userId của người dùng hiện tại
        // Thực tế bạn cần sửa đổi logic này để lấy ID từ session hoặc thông qua bảo mật của Spring
        return 1L; // Ví dụ: ID = 1
    }

    @PostMapping("/api/giohang/{nguoiDungId}/them-san-pham/{sanPhamId}")
    public ResponseEntity<String> themSanPhamVaoGio(
            @PathVariable Long nguoiDungId,
            @PathVariable Long sanPhamId,
            @RequestParam int soLuong) {
        gioHangService.themSanPhamVaoGio(nguoiDungId, sanPhamId, soLuong);
        return ResponseEntity.ok("Sản phẩm đã được thêm vào giỏ hàng thành công!");
    }

}