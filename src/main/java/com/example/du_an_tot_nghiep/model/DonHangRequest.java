package com.example.du_an_tot_nghiep.model;

import com.example.du_an_tot_nghiep.entity.NguoiDung;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DonHangRequest {

    @NotEmpty(message = "Trạng thái không được để trống")  // Đảm bảo chuỗi không trống
    private String trangThai;

    @DecimalMin(value = "0.0", message = "Tổng tiền phải lớn hơn hoặc bằng 0")
    @NotNull(message = "Tổng tiền không được để trống")
    private Double tongTien;

    @NotEmpty(message = "Trạng thái thanh toán không được để trống")  // Đảm bảo chuỗi không trống
    private String trangThaiThanhToan;

    @NotEmpty(message = "Phương thức thanh toán không được để trống")  // Đảm bảo chuỗi không trống
    private String phuongThucThanhToan;

    @NotNull(message = "ID người dùng không được để trống")
    private Long nguoiDungId;

}