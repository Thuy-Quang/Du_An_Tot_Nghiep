package com.example.du_an_tot_nghiep.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class ChiTietDonHangReq {

    private Long sanPhamChiTietId;  // ID của sản phẩm
    private Integer soLuong;            // Số lượng của sản phẩm
    private Float giaDonVi;         // Giá đơn vị của sản phẩm

    // Constructor có tham số
    public ChiTietDonHangReq(Long sanPhamChiTietId, Integer soLuong, Float giaDonVi) {
        this.sanPhamChiTietId = sanPhamChiTietId;
        this.soLuong = soLuong;
        this.giaDonVi = giaDonVi;
    }

    // Getter custom để lấy sanPhamId
    public Long getSanPhamId() {
        return this.sanPhamChiTietId;
    }
}