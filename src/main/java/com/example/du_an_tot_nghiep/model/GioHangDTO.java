package com.example.du_an_tot_nghiep.model;

import lombok.Data;

@Data
public class GioHangDTO {
    private Long id;       // ID sản phẩm
    private String tenSanPham;   // Tên sản phẩm
    private Double gia;  // Giá sản phẩm
    private Integer soLuong; // Số lượng

    // Getters và setters

}
