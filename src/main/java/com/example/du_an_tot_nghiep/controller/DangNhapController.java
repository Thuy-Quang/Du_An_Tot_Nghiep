package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.security.JwtUtil;
import com.example.du_an_tot_nghiep.service.NguoiDungDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/api/xac-thuc")
public class DangNhapController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NguoiDungDetailsService nguoiDungDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Đăng nhập
    @PostMapping("/dang-nhap")
    public RedirectView dangNhap(@RequestParam String tenDangNhap, @RequestParam String matKhau, RedirectAttributes redirectAttributes) {
        // Xác thực người dùng
        Boolean isUserInDB = nguoiDungDetailsService.checkUserInDB(tenDangNhap, matKhau);
        if (!isUserInDB) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác");
            return new RedirectView("/dang-nhap/hien-thi");
        }

        // Tạo token JWT
        try {
            String token = jwtUtil.generateToken(tenDangNhap);
            redirectAttributes.addFlashAttribute("successMessage", "Đăng nhập thành công");
            redirectAttributes.addFlashAttribute("token", token);  // Lưu token nếu cần sử dụng
            return new RedirectView("/index");  // Chuyển hướng về trang chủ sau khi đăng nhập thành công
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi chi tiết để kiểm tra
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể tạo JWT");
            return new RedirectView("/dang-nhap/hien-thi");
        }
    }
}
