package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.GioHang;
import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.service.GioHangService;
import com.example.du_an_tot_nghiep.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class GioHangController {
    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private GioHangService gioHangService;

    @GetMapping("/giohang")
    public String hienThiDanhSachSanPham(Model model) {
        List<SanPham> sanPhams = sanPhamService.getAllSanPhams();
        Long currentUserId = getCurrentUserId(); // Giả sử phương thức này trả về ID của người dùng hiện tại
        model.addAttribute("sanPhams", sanPhams);
        model.addAttribute("currentUserId", currentUserId); // Truyền userId vào model
        return "giohang/giohang";
    }

    private Long getCurrentUserId() {
        // Giả lập ID người dùng hiện tại
        return 1L; // Ví dụ ID = 1
    }

    @PostMapping("/api/giohang/{nguoiDungId}/them-san-pham/{sanPhamId}")
    public ResponseEntity<String> themSanPhamVaoGio(
            @PathVariable Long nguoiDungId,
            @PathVariable Long sanPhamId,
            @RequestParam int soLuong) {
        Optional<SanPham> sanPhamOpt = sanPhamService.getSanPhamById(sanPhamId);
        if (!sanPhamOpt.isPresent()) {
            return new ResponseEntity<>("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND);
        }

        SanPham sanPham = sanPhamOpt.get();
        gioHangService.themSanPhamVaoGio(nguoiDungId, sanPhamId, soLuong);

        return ResponseEntity.ok("Sản phẩm " + sanPham.getTenSanPham() + " đã được thêm vào giỏ hàng thành công!");
    }
}
