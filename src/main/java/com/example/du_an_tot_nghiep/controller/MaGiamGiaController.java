package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.MaGiamGia;
import com.example.du_an_tot_nghiep.repository.MaGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@Controller
@RequestMapping("/ma-giam-gia")
public class MaGiamGiaController {

    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;

    @GetMapping
    public String listMaGiamGia(@RequestParam(value = "search", required = false) String search,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "5") int size,
                                Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("ngayBatDau").descending());
        Page<MaGiamGia> maGiamGiaPage;

        if (search != null && !search.isEmpty()) {
            maGiamGiaPage = maGiamGiaRepository.findByMaContainingIgnoreCase(search, pageable);
        } else {
            maGiamGiaPage = maGiamGiaRepository.findAll(pageable);
        }

        model.addAttribute("maGiamGias", maGiamGiaPage.getContent());
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", maGiamGiaPage.getTotalPages());
        model.addAttribute("totalItems", maGiamGiaPage.getTotalElements());

        return "magiamgia/index";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("maGiamGia", new MaGiamGia());
        return "magiamgia/create";
    }

    @PostMapping("/create")
    public String createMaGiamGia(@ModelAttribute("maGiamGia") MaGiamGia maGiamGia) {
        maGiamGia.setNgayTao(LocalDateTime.now());
        //maGiamGia.setNgayCapNhat(LocalDateTime.now());
        maGiamGiaRepository.save(maGiamGia);
        return "redirect:/ma-giam-gia";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        MaGiamGia maGiamGia = maGiamGiaRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        model.addAttribute("maGiamGia", maGiamGia);
        return "magiamgia/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateMaGiamGia(@PathVariable("id") Long id, @ModelAttribute("maGiamGia") MaGiamGia maGiamGia) {
        maGiamGia.setId(id);

        maGiamGia.setNgayCapNhat(LocalDateTime.now());
        maGiamGiaRepository.save(maGiamGia);
        return "redirect:/ma-giam-gia";
    }

    @GetMapping("/delete/{id}")
    public String deleteMaGiamGia(@PathVariable("id") Long id) {
        maGiamGiaRepository.deleteById(id);
        return "redirect:/ma-giam-gia";
    }
}