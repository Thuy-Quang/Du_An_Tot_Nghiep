package com.example.du_an_tot_nghiep.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoaiSanPhamRequest {
    private Long id;

        @NotBlank(message = "Tên loại sản phẩm không được để trống")
        @Size(min = 2, max = 50, message = "Tên loại sản phẩm phải từ 2 đến 50 ký tự")
        private String tenLoai;
        @NotBlank(message = "Mô tả không được để trống")
        @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
        private String moTa;

        @NotNull(message = "Ngày tạo không được để trống")
        private Date ngayTao;

        @NotNull(message = "Ngày cập nhật không được để trống")
        private Date ngayCapNhat;
}

