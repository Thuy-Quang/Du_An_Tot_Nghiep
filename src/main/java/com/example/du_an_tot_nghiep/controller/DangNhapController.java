package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.security.JwtUtil;
import com.example.du_an_tot_nghiep.security.NguoiDungDTO;
import com.example.du_an_tot_nghiep.service.NguoiDungDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
    public ResponseEntity<?> dangNhap(@RequestBody NguoiDungDTO nguoiDungDto) {
        /* TODO:
        *   Chỗ này cần lấy thông tin về quyền để đưa vào token
        */
        // Xác thực người dùng
        Boolean isUserInDB = nguoiDungDetailsService.checkUserInDB(
                nguoiDungDto.getTenDangNhap(),
                nguoiDungDto.getMatKhau()
        );
        if (!isUserInDB){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("tên dăng nhập hoặc mật khẩu khôgg chính sác ");
        }

        //Tạo token JWT
        try {
            String token = jwtUtil.generateToken(nguoiDungDto.getTenDangNhap());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi chi tiết để kiểm tra
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể tạo JWT");
        }
    }


//    // Đăng ký
//    @PostMapping("/dang-ky")
//    public ResponseEntity<?> dangKy(@RequestBody NguoiDung nguoiDung) {
//        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
//        nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDung.getMatKhau()));
//
//        // Lưu người dùng vào cơ sở dữ liệu
//        nguoiDungDetailsService.dangKyNguoiDung(nguoiDung);
//
//        return ResponseEntity.ok("Đăng ký thành công!");
//    }
//
//    // Đổi mật khẩu
//    @PostMapping("/doi-mat-khau")
//    public ResponseEntity<?> doiMatKhau(@RequestParam Long nguoiDungId, @RequestBody String matKhauMoi) {
//        // Mã hóa mật khẩu mới
//        String matKhauMaHoa = passwordEncoder.encode(matKhauMoi);
//
//        nguoiDungDetailsService.doiMatKhau(nguoiDungId, matKhauMaHoa);
//        return ResponseEntity.ok("Đổi mật khẩu thành công!");
//    }
}
