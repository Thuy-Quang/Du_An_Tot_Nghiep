package com.example.du_an_tot_nghiep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GioHangController {

    @GetMapping("/giohang")
    public String hienThiGioHang() {
        return "giohang/giohang"; // Trả về tên template Thymeleaf
    }

    @GetMapping("/giohangdetail")
    public String hienThiGioHangchitiet() {
        return "giohang/giohangdetail"; // Trả về tên template Thymeleaf
    }
}