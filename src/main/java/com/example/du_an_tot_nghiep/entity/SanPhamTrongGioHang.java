package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "san_pham_trong_gio_hang")
public class SanPhamTrongGioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gio_hang_id", referencedColumnName = "id", nullable = false)
    private GioHang gioHang;

    @ManyToOne
    @JoinColumn(name = "san_pham_id", referencedColumnName = "id", nullable = false)
    private SanPham sanPham;

    @Column(name = "so_luong", nullable = false)
    private int soluong;

    public SanPhamTrongGioHang(GioHang gioHang, SanPham sanPham, int soluong) {
        this.gioHang = gioHang;
        this.sanPham = sanPham;
        this.soluong = soluong;
    }
}
