package com.example.du_an_tot_nghiep.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SanPhamRequest {
    private String tenSanPham;

    private String moTa;

    private float gia;

    private MultipartFile hinhAnh;

    private String mauSac;

    private String kichCo;

    private int soLuong;

    private String trangThai;

    private Long loaiSanPham;
}
