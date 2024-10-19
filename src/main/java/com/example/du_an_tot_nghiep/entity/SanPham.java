package com.example.du_an_tot_nghiep.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "san_pham")
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_san_pham", nullable = false)
    private String tenSanPham;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "gia", nullable = false)
    private float gia;

    @Column(name = "hinh_anh")
    private String hinhAnh;
    @ManyToOne
    @JoinColumn(name = "mau_sac_id")
    private MauSac mauSac;
    @ManyToOne
    @JoinColumn(name = "kich_co_id")
    private KichCo kichCo;

    @Column(name = "so_luong", nullable = false)
    private int soLuong;

    @Column(name = "trang_thai")
    private String trangThai ;


    @Column(nullable = false)
    private LocalDateTime ngayTao;

    // Quan hệ Many-to-One với LoaiSanPham
    @ManyToOne
    @JoinColumn(name = "loai_san_pham_id", referencedColumnName = "id", nullable = false)
    private LoaiSanPham loaiSanPham; // Mỗi sản phẩm thuộc một loại sản phẩm

    // Quan hệ One-to-Many với ChiTietDonHang
    @OneToMany(mappedBy = "sanPham", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ChiTietDonHang> chiTietDonHangs; // Một sản phẩm có nhiều chi tiết đơn hàng

    // Quan hệ One-to-Many với SanPhamTrongGioHang
//    @OneToMany(mappedBy = "sanPham", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<SanPhamTrongGioHang> sanPhamTrongGioHangs; // Một sản phẩm có thể xuất hiện trong nhiều giỏ hàng
//
//    // Quan hệ One-to-Many với MaGiamGiaSanPham
//    @OneToMany(mappedBy = "sanPham", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<MaGiamGiaSanPham> maGiamGiaSanPhams; // Một sản phẩm có thể có nhiều mã giảm giá
}