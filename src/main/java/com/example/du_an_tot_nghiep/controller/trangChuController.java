package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class trangChuController {
    @Autowired
    SanPhamRepository sanPhamRepository;
//    @PreAuthorize("hasRole('Khách hàng')")
    @GetMapping("/index")
    public String index(Model model){
        List<SanPham> list = sanPhamRepository.findAll();
        model.addAttribute("list",list);
        return "layout/index";

    }
    @GetMapping("/dang-nhap/hien-thi")
    public String hienThiTrangDangNhap(){
        return "DangNhap/dangnhap"; // Không cần đuôi ".html"
    }
}
