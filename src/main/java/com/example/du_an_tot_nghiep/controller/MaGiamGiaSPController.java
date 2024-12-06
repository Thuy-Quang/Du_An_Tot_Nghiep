package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.MaGiamGiaSanPham;
import com.example.du_an_tot_nghiep.repository.MaGiamGiaRepository;
import com.example.du_an_tot_nghiep.repository.MaGiamGiaSanPhamRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import com.example.du_an_tot_nghiep.service.MaGiamGiaSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/ma-giam-gia-san-pham")
public class MaGiamGiaSPController {
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;

    @Autowired
    private MaGiamGiaSanPhamRepository maGiamGiaSanPhamRepository;

    @Autowired
    private MaGiamGiaSanPhamService maGiamGiaSanPhamService;

    // Phương thức hiển thị danh sách mã giảm giá sản phẩm
    @GetMapping("/hienthi")
    public String hienthii(Model model) {
        model.addAttribute("list", maGiamGiaSanPhamRepository.findAll());
        return "magiamgiasanpham/index";
    }

    // Phương thức hiển thị form thêm mới mã giảm giá sản phẩm
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("maGiamGiaSanPham", new MaGiamGiaSanPham());
        model.addAttribute("sanPhams", sanPhamRepository.findAll()); // Truyền danh sách sản phẩm
        model.addAttribute("maGiamGias", maGiamGiaRepository.findAll()); // Truyền danh sách mã giảm giá
        return "magiamgiasanpham/add";
    }

    // Phương thức thêm mới mã giảm giá sản phẩm
    @PostMapping("/add")
    public String add(@ModelAttribute MaGiamGiaSanPham maGiamGiaSanPham, Model model) {
        try {
            // Kiểm tra ngày áp dụng và ngày hết hạn
            if (maGiamGiaSanPham.getNgayApDung().isAfter(maGiamGiaSanPham.getMaGiamGia().getNgayHetHan())) {
                model.addAttribute("error", "Ngày áp dụng phải trước ngày hết hạn.");
                model.addAttribute("maGiamGiaSanPham", maGiamGiaSanPham);
                model.addAttribute("sanPhams", sanPhamRepository.findAll());
                model.addAttribute("maGiamGias", maGiamGiaRepository.findAll());
                return "magiamgiasanpham/add";
            }

            maGiamGiaSanPhamService.save(maGiamGiaSanPham);
            return "redirect:/ma-giam-gia-san-pham/hienthi";
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            model.addAttribute("sanPhams", sanPhamRepository.findAll());
            model.addAttribute("maGiamGias", maGiamGiaRepository.findAll());
            return "magiamgiasanpham/add";
        }
    }

    // Phương thức hiển thị form chỉnh sửa mã giảm giá sản phẩm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MaGiamGiaSanPham maGiamGiaSanPham = maGiamGiaSanPhamService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID: " + id));

        model.addAttribute("maGiamGiaSanPham", maGiamGiaSanPham);
        model.addAttribute("sanPhams", sanPhamRepository.findAll()); // Truyền danh sách sản phẩm
        model.addAttribute("maGiamGias", maGiamGiaRepository.findAll()); // Truyền danh sách mã giảm giá

        return "magiamgiasanpham/edit"; // Template để chỉnh sửa
    }

    // Phương thức cập nhật mã giảm giá sản phẩm
    // Cập nhật mã giảm giá sản phẩm
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute MaGiamGiaSanPham maGiamGiaSanPham, Model model) {
        try {
            System.out.println("Bắt đầu cập nhật mã giảm giá sản phẩm với ID: " + id);

            // Kiểm tra ngày áp dụng và ngày hết hạn
            if (maGiamGiaSanPham.getNgayApDung().isAfter(maGiamGiaSanPham.getMaGiamGia().getNgayHetHan())) {
                model.addAttribute("error", "Ngày áp dụng phải trước ngày hết hạn.");
                model.addAttribute("maGiamGiaSanPham", maGiamGiaSanPham);
                model.addAttribute("sanPhams", sanPhamRepository.findAll());
                model.addAttribute("maGiamGias", maGiamGiaRepository.findAll());
                System.out.println("Ngày áp dụng phải trước ngày hết hạn.");
                return "magiamgiasanpham/edit";
            }

            // Ghi log trước khi lưu để kiểm tra đối tượng
            System.out.println("Đang cập nhật mã giảm giá sản phẩm: " + maGiamGiaSanPham);

            // Cập nhật dữ liệu
            maGiamGiaSanPham.setId(id);
            maGiamGiaSanPhamService.save(maGiamGiaSanPham);

            System.out.println("Cập nhật thành công cho mã giảm giá sản phẩm với ID: " + id);

            return "redirect:/ma-giam-gia-san-pham/hienthi";
        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
            model.addAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            model.addAttribute("sanPhams", sanPhamRepository.findAll());
            model.addAttribute("maGiamGias", maGiamGiaRepository.findAll());
            return "magiamgiasanpham/edit";
        }
    }

    // Phương thức xóa mã giảm giá sản phẩm
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        maGiamGiaSanPhamService.deleteById(id);
        return "redirect:/ma-giam-gia-san-pham/hienthi";
    }

    // Phương thức chi tiết mã giảm giá sản phẩm
//    @GetMapping("/detail/{id}")
//    public String showDetail(@PathVariable Long id, Model model) {
//        MaGiamGiaSanPham maGiamGiaSanPham = maGiamGiaSanPhamService.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid ID: " + id));
//
//        // Định dạng ngày giờ
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//
//        // Định dạng ngày áp dụng
//        String ngayApDungFormatted = (maGiamGiaSanPham.getNgayApDung() != null)
//                ? maGiamGiaSanPham.getNgayApDung().format(dateTimeFormatter)
//                : "Không xác định";
//
//        // Định dạng ngày hết hạn
//        String ngayHetHanFormatted = (maGiamGiaSanPham.getNgayHetHan() != null)
//                ? maGiamGiaSanPham.getNgayHetHan().format(dateTimeFormatter)
//                : "Không xác định";
//
//        // Định dạng ngày tạo
//        String ngayTaoFormatted = (maGiamGiaSanPham.getNgayTao() != null)
//                ? LocalDateTime.ofInstant(maGiamGiaSanPham.getNgayTao().toInstant(ZoneOffset.UTC), ZoneId.systemDefault())
//                .format(dateTimeFormatter)
//                : "Không xác định";
//
//        // Định dạng ngày cập nhật
//        String ngayCapNhatFormatted = (maGiamGiaSanPham.getNgayCapNhat() != null)
//                ? LocalDateTime.ofInstant(maGiamGiaSanPham.getNgayCapNhat().toInstant(ZoneOffset.UTC), ZoneId.systemDefault())
//                .format(dateTimeFormatter)
//                : "Không xác định";
//
//        // Thêm thuộc tính vào model
//        model.addAttribute("maGiamGiaSanPham", maGiamGiaSanPham);
//        model.addAttribute("ngayApDungFormatted", ngayApDungFormatted);
//        model.addAttribute("ngayHetHanFormatted", ngayHetHanFormatted);
//        model.addAttribute("ngayTaoFormatted", ngayTaoFormatted);
//        model.addAttribute("ngayCapNhatFormatted", ngayCapNhatFormatted);
//
//        return "magiamgiasanpham/detail";
//    }
}
