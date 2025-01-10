package com.example.du_an_tot_nghiep.controller;
import com.example.du_an_tot_nghiep.entity.KichCo;
import com.example.du_an_tot_nghiep.repository.KichCoRepository;
import com.example.du_an_tot_nghiep.service.KichCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/kich-co")
public class KichCoController {

    @Autowired
    KichCoService kichCoService;
    @Autowired
    private KichCoRepository kichCoRepository;

    @GetMapping("/list")
    public String listKichCo(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KichCo> kichCoPage;

        if (keyword == null || keyword.isEmpty()) {
            kichCoPage = kichCoRepository.findAll(pageable);
        } else {
            kichCoPage = kichCoRepository.findByTenKichCoContainingIgnoreCase(keyword, pageable);
        }

        model.addAttribute("kichCoPage", kichCoPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "kichco/list";
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("kichCo", new KichCo());
        return "kichco/add";
    }

    @PostMapping("/add")
    public String addKichCo(@ModelAttribute KichCo kichCo) {
        kichCoRepository.save(kichCo);
        return "redirect:/kich-co/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        KichCo kichCo = kichCoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid KichCo ID:" + id));
        model.addAttribute("kichCo", kichCo);
        return "kichco/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateKichCo(@PathVariable Long id, @ModelAttribute KichCo kichCo) {
        kichCo.setId(id);
        kichCoRepository.save(kichCo);
        return "redirect:/kich-co/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteKichCo(@PathVariable Long id) {
        KichCo kichCo = kichCoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid KichCo ID:" + id));
        kichCoRepository.delete(kichCo);
        return "redirect:/kich-co/list";
    }
}
