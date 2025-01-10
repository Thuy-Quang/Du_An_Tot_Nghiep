package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ma_giam_gia_khach_hang")
public class MaGiamGiaKhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "ma_giam_gia_id", nullable = false)
    private MaGiamGia maGiamGia;

    @Column(name = "ngay_ap_dung", nullable = false)
    private LocalDateTime ngayApDung;

    @Column(name = "trang_thai", nullable = false)
    private String trangThai;

    // Getters and setters
}