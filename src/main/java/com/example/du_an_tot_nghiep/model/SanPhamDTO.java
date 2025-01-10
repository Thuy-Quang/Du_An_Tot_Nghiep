package com.example.du_an_tot_nghiep.model;

import lombok.Data;

import java.util.List;

@Data
public class SanPhamDTO {
        private Long id;
        private String tenSanPham;
        private String hinhAnh;
        private Float gia;
        private List<String> mauSacs; // Danh sách tên màu sắc
        private List<String> kichCo; // Danh sách tên kích cỡ
        private Integer soLuong;


        // Constructor, getter, setter
}
