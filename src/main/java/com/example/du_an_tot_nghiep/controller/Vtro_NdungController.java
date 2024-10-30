package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.VaiTroNguoiDung;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import com.example.du_an_tot_nghiep.repository.VaiTroNguoiDungRepository;
import com.example.du_an_tot_nghiep.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/vai-tro-nguoi-dung")
public class Vtro_NdungController {

    @Autowired
    private VaiTroNguoiDungRepository vaiTroNguoiDungRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @GetMapping
    public String listVaiTroNguoiDung(Model model) {
        List<VaiTroNguoiDung> listVaiTroNguoiDung = vaiTroNguoiDungRepository.findAll();
        model.addAttribute("vaiTroNguoiDungList", listVaiTroNguoiDung);
        return "vaiTroNguoiDung/list";
    }

    @GetMapping("/add")
    public String addVaiTroNguoiDungForm(Model model) {
        model.addAttribute("vaiTroNguoiDung", new VaiTroNguoiDung());
        model.addAttribute("nguoiDungList", nguoiDungRepository.findAll());
        model.addAttribute("vaiTroList", vaiTroRepository.findAll());
        return "vaiTroNguoiDung/add";
    }

    @PostMapping("/save")
    public String saveVaiTroNguoiDung(@ModelAttribute VaiTroNguoiDung vaiTroNguoiDung, RedirectAttributes redirectAttributes) {
        try {
            vaiTroNguoiDungRepository.save(vaiTroNguoiDung);
            redirectAttributes.addFlashAttribute("message", "Thêm vai trò người dùng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thêm vai trò người dùng thất bại!");
        }
        return "redirect:/vai-tro-nguoi-dung";
    }

    @GetMapping("/edit/{id}")
    public String editVaiTroNguoiDungForm(@PathVariable Long id, Model model) {
        VaiTroNguoiDung vaiTroNguoiDung = vaiTroNguoiDungRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò người dùng"));
        model.addAttribute("vaiTroNguoiDung", vaiTroNguoiDung);
        model.addAttribute("nguoiDungList", nguoiDungRepository.findAll());
        model.addAttribute("vaiTroList", vaiTroRepository.findAll());
        return "vaiTroNguoiDung/edit";
    }

    @PostMapping("/update")
    public String updateVaiTroNguoiDung(@ModelAttribute VaiTroNguoiDung vaiTroNguoiDung, RedirectAttributes redirectAttributes) {
        try {
            vaiTroNguoiDungRepository.save(vaiTroNguoiDung);
            redirectAttributes.addFlashAttribute("message", "Cập nhật vai trò người dùng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật vai trò người dùng thất bại!");
        }
        return "redirect:/vai-tro-nguoi-dung";
    }

    @GetMapping("/delete/{id}")
    public String deleteVaiTroNguoiDung(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vaiTroNguoiDungRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa vai trò người dùng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Xóa vai trò người dùng thất bại!");
        }
        return "redirect:/vai-tro-nguoi-dung";
    }

    @GetMapping("/detail/{id}")
    public String detailVaiTroNguoiDung(@PathVariable Long id, Model model) {
        VaiTroNguoiDung vaiTroNguoiDung = vaiTroNguoiDungRepository.findById(id).orElse(null);
        if (vaiTroNguoiDung == null) {
            model.addAttribute("errorMessage", "Không tìm thấy vai trò người dùng.");
            return "vaiTroNguoiDung/detail";
        }
        model.addAttribute("vaiTroNguoiDung", vaiTroNguoiDung);
        return "vaiTroNguoiDung/detail";
    }
}
