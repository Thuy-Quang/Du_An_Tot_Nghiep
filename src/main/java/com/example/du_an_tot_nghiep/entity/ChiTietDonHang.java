package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chi_tiet_don_hang")
public class ChiTietDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "don_hang_id", referencedColumnName = "id", nullable = false)
    private DonHang donHang; // Đối tượng DonHang liên kết với chi tiết đơn hàng

    @ManyToOne
    @JoinColumn(name = "san_pham_id", referencedColumnName = "id", nullable = false)
    private SanPham sanPham; // Đối tượng SanPham liên kết với chi tiết đơn hàng

    @Column(name = "so_luong", nullable = false)
    private int soLuong;

    @Column(name = "gia_don_vi", nullable = false)
    private float giaDonVi;

    // Computed column for total price per product
    public float getTongGia() {
        return soLuong * giaDonVi;
    }
}
