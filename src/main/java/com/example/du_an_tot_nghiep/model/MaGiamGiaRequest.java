package com.example.du_an_tot_nghiep.model;

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
public class MaGiamGiaRequest {
    private Long id;
    @NotBlank(message = "Mã giảm giá không được để trống")
    @Size(min = 3, max = 10, message = "Mã giảm giá phải từ 3 đến 10 ký tự")
    private String ma;
    @Digits(integer = 3, fraction = 2, message = "Phần trăm giảm phải là số và có tối đa 3 chữ số nguyên và 2 chữ số thập phân")
    @Min(value = 0, message = "Phần trăm giảm phải lớn hơn 0")
    @Max(value = 100, message = "Phần trăm giảm phải nhỏ hơn hoặc bằng 100")
    private float phanTramGiam;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private Date ngayBatDau;

    @NotNull(message = "Ngày hết hạn không được để trống")
    private Date ngayHetHan;

    @NotBlank(message = "Trạng thái không được để trống")
    private String trangThai;
}
