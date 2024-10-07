package com.example.du_an_tot_nghiep.controller.SanPham.HienThiCTSP;



import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.model.SanPhamRequest;
import com.example.du_an_tot_nghiep.repository.LoaiSanPhamRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import com.example.du_an_tot_nghiep.service.SanPham.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/HienThi")
public class HienThiController {

    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    SanPhamService sanPhamService;

    @GetMapping("/GetAll")
    public String show(Model model){
        model.addAttribute("listSPham",sanPhamRepository.findAll());
        model.addAttribute("listLSP",loaiSanPhamRepository.findAll());
        return "SanPham/HienThiSP/index";

    }

    @GetMapping("/create")
    private  String getProject(Model model){
        model.addAttribute("listSPham",sanPhamRepository.findAll());
        model.addAttribute("listLSP",loaiSanPhamRepository.findAll());

        return "SanPham/HienThiSP/Add";
    }

    @PostMapping("/create")
    private String postSanPham(SanPhamRequest sanPhamRequest) {
        sanPhamService.addSanPham(sanPhamRequest);
        return "redirect:/HienThi/GetAll";
    }

    @GetMapping("/listSPham/edit/id/{id}")
    private String putSanPham(@PathVariable("id") Long id, Model model) {
        model.addAttribute("detail", sanPhamRepository.findById(id).get());
        model.addAttribute("listLSP",loaiSanPhamRepository.findAll());
        return "SanPham/HienThiSP/Update";
    }

    @PostMapping("/listSPham/edit/id/{id}")
    private String putSanPham(@PathVariable Long id, SanPhamRequest sanPhamRequest) {
        sanPhamService.updateSanPham(id, sanPhamRequest);
        return "redirect:/HienThi/GetAll";
    }


    @GetMapping("/listSPham/detail/id/{id}")
    private String detail(@PathVariable Long  id,Model model){

        SanPham sanPham = sanPhamRepository.findById(id).get();
        model.addAttribute("detail",sanPham);
        model.addAttribute("listSPham",sanPhamRepository.findAll());
        model.addAttribute("listLSP",loaiSanPhamRepository.findAll());
        return "SanPham/HienThiSP/Detail";
    }

    @PostMapping("/listSPham/delete/id/{id}")
    private String delete(@PathVariable("id") Long id) {
        sanPhamRepository.delete(sanPhamRepository.findById(id).get());
        return "redirect:/HienThi/GetAll";
    }
}
