package com.example.du_an_tot_nghiep.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class ChiTietDonHangReq {

    private Long sanPhamChiTietId;  // ID của sản phẩm
    private int soLuong;      // Số lượng của sản phẩm
    private float giaDonVi;
    // Constructor mặc định (Lombok sẽ tạo ra tự động nhờ @Getter và @Setter)
    public ChiTietDonHangReq() {}

    // Constructor có tham số (có thể tùy chọn)
    public ChiTietDonHangReq(Long sanPhamId, int soLuong) {
        this.sanPhamChiTietId = sanPhamChiTietId;
        this.soLuong = soLuong;
    }
}

