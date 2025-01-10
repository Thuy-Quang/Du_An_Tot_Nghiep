package com.example.du_an_tot_nghiep.model;

import com.example.du_an_tot_nghiep.entity.NguoiDung;
import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DonHangReq {
    private Long nguoiDungId;  // ID của người dùng
    private String phuongThucThanhToan;  // Phương thức thanh toán
    private Double tongTien;  // Tổng tiền đơn hàng
    private List<ChiTietDonHangReq> sanPhamList;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String diaChi;
}


