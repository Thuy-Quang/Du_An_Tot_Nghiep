package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.repository.LoaiSanPhamRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/sanpham")
public class sphamController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    @GetMapping
    public String listSanPham(@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size,
                              @RequestParam(value = "q", required = false) String query,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPham> sanPhams;

        if (query != null && !query.isEmpty()) {
            sanPhams = sanPhamRepository.findByTenSanPhamContainingOrderByNgayTaoDesc(query, pageable);
        } else {
            sanPhams = sanPhamRepository.findAllByOrderByNgayTaoDesc(pageable);
        }

        model.addAttribute("sanPhams", sanPhams);
        model.addAttribute("listLSP", loaiSanPhamRepository.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sanPhams.getTotalPages());
        model.addAttribute("query", query);
        return "sanpham/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("sanPham", new SanPham());
        model.addAttribute("listLSP", loaiSanPhamRepository.findAll());
        return "sanpham/add";
    }

    @PostMapping("/add")
    public String addSanPham(@ModelAttribute("sanPham") SanPham sanPham) {
        sanPham.setNgayTao(new Date());
        sanPhamRepository.save(sanPham);
        return "redirect:/sanpham";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<SanPham> optionalSanPham = sanPhamRepository.findById(id);
        if (optionalSanPham.isEmpty()) {
            throw new EntityNotFoundException("Sản phẩm không tồn tại");
        }
        model.addAttribute("sanPham", optionalSanPham.get());
        model.addAttribute("listLSP", loaiSanPhamRepository.findAll());
        return "sanpham/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateSanPham(@PathVariable Long id, @ModelAttribute("sanPham") SanPham updatedSanPham) {
        SanPham existingSanPham = sanPhamRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại"));
        updatedSanPham.setId(id);
        updatedSanPham.setNgayTao(existingSanPham.getNgayTao());
        updatedSanPham.setNgayCapNhat(new Date());
        sanPhamRepository.save(updatedSanPham);
        return "redirect:/sanpham";
    }

    @GetMapping("/delete/{id}")
    public String deleteSanPham(@PathVariable Long id) {
        sanPhamRepository.deleteById(id);
        return "redirect:/sanpham";
    }
}
