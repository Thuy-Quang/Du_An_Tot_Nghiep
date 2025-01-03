package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.security.JwtUtil;
import com.example.du_an_tot_nghiep.service.NguoiDungDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/xac-thuc")
public class DangNhapController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NguoiDungDetailsService nguoiDungDetailsService;

    // Đăng nhập
    @PostMapping("/dang-nhap")
    public ResponseEntity<?> dangNhap(@RequestParam String tenDangNhap, @RequestParam String matKhau) {
        // Xác thực người dùng
        Boolean isUserInDB = nguoiDungDetailsService.checkUserInDB(tenDangNhap, matKhau);
        if (!isUserInDB) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Tên đăng nhập hoặc mật khẩu không chính xác");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        Long userId = nguoiDungDetailsService.getUserIdByUsername(tenDangNhap);

        // Tạo token JWT
        try {
            String token = jwtUtil.generateToken(tenDangNhap, userId);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Đăng nhập thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Không thể tạo JWT");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    // Đăng xuất
    @PostMapping("/dang-xuat")
    public ResponseEntity<?> dangXuat() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đăng xuất thành công");
        return ResponseEntity.ok(response);
    }
}
