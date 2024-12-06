package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.MaGiamGia;
import com.example.du_an_tot_nghiep.service.MaGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/ma-giam-gia")
public class MaGiamGiaController {

    @Autowired
    private MaGiamGiaService maGiamGiaService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, "ngayBatDau", new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(Date.class, "ngayHetHan", new CustomDateEditor(dateFormat, true));
    }

    @GetMapping
    public String listMaGiamGia(Model model) {
        model.addAttribute("listMaGiamGia", maGiamGiaService.getAllMaGiamGia());
        return "magiamgia/index"; // Trả về view danh sách
    }

    @GetMapping("/create")
    public String createMaGiamGiaForm(Model model) {
        model.addAttribute("maGiamGia", new MaGiamGia());
        return "magiamgia/create"; // Trả về view thêm mới
    }

//    @PostMapping("/save")
//    public String saveMaGiamGia(@ModelAttribute MaGiamGia maGiamGia) {
//        maGiamGiaService.saveOrUpdateMaGiamGia(maGiamGia);
//        return "redirect:/ma-giam-gia"; // Quay lại danh sách
//    }

    @GetMapping("/edit/{id}")
    public String editMaGiamGiaForm(@PathVariable Long id, Model model) {
        Optional<MaGiamGia> maGiamGiaOptional = maGiamGiaService.getMaGiamGiaById(id);
        if (maGiamGiaOptional.isPresent()) {
            model.addAttribute("maGiamGia", maGiamGiaOptional.get());
            return "magiamgia/edit"; // Trả về view sửa
        }
        return "redirect:/ma-giam-gia"; // Nếu không tìm thấy, quay lại danh sách
    }

//    @PostMapping("/update/{id}")
//    public String updateMaGiamGia(@PathVariable Long id, @ModelAttribute MaGiamGia maGiamGia) {
//        maGiamGia.setId(id); // Đặt ID để cập nhật
//        maGiamGiaService.saveOrUpdateMaGiamGia(maGiamGia);
//        return "redirect:/ma-giam-gia"; // Quay lại danh sách
//    }

    @PostMapping("/delete/{id}")
    public String deleteMaGiamGia(@PathVariable Long id) {
        maGiamGiaService.deleteMaGiamGia(id);
        return "redirect:/ma-giam-gia"; // Quay lại danh sách
    }
}