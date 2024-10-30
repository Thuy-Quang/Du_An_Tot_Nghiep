package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.VaiTro;
import com.example.du_an_tot_nghiep.service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/vai-tro")
public class VaiTroController {

    @Autowired
    private VaiTroService vaiTroService;

    @GetMapping
    public String listVaiTro(Model model) {
        model.addAttribute("vaiTros", vaiTroService.findAll());
        return "vaiTro/list"; // Tên view để hiển thị danh sách vai trò
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("vaiTro", new VaiTro());
        return "vaiTro/add"; // Tên view để thêm vai trò
    }

    @PostMapping("/add")
    public String addVaiTro(@ModelAttribute VaiTro vaiTro, RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra nếu tên vai trò đã tồn tại
            if (vaiTroService.existsByTenVaiTro(vaiTro.getTenVaiTro())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Tên vai trò đã tồn tại!");
                return "redirect:/vai-tro/add";
            }
            vaiTroService.save(vaiTro);
            redirectAttributes.addFlashAttribute("message", "Thêm vai trò thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thêm vai trò thất bại: " + e.getMessage());
        }
        return "redirect:/vai-tro";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<VaiTro> vaiTro = vaiTroService.findById(id);
        if (vaiTro.isPresent()) {
            model.addAttribute("vaiTro", vaiTro.get());
            return "vaiTro/edit"; // Tên view để chỉnh sửa vai trò
        }
        return "redirect:/vai-tro"; // Nếu không tìm thấy vai trò, chuyển hướng về danh sách
    }

    @PostMapping("/edit/{id}")
    public String editVaiTro(@PathVariable Long id, @ModelAttribute VaiTro vaiTro, RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra nếu tên vai trò đã tồn tại (ngoại trừ vai trò hiện tại)
            if (vaiTroService.existsByTenVaiTro(vaiTro.getTenVaiTro()) && !vaiTroService.findById(id).get().getTenVaiTro().equals(vaiTro.getTenVaiTro())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Tên vai trò đã tồn tại!");
                return "redirect:/vai-tro/edit/" + id;
            }
            vaiTroService.update(id, vaiTro);
            redirectAttributes.addFlashAttribute("message", "Cập nhật vai trò thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật vai trò thất bại: " + e.getMessage());
        }
        return "redirect:/vai-tro";
    }

    @GetMapping("/delete/{id}")
    public String deleteVaiTro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vaiTroService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa vai trò thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Xóa vai trò thất bại: " + e.getMessage());
        }
        return "redirect:/vai-tro";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        Optional<VaiTro> vaiTro = vaiTroService.findById(id);
        if (vaiTro.isPresent()) {
            model.addAttribute("vaiTro", vaiTro.get());
            return "vaiTro/detail"; // Tên view để hiển thị chi tiết vai trò
        }
        return "redirect:/vai-tro"; // Nếu không tìm thấy vai trò, chuyển hướng về danh sách
    }
}
