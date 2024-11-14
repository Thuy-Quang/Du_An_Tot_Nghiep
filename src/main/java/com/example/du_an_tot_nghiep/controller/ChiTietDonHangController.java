package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.ChiTietDonHang;
import com.example.du_an_tot_nghiep.repository.ChiTietDonHangRepository;
import com.example.du_an_tot_nghiep.service.ChiTietDonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping ("/chi-tiet-don-hang")
public class ChiTietDonHangController {
    @Autowired
    ChiTietDonHangRepository chiTietDonHangRepository;
    @Autowired
    private ChiTietDonHangService chiTietDonHangService;

    // Hiển thị danh sách chi tiết đơn hàng
    @GetMapping("/hienthi")
    public String getChiTietDonHangList(Model model) {
        List<ChiTietDonHang> list = chiTietDonHangService.getAll();
        model.addAttribute("list", list);
        return "chitietdonhang/index"; // Tên của template HTML
    }

    // Hiển thị trang thêm chi tiết đơn hàng
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("chiTietDonHang", new ChiTietDonHang());
        return "chitietdonhang/add"; // Tên của template HTML cho thêm
    }

    // Xử lý thêm chi tiết đơn hàng
    @PostMapping("/add")
    public String addChiTietDonHang(@ModelAttribute ChiTietDonHang chiTietDonHang) {
        chiTietDonHangService.add(chiTietDonHang);
        return "redirect:/chi-tiet-don-hang/hienthi"; // Quay lại danh sách sau khi thêm
    }

    // Hiển thị trang sửa chi tiết đơn hàng
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ChiTietDonHang chiTietDonHang = chiTietDonHangService.getById(id).orElse(null);
        model.addAttribute("chiTietDonHang", chiTietDonHang);
        return "chitietdonhang/edit"; // Tên của template HTML cho sửa
    }

    // Xử lý sửa chi tiết đơn hàng
    @PostMapping("/edit")
    public String editChiTietDonHang(@ModelAttribute ChiTietDonHang chiTietDonHang) {
        chiTietDonHangService.update(chiTietDonHang);
        return "redirect:/chi-tiet-don-hang/hienthi"; // Quay lại danh sách sau khi sửa
    }

    // Xóa chi tiết đơn hàng
    @PostMapping("/delete/{id}")
    public String deleteChiTietDonHang(@PathVariable Long id) {
        chiTietDonHangService.delete(id);
        return "redirect:/chi-tiet-don-hang/hienthi"; // Quay lại danh sách sau khi xóa
    }
}
