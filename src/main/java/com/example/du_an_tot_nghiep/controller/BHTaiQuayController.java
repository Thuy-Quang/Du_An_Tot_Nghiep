package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.*;
import com.example.du_an_tot_nghiep.model.DonHangReq;
import com.example.du_an_tot_nghiep.repository.DonHangRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamChiTietRepository;
import com.example.du_an_tot_nghiep.service.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bhtaiquay")
public class BHTaiQuayController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private ChiTietDonHangService chiTietDonHangService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private BanHangTaiQuayService banHangTaiQuayService;


    // Hiển thị trang bán hàng tại quầy
    @GetMapping
    public String showPage(
            @RequestParam(defaultValue = "0") int pageSanPham,
            @RequestParam(defaultValue = "0") int pageDonHang,
            @RequestParam(defaultValue = "0") int pageChiTiet,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Long idDonHangDuocChon,
            Model model) {
        try {
            // Lấy danh sách khách hàng từ service
            List<NguoiDung> danhSachKhachHang = nguoiDungService.findAll();
            model.addAttribute("listKhachHang", danhSachKhachHang);

            // Lấy danh sách đơn hàng và sắp xếp giảm dần theo ngày tạo
            Page<DonHang> donHangPage = donHangRepository.findAllByOrderByNgayTaoDesc(PageRequest.of(pageDonHang, size));
            Page<ChiTietDonHang> chiTietDonHangPage = chiTietDonHangService.getAll(PageRequest.of(pageChiTiet, size));
            model.addAttribute("listDonHang", donHangPage.getContent()); // Danh sách đơn hàng trên trang hiện tại
            model.addAttribute("currentPageDonHang", pageDonHang); // Trang hiện tại
            model.addAttribute("totalPagesDonHang", donHangPage.getTotalPages()); // Tổng số trang

            // Tải toàn bộ dữ liệu trang
            Map<String, Object> danhSach = banHangTaiQuayService.loadManagementPage(pageSanPham, pageDonHang, pageChiTiet, size, idDonHangDuocChon);

            // Thêm đơn hàng được chọn vào Model
            if (idDonHangDuocChon != null) {
                DonHang donHangDuocChon = donHangService.findById(idDonHangDuocChon);
                banHangTaiQuayService.tinhTongTien(donHangDuocChon);
                model.addAttribute("donHangDuocChon", donHangDuocChon);
                model.addAttribute("hienThiModal", true); // Hiển thị modal khi có đơn hàng được chọn
            } else {
                model.addAttribute("donHangDuocChon", new DonHang());
                model.addAttribute("hienThiModal", false); // Không hiển thị modal khi không có đơn hàng được chọn
            }

            // Thêm danh sách vào model
            danhSach.forEach(model::addAttribute);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            e.printStackTrace();
        }
        return "banhang/index";
    }

    // Tạo đơ  hàng
    @PostMapping("/taoDonHang")
    public String taoDonHang(@RequestParam("nguoiDungId") Long nguoiDungId,
                             @RequestParam("phuongThucThanhToan") String phuongThucThanhToan,
                             RedirectAttributes redirectAttributes) {
        try {
            // Gọi service để tạo đơn hàng
            banHangTaiQuayService.taoDonHang(nguoiDungId, phuongThucThanhToan);

            // Thông báo thành công
            redirectAttributes.addFlashAttribute("message", "Đơn hàng đã được tạo thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi tạo đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/bhtaiquay"; // Chuyển hướng về trang bán hàng tại quầy
    }

    // thêm sản phẩm
    @PostMapping("/addSanPham")
    public String addSanPham(@RequestParam Long donHangId,
                             @RequestParam Long sanPhamId,
                             @RequestParam int soLuong,
                             RedirectAttributes redirectAttributes) {
        try {
            // Gọi service để thêm sản phẩm
            banHangTaiQuayService.addSPVaoDonHang(donHangId, sanPhamId, soLuong);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm vào đơn hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/bhtaiquay?idDonHangDuocChon=" + donHangId;
    }


    // Sửa đơn hàng trong danh sách đơn hàng
    @GetMapping("/editDonHang")
    public String editDonHang(@RequestParam Long donHangId, Model model) {
        try {
            // Lấy chi tiết đơn hàng
            DonHang donHang = donHangService.findById(donHangId);
            model.addAttribute("donHangDuocChon", donHang);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Đơn hàng không tồn tại: " + e.getMessage());
        }
        return "banhang/chitietdonhang"; // Trả về giao diện chứa modal
    }


    // sửa đơn hàng trong đơn hàng
    @PostMapping("/updateDonHang")
    @Transactional
    public String updateDonHang(
            @RequestParam Long donHangId,
            @RequestParam List<Long> sanPhamChiTietIds,
            @RequestParam List<Integer> soLuongs,
            RedirectAttributes redirectAttributes) {
        try {
            // Lặp qua các sản phẩm để cập nhật
            for (int i = 0; i < sanPhamChiTietIds.size(); i++) {
                banHangTaiQuayService.capNhatChiTiet(donHangId, sanPhamChiTietIds.get(i), soLuongs.get(i), false);
            }
            redirectAttributes.addFlashAttribute("message", "Cập nhật đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/bhtaiquay?idDonHangDuocChon=" + donHangId;
    }

    @PostMapping("/thanhToan")
    public String thanhToan(@RequestParam Long donHangId, Model model) {
        try {
            banHangTaiQuayService.thanhToanDonHang(donHangId);

            // Cập nhật danh sách đơn hàng để hiển thị trạng thái mới
            model.addAttribute("listDonHang", donHangRepository.findAll());
            return "redirect:/bhtaiquay";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Thanh toán thất bại: " + e.getMessage());

            // Trả về danh sách đơn hàng để hiển thị nếu có lỗi
            model.addAttribute("listDonHang", donHangRepository.findAll());
            return "banhang/index";
        }
    }

    // Xử lý logic bán hàng tại quầy
    @PostMapping("/ban-hang-tai-quay")
    @Transactional
    public ResponseEntity<?> banHangTaiQuay(@RequestBody DonHangReq donHangReq) {
        try {
            // Sử dụng dịch vụ để xử lý toàn bộ logic
            DonHang donHang = banHangTaiQuayService.taoDonHangTuYeuCau(donHangReq);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đơn hàng đã được tạo thành công.");
            response.put("donHangId", donHang.getId());
            response.put("tongTien", donHang.getTongTien());
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
