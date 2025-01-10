package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.LoaiSanPham;
import com.example.du_an_tot_nghiep.model.LoaiSanPhamRequest;
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
        model.addAttribute("loaiSanPhamRequest", new LoaiSanPhamRequest());
        return "loaisanpham/add";
    }

    @PostMapping("/save")
    public String saveLoaiSanPham(@ModelAttribute("loaiSanPhamRequest") LoaiSanPhamRequest loaiSanPhamRequest) {
        LoaiSanPham loaiSanPham = convertToEntity(loaiSanPhamRequest);
        loaiSanPhamService.saveOrUpdateLoaiSanPham(loaiSanPham);
        return "redirect:/loai-san-pham";
    }

    @PostMapping("/update/{id}")
    public String updateLoaiSanPham(@PathVariable Long id, @ModelAttribute("loaiSanPhamRequest") LoaiSanPhamRequest loaiSanPhamRequest) {
        loaiSanPhamRequest.setId(id);
        LoaiSanPham loaiSanPham = convertToEntity(loaiSanPhamRequest);
        loaiSanPhamService.saveOrUpdateLoaiSanPham(loaiSanPham);
        return "redirect:/loai-san-pham";
    }



    @PostMapping("/delete/{id}")
    public String deleteLoaiSanPham(@PathVariable Long id) {
        loaiSanPhamService.deleteLoaiSanPham(id);
        return "redirect:/loai-san-pham";
    }
    private LoaiSanPham convertToEntity(LoaiSanPhamRequest request) {
        LoaiSanPham loaiSanPham = new LoaiSanPham();
        loaiSanPham.setId(request.getId());
        loaiSanPham.setTenLoai(request.getTenLoai());
        loaiSanPham.setMoTa(request.getMoTa());
        return loaiSanPham;
    }
    @GetMapping("/edit/{id}")
    public String editLoaiSanPhamForm(@PathVariable Long id, Model model) {
        Optional<LoaiSanPham> loaiSanPhamOptional = loaiSanPhamService.getLoaiSanPhamById(id);
        if (loaiSanPhamOptional.isPresent()) {
            LoaiSanPhamRequest loaiSanPhamRequest = new LoaiSanPhamRequest();
            loaiSanPhamRequest.setId(loaiSanPhamOptional.get().getId());
            loaiSanPhamRequest.setTenLoai(loaiSanPhamOptional.get().getTenLoai());
            loaiSanPhamRequest.setMoTa(loaiSanPhamOptional.get().getMoTa());
            model.addAttribute("loaiSanPhamRequest", loaiSanPhamRequest);
            return "loaisanpham/edit"; // Đảm bảo tên view đúng
        }
        return "redirect:/loai-san-pham"; // Quay lại trang danh sách nếu không tìm thấy
    }
}