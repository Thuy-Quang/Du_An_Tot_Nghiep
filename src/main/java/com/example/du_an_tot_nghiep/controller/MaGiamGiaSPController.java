package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.MaGiamGiaSanPham;
import com.example.du_an_tot_nghiep.repository.MaGiamGiaRepository;
import com.example.du_an_tot_nghiep.repository.MaGiamGiaSanPhamRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ma-giam-gia-san-pham")
public class MaGiamGiaSPController {

    @Autowired
    private MaGiamGiaSanPhamRepository maGiamGiaSanPhamRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;


    // Hiển thị danh sách MaGiamGiaSanPham
    @GetMapping
    public String list(Model model) {
        List<MaGiamGiaSanPham> maGiamGiaSanPhamList = maGiamGiaSanPhamRepository.findAll();
        model.addAttribute("maGiamGiaSanPhamList", maGiamGiaSanPhamList);
        return "magiamgiasanpham/index";
    }

    // Form thêm mới MaGiamGiaSanPham
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("maGiamGiaSanPham", new MaGiamGiaSanPham());
        model.addAttribute("sanPhamChiTietList", sanPhamChiTietRepository.findAll());
        model.addAttribute("maGiamGiaList", maGiamGiaRepository.findAll());
        return "magiamgiasanpham/add";
    }

    // Xử lý form thêm mới
    @PostMapping("/create")
    public String create(@ModelAttribute MaGiamGiaSanPham maGiamGiaSanPham) {
        maGiamGiaSanPhamRepository.save(maGiamGiaSanPham);
        return "redirect:/ma-giam-gia-san-pham";
    }

    // Form sửa MaGiamGiaSanPham
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        MaGiamGiaSanPham maGiamGiaSanPham = maGiamGiaSanPhamRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("maGiamGiaSanPham", maGiamGiaSanPham);
        model.addAttribute("sanPhamChiTietList", sanPhamChiTietRepository.findAll());
        model.addAttribute("maGiamGiaList", maGiamGiaRepository.findAll());
        return "magiamgiasanpham/edit";
    }

    // Xử lý form sửa
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute MaGiamGiaSanPham maGiamGiaSanPham) {
        maGiamGiaSanPham.setId(id);
        maGiamGiaSanPhamRepository.save(maGiamGiaSanPham);
        return "redirect:/ma-giam-gia-san-pham";
    }

    // Xóa MaGiamGiaSanPham
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        maGiamGiaSanPhamRepository.deleteById(id);
        return "redirect:/ma-giam-gia-san-pham";
    }

}
