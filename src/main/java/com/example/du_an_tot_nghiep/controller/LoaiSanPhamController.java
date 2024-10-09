package com.example.du_an_tot_nghiep.controller;


import com.example.du_an_tot_nghiep.entity.LoaiSanPham;
import com.example.du_an_tot_nghiep.service.LoaiSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/loai-san-pham")
public class LoaiSanPhamController {

    @Autowired
    private LoaiSanPhamService loaiSanPhamService;

    @GetMapping
    public String listLoaiSanPham(Model model) {
        model.addAttribute("listLoaiSanPham", loaiSanPhamService.getAllLoaiSanPham());
        return "loaisanpham/index";
    }

    @GetMapping("/add")
    public String addLoaiSanPhamForm(Model model) {
        model.addAttribute("loaiSanPham", new LoaiSanPham());
        return "loaisanpham/add";
    }

    @PostMapping("/save")
    public String saveLoaiSanPham(@ModelAttribute("loaiSanPham") LoaiSanPham loaiSanPham) {
        loaiSanPhamService.saveOrUpdateLoaiSanPham(loaiSanPham);
        return "redirect:/loai-san-pham";
    }
    @GetMapping("/edit/{id}")
    public String editLoaiSanPhamForm(@PathVariable Long id, Model model) {
        Optional<LoaiSanPham> loaiSanPhamOptional = loaiSanPhamService.getLoaiSanPhamById(id);
        if (loaiSanPhamOptional.isPresent()) {
            model.addAttribute("loaiSanPham", loaiSanPhamOptional.get());
            return "loaisanpham/edit"; // Trả về view sửa
        }
        return "redirect:/loai-san-pham"; // Nếu không tìm thấy, quay lại danh sách
    }

    @PostMapping("/update/{id}")
    public String updateLoaiSanPham(@PathVariable Long id, @ModelAttribute LoaiSanPham loaiSanPham) {
        loaiSanPham.setId(id); // Đảm bảo ID được thiết lập đúng
        loaiSanPhamService.saveOrUpdateLoaiSanPham(loaiSanPham);
        return "redirect:/loai-san-pham"; // Quay lại danh sách sau khi cập nhật
    }

    @PostMapping("/delete/{id}")
    public String deleteLoaiSanPham(@PathVariable Long id) {
        loaiSanPhamService.deleteLoaiSanPham(id);
        return "redirect:/loai-san-pham";
    }
}