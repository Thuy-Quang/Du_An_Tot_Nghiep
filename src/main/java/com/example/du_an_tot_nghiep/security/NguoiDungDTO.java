package com.example.du_an_tot_nghiep.security;

import lombok.Data;

@Data
public class NguoiDungDTO {
    String tenDangNhap;
    String matKhau;

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }
}
