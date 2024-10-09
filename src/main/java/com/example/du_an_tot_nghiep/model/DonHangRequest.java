package com.example.du_an_tot_nghiep.model;

import com.example.du_an_tot_nghiep.entity.NguoiDung;
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
    private Long nguoiDungId;              // ID của người dùng (người đặt hàng)
    private String trangThai;               // Trạng thái đơn hàng
    private float tongTien;                 // Tổng tiền của đơn hàng
    private String phuongThucThanhToan;    // Phương thức thanh toán
    private Date ngayTao;                   // Ngày tạo đơn hàng
    private Date ngayCapNhat;                // Ngày cập nhật đơn hàng
    private String trangThaiThanhToan;      // Trạng thái thanh toán
    private String diaChi;
    // Địa chỉ giao hàng
    private NguoiDung nguoiDung; // ID người dùng
}