package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.MaGiamGiaKhachHang;
import com.example.du_an_tot_nghiep.repository.MaGiamGiaKhachHangRepository;
import com.example.du_an_tot_nghiep.service.MaGiamGiaKHService; // Đã sửa tên class
import com.example.du_an_tot_nghiep.service.NguoiDungService;
import com.example.du_an_tot_nghiep.service.MaGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ma-giam-gia-kh")
public class MaGiamGiaKHController {

    @Autowired
    private MaGiamGiaKHService maGiamGiaKHService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private MaGiamGiaService maGiamGiaService;
    @Autowired
    MaGiamGiaKhachHangRepository maGiamGiaKhachHangRepository;

    // Hiển thị danh sách mã giảm giá khách hàng
    @GetMapping("/hienthi")
    public String listMaGiamGiaKH(Model model) {
        List<MaGiamGiaKhachHang> list = maGiamGiaKHService.findAll();
        model.addAttribute("maGiamGiaKHList", list);
        return "magiamgiakhachhang/index";
    }



    // Hiển thị form thêm mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("maGiamGiaKH", new MaGiamGiaKhachHang());
        model.addAttribute("nguoiDungList", nguoiDungService.findAll());
        model.addAttribute("maGiamGiaList", maGiamGiaService.findAll());
        return "magiamgiakhachhang/add";
    }

    // Xử lý thêm mới mã giảm giá khách hàng
    @PostMapping("/add")
    public String addMaGiamGiaKH(@ModelAttribute MaGiamGiaKhachHang maGiamGiaKH, RedirectAttributes redirectAttributes) {
        maGiamGiaKHService.save(maGiamGiaKH);
        redirectAttributes.addFlashAttribute("message", "Thêm mã giảm giá khách hàng thành công!");
        return "redirect:/ma-giam-gia-kh/hienthi";
    }

    // Hiển thị form sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<MaGiamGiaKhachHang> maGiamGiaKH = maGiamGiaKHService.findById(id);
        if (maGiamGiaKH.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy mã giảm giá khách hàng!");
            return "redirect:/ma-giam-gia-kh/hienthi";
        }
        model.addAttribute("maGiamGiaKH", maGiamGiaKH.get());
        model.addAttribute("nguoiDungList", nguoiDungService.findAll());
        model.addAttribute("maGiamGiaList", maGiamGiaService.findAll());
        return "magiamgiakhachhang/edit";
    }

    // Xử lý cập nhật mã giảm giá khách hàng
    @PostMapping("/edit/{id}")
    public String editMaGiamGiaKH(@PathVariable Long id, @ModelAttribute MaGiamGiaKhachHang maGiamGiaKH, RedirectAttributes redirectAttributes) {
        Optional<MaGiamGiaKhachHang> existing = maGiamGiaKHService.findById(id);
        if (existing.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy mã giảm giá khách hàng để cập nhật!");
            return "redirect:/ma-giam-gia-kh/hienthi";
        }
        maGiamGiaKH.setId(id);
        maGiamGiaKHService.save(maGiamGiaKH);
        redirectAttributes.addFlashAttribute("message", "Cập nhật mã giảm giá khách hàng thành công!");
        return "redirect:/ma-giam-gia-kh/hienthi";
    }

    // Xóa mã giảm giá khách hàng
    @PostMapping("/delete/{id}")
    public String deleteMaGiamGiaKH(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<MaGiamGiaKhachHang> existing = maGiamGiaKHService.findById(id);
        if (existing.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy mã giảm giá khách hàng để xóa!");
            return "redirect:/ma-giam-gia-kh/hienthi";
        }
        maGiamGiaKHService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa mã giảm giá khách hàng thành công!");
        return "redirect:/ma-giam-gia-kh/hienthi";
    }

    // Hiển thị chi tiết
    @GetMapping("/detail/{id}")
    public String viewDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<MaGiamGiaKhachHang> maGiamGiaKH = maGiamGiaKHService.findById(id);
        if (maGiamGiaKH.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy mã giảm giá khách hàng!");
            return "redirect:/ma-giam-gia-kh/hienthi";
        }
        model.addAttribute("maGiamGiaKH", maGiamGiaKH.get());
        return "magiamgiakhachhang/detail";
    }
}
