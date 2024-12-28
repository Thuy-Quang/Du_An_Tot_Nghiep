package com.example.du_an_tot_nghiep.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietDonHangDTO {
    private String tenSanPham;
    private String tenMauSac;
    private String tenKichCo;
    private int soLuong;
    private double giaDonVi;
    private double tongGia;
}
