package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.*;
import com.example.du_an_tot_nghiep.model.GioHangDTO;
import com.example.du_an_tot_nghiep.repository.KichCoRepository;
import com.example.du_an_tot_nghiep.repository.MauSacRepository;
import com.example.du_an_tot_nghiep.service.DonHangService;
import com.example.du_an_tot_nghiep.service.GioHangService;
import com.example.du_an_tot_nghiep.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class GioHangController {
    @Autowired
    DonHangService donHangService;
    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private MauSacRepository mauSacRepository;
    @Autowired
    private KichCoRepository kichCoRepository;

    @Autowired
    private GioHangService gioHangService;

    @GetMapping("/giohang")
    public String hienThiDanhSachSanPham(Model model,
                                         @RequestParam(defaultValue = "") String searchQuery,
                                         @RequestParam(value = "price", defaultValue = "") String[] price) {
        // Lấy tất cả sản phẩm
        List<SanPham> sanPhams = sanPhamService.getall();

        // Tìm kiếm theo từ khóa
        if (!searchQuery.isEmpty()) {
            sanPhams = sanPhams.stream()
                    .filter(sanPham -> sanPham.getTenSanPham().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Lọc theo giá nếu có
        if (price.length > 0) {
            sanPhams = sanPhams.stream()
                    .filter(sanPham -> {
                        double productPrice = sanPham.getGia();  // Lấy giá của sản phẩm
                        for (String priceRange : price) {
                            String[] range = priceRange.split("-");
                            // Nếu giá trong phạm vi này
                            if (range.length == 2) {
                                double minPrice = Double.parseDouble(range[0]);
                                double maxPrice = Double.parseDouble(range[1]);
                                if (productPrice >= minPrice && productPrice <= maxPrice) {
                                    return true;
                                }
                            } else if (priceRange.equals("2000+")) {
                                // Xử lý cho giá trên 2000
                                if (productPrice > 2000) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        // Lấy danh sách màu sắc để hiển thị
        List<MauSac> mauSacs = mauSacRepository.findAll();

        // Thêm dữ liệu vào model
        model.addAttribute("sanPhams", sanPhams);  // Danh sách sản phẩm sau khi lọc và tìm kiếm
        model.addAttribute("mauSacs", mauSacs);    // Danh sách màu sắc để hiển thị
        model.addAttribute("searchQuery", searchQuery);  // Từ khóa tìm kiếm
        model.addAttribute("price", price);         // Bộ lọc giá

        return "giohang/giohang";  // Trả về trang giỏ hàng
    }

    @GetMapping("/giohangdetail")
    public String hienThiSanPhamTrongGioHang(){
        return "giohang/giohangdetail";
    }

    @GetMapping("/sanpham/{id}")
    public String chiTietSanPham(@PathVariable Long id, Model model) {
//        SanPham sanPham = sanPhamService.findById(id); // Tìm sản phẩm theo ID
//        model.addAttribute("sanPham", sanPham);
        return "sanphamchitiet/hienthi"; // Tên file HTML chi tiết
    }

    @GetMapping("/dat-hang")
    public String datHang() {
        return "giohang/dathang";  // Trả về view hoặc chuyển hướng đến trang đặt hàng
    }

    @GetMapping("/khachhang/{id}")
    public String getDonHangTheoUser(@PathVariable Long id,Model model) {
        List<DonHang> optionalDonHang = donHangService.getOrdersByUserId(id);
        model.addAttribute("donHang", optionalDonHang);
        return "giohang/donhang";
    }

}
