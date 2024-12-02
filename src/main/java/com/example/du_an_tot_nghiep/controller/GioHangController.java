package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.GioHang;
import com.example.du_an_tot_nghiep.entity.KichCo;
import com.example.du_an_tot_nghiep.entity.MauSac;
import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.model.GioHangDTO;
import com.example.du_an_tot_nghiep.repository.KichCoRepository;
import com.example.du_an_tot_nghiep.repository.MauSacRepository;
import com.example.du_an_tot_nghiep.service.GioHangService;
import com.example.du_an_tot_nghiep.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class GioHangController {
    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private MauSacRepository mauSacRepository;
    @Autowired
    private KichCoRepository kichCoRepository;

    @Autowired
    private GioHangService gioHangService;

    @GetMapping("/giohang")
    public String hienThiDanhSachSanPham(Model model) {
        List<SanPham> sanPhams = sanPhamService.getall();
        List<MauSac> mauSacs = mauSacRepository.findAll();
        List<KichCo> kichCos = kichCoRepository.findAll();
        model.addAttribute("sanPhams", sanPhams);
        model.addAttribute("mauSacs", mauSacs);
        model.addAttribute("kichxCos", kichCos);
        return "giohang/giohang";
    }
    @GetMapping("/giohangdetail")
    public String hienThiSanPhamTrongGioHang(){
        return "giohang/giohangdetail";
    }
    @GetMapping("/dat-hang")
    public String datHang() {
        return "giohang/dathang";  // Trả về view hoặc chuyển hướng đến trang đặt hàng
    }
    @GetMapping("/donhang/khachhang")
    public  String donhangcuakhachhang(){
        return "giohang/donhang";
    }
}
