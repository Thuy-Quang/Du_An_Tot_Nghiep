package com.example.du_an_tot_nghiep.controller;
import com.example.du_an_tot_nghiep.entity.MauSac;
import com.example.du_an_tot_nghiep.repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mau-sac")
public class MauSacController {
    @Autowired
    private MauSacRepository mauSacRepository;

    @GetMapping("/list")
    public String listMauSac(Model model) {
        List<MauSac> mauSacList = mauSacRepository.findAll();
        model.addAttribute("mauSacList", mauSacList);
        return "mausac/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("mauSac", new MauSac());
        return "mausac/add";
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
        return "mausac/edit";
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
