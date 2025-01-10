package com.example.du_an_tot_nghiep.model;

import java.time.LocalDate;

public class MaGiamGiaKhachHangDTO {
    private String maGiamGiaMa;
    private float phanTramGiam;
    private LocalDate ngayBatDau;
    private LocalDate ngayHetHan;
    private String maGiamGiaTrangThai;
    private Long nguoiDungId;

    public MaGiamGiaKhachHangDTO(String maGiamGiaMa, float phanTramGiam, LocalDate ngayBatDau,
                                 LocalDate ngayHetHan, String maGiamGiaTrangThai, Long nguoiDungId) {
        this.maGiamGiaMa = maGiamGiaMa;
        this.phanTramGiam = phanTramGiam;
        this.ngayBatDau = ngayBatDau;
        this.ngayHetHan = ngayHetHan;
        this.maGiamGiaTrangThai = maGiamGiaTrangThai;
        this.nguoiDungId = nguoiDungId;
    }

    // Getters and setters
    public String getMaGiamGiaMa() {
        return maGiamGiaMa;
    }

    public void setMaGiamGiaMa(String maGiamGiaMa) {
        this.maGiamGiaMa = maGiamGiaMa;
    }

    public float getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(float phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(LocalDate ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public String getMaGiamGiaTrangThai() {
        return maGiamGiaTrangThai;
    }

    public void setMaGiamGiaTrangThai(String maGiamGiaTrangThai) {
        this.maGiamGiaTrangThai = maGiamGiaTrangThai;
    }

    public Long getNguoiDungId() {
        return nguoiDungId;
    }

    public void setNguoiDungId(Long nguoiDungId) {
        this.nguoiDungId = nguoiDungId;
    }
}
