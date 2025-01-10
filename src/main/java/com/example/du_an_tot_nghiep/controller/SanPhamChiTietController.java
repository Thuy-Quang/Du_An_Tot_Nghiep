package com.example.du_an_tot_nghiep.controller;
import com.example.du_an_tot_nghiep.entity.KichCo;
import com.example.du_an_tot_nghiep.entity.SanPhamChiTiet;
import com.example.du_an_tot_nghiep.repository.SanPhamChiTietRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import com.example.du_an_tot_nghiep.repository.MauSacRepository;
import com.example.du_an_tot_nghiep.repository.KichCoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/san-pham-chi-tiet")
public class SanPhamChiTietController {

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private KichCoRepository kichCoRepository;

    @GetMapping
    public String listSanPhamChiTiet(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            Model model) {
        Page<SanPhamChiTiet> sanPhamChiTietPage;
        if (search != null && !search.isEmpty()) {
            sanPhamChiTietPage = sanPhamChiTietRepository.findBySearchTerm(search, PageRequest.of(page, size, Sort.by("ngayTao").descending()));
        } else {
            sanPhamChiTietPage = sanPhamChiTietRepository.findAll(PageRequest.of(page, size, Sort.by("ngayTao").descending()));
        }

        model.addAttribute("sanPhamChiTietPage", sanPhamChiTietPage);
        model.addAttribute("sanPhamList", sanPhamRepository.findAll());
        model.addAttribute("mauSacList", mauSacRepository.findAll());
        model.addAttribute("kichCoList", kichCoRepository.findAll());
        return "san_pham_chi_tiet/list";
    }




    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("sanPhamChiTiet", new SanPhamChiTiet());
        model.addAttribute("sanPhamList", sanPhamRepository.findAll());
        model.addAttribute("mauSacList", mauSacRepository.findAll());
        model.addAttribute("kichCoList", kichCoRepository.findAll());
        return "san_pham_chi_tiet/create";
    }


    @PostMapping("/create")
    public String createSanPhamChiTiet(@ModelAttribute SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTiet.setNgayTao(new Date());
        sanPhamChiTiet.setNgayCapNhat(new Date());
        sanPhamChiTietRepository.save(sanPhamChiTiet);
        return "redirect:/san-pham-chi-tiet";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("\n" +
                        "Id  không hợp lệ:" + id));
        model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);
        model.addAttribute("sanPhamList", sanPhamRepository.findAll());
        model.addAttribute("mauSacList", mauSacRepository.findAll());
        model.addAttribute("kichCoList", kichCoRepository.findAll());
        return "san_pham_chi_tiet/edit";
    }

    @PostMapping("/edit/{id}")
    public String editSanPhamChiTiet(@PathVariable Long id, @ModelAttribute SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTiet.setNgayCapNhat(new Date());
        sanPhamChiTietRepository.save(sanPhamChiTiet);
        return "redirect:/san-pham-chi-tiet";
    }

    @GetMapping("/delete/{id}")
    public String deleteSanPhamChiTiet(@PathVariable Long id) {
        sanPhamChiTietRepository.deleteById(id);
        return "redirect:/san-pham-chi-tiet";
    }

    //    @GetMapping("/create_them")
//    public String list(Model model) {
//        model.addAttribute("sanPhamChiTiet", new SanPhamChiTiet());
//        model.addAttribute("sanPhamList", sanPhamRepository.findAll());
//        model.addAttribute("mauSacList", mauSacRepository.findAll());
//        model.addAttribute("kichCoList", kichCoRepository.findAll());
//        return "san_pham_chi_tiet/them";
//    }

}
