package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nguoi_dung")
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải có ít nhất 3 ký tự và tối đa 50 ký tự")
    @Column(name = "ten_dang_nhap", nullable = false, unique = true)
    private String tenDangNhap;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu phải có ít nhất 6 ký tự và tối đa 100 ký tự")
    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @NotEmpty(message = "Họ và tên không được để trống")
    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải có 10 chữ số")
    @Column(name = "so_dien_thoai", nullable = false)
    private String soDienThoai;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "ngay_tao", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Transient
    private String xacNhanMatKhau; // Trường không được lưu vào database, dùng để xác nhận mật khẩu trong form
}
