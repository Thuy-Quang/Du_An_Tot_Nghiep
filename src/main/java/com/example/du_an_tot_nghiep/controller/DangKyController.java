package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.NguoiDung;
import com.example.du_an_tot_nghiep.security.JwtUtil;

import com.example.du_an_tot_nghiep.service.NguoiDungDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
public class DangKyController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NguoiDungDetailsService nguoiDungDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute("nguoiDung")
    public NguoiDung nguoiDung() {
        return new NguoiDung();
    }

    // Hiển thị trang đăng ký
    @GetMapping("/dang-ky")
    public String showRegistrationForm() {
        return "DangNhap/dangki"; // Đảm bảo file HTML tồn tại tại thư mục templates/DangNhap/
    }
    @PostMapping("/dang-ky")
    public String registerUser(
            @ModelAttribute("nguoiDung") @Valid NguoiDung nguoiDung,
            BindingResult bindingResult,
            Model model) {

        // Kiểm tra lỗi trong form nhập liệu
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Vui lòng kiểm tra lại các trường thông tin.");
            return "DangNhap/dangki";
        }

        // Kiểm tra tên đăng nhập đã tồn tại
        if (nguoiDungDetailsService.existsByTenDangNhap(nguoiDung.getTenDangNhap())) {
            model.addAttribute("errorTenDangNhap", "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.");
            return "DangNhap/dangki";
        }

        // Kiểm tra email đã tồn tại
        if (nguoiDungDetailsService.existsByEmail(nguoiDung.getEmail())) {
            model.addAttribute("errorEmail", "Email đã tồn tại. Vui lòng chọn email khác.");
            return "DangNhap/dangki";
        }

        // Kiểm tra mật khẩu và xác nhận mật khẩu
        if (!nguoiDung.getMatKhau().equals(nguoiDung.getXacNhanMatKhau())) {
            model.addAttribute("errorMatKhau", "Mật khẩu và xác nhận mật khẩu không khớp.");
            return "DangNhap/dangki";
        }

        // Mã hóa mật khẩu trước khi lưu
        nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDung.getMatKhau()));
        nguoiDung.setNgayTao(new Date());
        nguoiDung.setNgayCapNhat(new Date());

        // Lưu người dùng vào cơ sở dữ liệu
        nguoiDungDetailsService.saveUser(nguoiDung);

        model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/dang-nhap/hien-thi";
    }
    @RequestMapping("/change-password")
    public String showChangePasswordPage() {
        return "DangNhap/doimk";  // Trả về tên template "doimk"
    }

//    @PostMapping("/change-password")
//    public String changePassword(@RequestParam("current-password") String currentPassword,
//                                 @RequestParam("new-password") String newPassword,
//                                 @RequestParam("confirm-password") String confirmPassword,
//                                 Model model) {
//
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = userDetails.getUsername();
//
//        // Kiểm tra mật khẩu cũ
//        if (!passwordEncoder.matches(currentPassword, nguoiDungDetailsService.getPasswordByUsername(username))) {
//            model.addAttribute("error", "Mật khẩu cũ không đúng");
//            return "DangNhap/doimk"; // Trả về template "doimk" nếu có lỗi
//        }
//
//        // Kiểm tra mật khẩu mới và xác nhận mật khẩu
//        if (!newPassword.equals(confirmPassword)) {
//            model.addAttribute("error", "Mật khẩu mới và mật khẩu xác nhận không khớp");
//            return "DangNhap/doimk"; // Trả về template "doimk" nếu có lỗi
//        }
//
//        // Mã hóa mật khẩu mới và lưu vào database
//        String encodedNewPassword = passwordEncoder.encode(newPassword);
//        nguoiDungDetailsService.updatePassword(username, encodedNewPassword);
//
//        model.addAttribute("success", "Mật khẩu đã được thay đổi thành công");
//        return "DangNhap/doimk"; // Trở lại trang đổi mật khẩu với thông báo thành công
//    }
}


