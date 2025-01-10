package com.example.du_an_tot_nghiep.controller;
import com.example.du_an_tot_nghiep.entity.MauSac;
import com.example.du_an_tot_nghiep.repository.MauSacRepository;
import com.example.du_an_tot_nghiep.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mau-sac")
public class MauSacController {
    @Autowired
    MauSacService mauSacService;
    @Autowired
    private MauSacRepository mauSacRepository;


    @GetMapping("/list")
    public String listMauSac(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MauSac> mauSacPage = mauSacRepository.findByTenMauContainingIgnoreCase(search, pageable);

        model.addAttribute("mauSacPage", mauSacPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", mauSacPage.getTotalPages());
        model.addAttribute("search", search);

        return "mauSac/list";
    }

    @GetMapping("/hien-thi")
    public ResponseEntity<List<MauSac>> getAllMauSac() {
        List<MauSac> mauSacList = mauSacService.getAllMauSac();
        return ResponseEntity.ok(mauSacList);
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("mauSac", new MauSac());
        return "mauSac/add";
    }

    @PostMapping("/add")
    public String addMauSac(@ModelAttribute MauSac mauSac) {
        mauSacRepository.save(mauSac);
        return "redirect:/mau-sac/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MauSac mauSac = mauSacRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid MauSac ID:" + id));
        model.addAttribute("mauSac", mauSac);
        return "mauSac/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateMauSac(@PathVariable Long id, @ModelAttribute MauSac mauSac) {
        mauSac.setId(id);
        mauSacRepository.save(mauSac);
        return "redirect:/mau-sac/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteMauSac(@PathVariable Long id) {
        MauSac mauSac = mauSacRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid MauSac ID:" + id));
        mauSacRepository.delete(mauSac);
        return "redirect:/mau-sac/list";
    }
}
