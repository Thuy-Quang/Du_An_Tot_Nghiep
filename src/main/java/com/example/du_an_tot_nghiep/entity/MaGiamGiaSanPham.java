package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ma_giam_gia_san_pham")
public class MaGiamGiaSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "san_pham_chi_tiet_id", nullable = false)
    private SanPhamChiTiet sanPhamChiTiet;

    @ManyToOne
    @JoinColumn(name = "ma_giam_gia_id", nullable = false)
    private MaGiamGia maGiamGia;

    @Column(name = "ngay_ap_dung", nullable = false)
    private LocalDateTime ngayApDung;

    @Column(name = "trang_thai", nullable = false)
    private String trangThai;

    // Getters and setters
}
