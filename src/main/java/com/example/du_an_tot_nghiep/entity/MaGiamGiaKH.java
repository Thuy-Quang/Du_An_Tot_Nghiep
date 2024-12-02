package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "ma_giam_gia_khach_hang")
public class MaGiamGiaKH {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id", referencedColumnName = "id", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "ma_giam_gia_id", referencedColumnName = "id", nullable = false)
    private MaGiamGia maGiamGia;

    @Column(name = "ngay_ap_dung")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayApDung;

    @Column(name = "ngay_het_han")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayHetHan;

    @Column(name = "ngay_cap_nhat")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayCapNhat;

    @Column(name = "trang_thai")
    private String trangThai;

    @PrePersist
    @PreUpdate
    public void preSaveOrUpdate() {
        this.ngayCapNhat = LocalDate.now(); // Tự động cập nhật ngày hiện tại khi lưu hoặc cập nhật
    }
}
