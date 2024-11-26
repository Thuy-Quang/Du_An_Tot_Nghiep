package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.entity.DonHang;
import com.example.du_an_tot_nghiep.entity.ChiTietDonHang;
import com.example.du_an_tot_nghiep.service.DonHangService;
import com.example.du_an_tot_nghiep.service.SanPhamService;
import com.example.du_an_tot_nghiep.service.ChiTietDonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/bhtaiquay")
public class BHTaiQuayController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private ChiTietDonHangService chiTietDonHangService;

//    @GetMapping
//    public String showManagementPage(
//            @RequestParam(defaultValue = "0") int pageSanPham,
//            @RequestParam(defaultValue = "0") int pageDonHang,
//            @RequestParam(defaultValue = "0") int pageChiTiet,
//            @RequestParam(defaultValue = "5") int size,
//            Model model) {
//        try {
//            // Kiểm tra ràng buộc: page phải >= 0
//            pageSanPham = Math.max(pageSanPham, 0);
//            pageDonHang = Math.max(pageDonHang, 0);
//            pageChiTiet = Math.max(pageChiTiet, 0);
//
//            // Lấy danh sách sản phẩm phân trang
//            Page<SanPham> sanPhamPage = sanPhamService.getAll(PageRequest.of(pageSanPham, size));
//            model.addAttribute("listSanPham", sanPhamPage.getContent());
//            model.addAttribute("currentPageSanPham", pageSanPham);
//            model.addAttribute("totalPagesSanPham", sanPhamPage.getTotalPages());
//
//            // Lấy danh sách đơn hàng phân trang
////            Page<DonHang> donHangPage = donHangService.getAll(PageRequest.of(pageDonHang, size));
//            model.addAttribute("listDonHang", donHangPage.getContent());
//            model.addAttribute("currentPageDonHang", pageDonHang);
//            model.addAttribute("totalPagesDonHang", donHangPage.getTotalPages());
//
//            // Lấy danh sách chi tiết đơn hàng phân trang
//            Page<ChiTietDonHang> chiTietDonHangPage = chiTietDonHangService.getAll(PageRequest.of(pageChiTiet, size));
//            model.addAttribute("listChiTietDonHang", chiTietDonHangPage.getContent());
//            model.addAttribute("currentPageChiTiet", pageChiTiet);
//            model.addAttribute("totalPagesChiTiet", chiTietDonHangPage.getTotalPages());
//
//            // Thông báo khi danh sách trống
//            if (sanPhamPage.isEmpty()) {
//                model.addAttribute("noProductMessage", "Hiện không có sản phẩm nào trong hệ thống.");
//            }
//            if (donHangPage.isEmpty()) {
//                model.addAttribute("noOrderMessage", "Hiện không có đơn hàng nào trong hệ thống.");
//            }
//            if (chiTietDonHangPage.isEmpty()) {
//                model.addAttribute("noOrderDetailMessage", "Hiện không có chi tiết đơn hàng nào trong hệ thống.");
//            }
//        } catch (Exception e) {
//            // Xử lý ngoại lệ nếu có lỗi xảy ra
//            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi tải dữ liệu: " + e.getMessage());
//            e.printStackTrace(); // Ghi log lỗi
//        }
//
//        System.out.println("Đang trả về template: banhang/index");
//        return "banhang/index"; // Trả về giao diện Thymeleaf
//    }
}
