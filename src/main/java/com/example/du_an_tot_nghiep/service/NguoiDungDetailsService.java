package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.NguoiDung;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class NguoiDungDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));

        return new org.springframework.security.core.userdetails.User(
                nguoiDung.getTenDangNhap(), nguoiDung.getMatKhau(), new ArrayList<>()
        );
    }

    // Thêm người dùng mới
    public String addNguoiDung(NguoiDung nguoiDung) {
        // Kiểm tra tên đăng nhập đã tồn tại chưa
        if (nguoiDungRepository.existsByTenDangNhap(nguoiDung.getTenDangNhap())) {
            return "Tên đăng nhập đã tồn tại trong cơ sở dữ liệu.";
        }

        // Kiểm tra email đã tồn tại chưa
        if (nguoiDungRepository.existsByEmail(nguoiDung.getEmail())) {
            return "Email đã tồn tại trong cơ sở dữ liệu.";
        }

        // Kiểm tra số điện thoại đã tồn tại chưa
        if (nguoiDungRepository.existsBySoDienThoai(nguoiDung.getSoDienThoai())) {
            return "Số điện thoại đã tồn tại trong cơ sở dữ liệu.";
        }

        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        String encodedPassword = passwordEncoder.encode(nguoiDung.getMatKhau());
        nguoiDung.setMatKhau(encodedPassword);

        // Lưu người dùng vào cơ sở dữ liệu
        nguoiDungRepository.save(nguoiDung);
        return "Thêm người dùng thành công.";
    }

    // Kiểm tra người dùng có trong cơ sở dữ liệu và mật khẩu có khớp không
    public Boolean checkUserInDB(String tenDangNhap, String matKhau) {
        Optional<NguoiDung> userOpt = nguoiDungRepository.findByTenDangNhap(tenDangNhap);
        if (userOpt.isPresent()) {
            // So sánh mật khẩu đã mã hóa trong cơ sở dữ liệu với mật khẩu người dùng nhập
            return passwordEncoder.matches(matKhau, userOpt.get().getMatKhau());
        }
        return false; // Người dùng không tồn tại
    }

    // Lấy ID người dùng từ tên đăng nhập
    public Long getUserIdByUsername(String tenDangNhap) {
        Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findByTenDangNhap(tenDangNhap);
        return nguoiDungOpt.map(NguoiDung::getId).orElse(null);  // Trả về ID người dùng nếu tồn tại
    }

    // Kiểm tra tên đăng nhập đã tồn tại
    public boolean existsByTenDangNhap(String tenDangNhap) {
        return nguoiDungRepository.existsByTenDangNhap(tenDangNhap);
    }

    // Kiểm tra email đã tồn tại
    public boolean existsByEmail(String email) {
        return nguoiDungRepository.existsByEmail(email);
    }

    // Kiểm tra số điện thoại đã tồn tại
    public boolean existsBySoDienThoai(String soDienThoai) {
        return nguoiDungRepository.existsBySoDienThoai(soDienThoai);
    }

    // Lưu người dùng mới vào cơ sở dữ liệu
    public void saveUser(NguoiDung nguoiDung) {
        nguoiDungRepository.save(nguoiDung);
    }
    public String getPasswordByUsername(String username) {
        Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findByTenDangNhap(username);
        if (nguoiDungOpt.isPresent()) {
            return nguoiDungOpt.get().getMatKhau();
        }
        return null;
    }
}

