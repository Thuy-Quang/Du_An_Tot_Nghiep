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

    // Thay đổi kiểu dữ liệu cho mauSac và kichCo
    private Integer mauSac; // Hoặc Long, tùy thuộc vào cách bạn định nghĩa ID trong MauSac
    private Integer kichCo;  // Hoặc Long, tùy thuộc vào cách bạn định nghĩa ID trong KichCo

    private int soLuong;
    private String trangThai;
    private Long loaiSanPham; // Giữ lại kiểu Long cho loại sản phẩm
}
