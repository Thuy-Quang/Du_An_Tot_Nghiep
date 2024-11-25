package com.example.du_an_tot_nghiep.model;

import lombok.Data;

@Data
public class SanPhamDTO {
        private Long id;
        private String tenSanPham;
        private Float gia;
        private String moTa;
        private String hinhAnh;

        // Constructor, getter, setter
}
