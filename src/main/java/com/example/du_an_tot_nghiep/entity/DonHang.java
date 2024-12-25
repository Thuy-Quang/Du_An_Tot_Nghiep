package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "don_hang")
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id", referencedColumnName = "id", nullable = false)
    private NguoiDung nguoiDung; // Đối tượng User liên kết với đơn hàng

    @Column(name = "trang_thai", nullable = false)
    private String trangThai;

    @Column(name = "tong_tien", nullable = false)
    private Double tongTien;

    @Column(name = "phuong_thuc_thanh_toan", nullable = false)
    private String phuongThucThanhToan;

    @Column(name = "ngay_tao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @Column(name = "trang_thai_thanh_toan")
    private String trangThaiThanhToan;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietDonHang> chiTietDonHangs;

    // Hàm này sẽ được gọi trước khi lưu đối tượng vào database
    @PrePersist
    protected void onCreate() {
        if (chiTietDonHangs != null && !chiTietDonHangs.isEmpty()) {
            tongTien = chiTietDonHangs.stream()
                    .mapToDouble(ChiTietDonHang::getTongGia)
                    .sum();
        } else {
            // Nếu không có chi tiết, tổng tiền là 0
            tongTien = 0.0;
        }

        if (trangThai == null) {
            trangThai = "Chưa xác nhận";
        }
        if (trangThaiThanhToan == null) {
            trangThaiThanhToan = "Chưa thanh toán";
        }
        if (ngayTao == null) {
            ngayTao = new Date();
        }
    }

    // Constructor chỉ nhận ID
    public DonHang(Long id) {
        this.id = id;
    }
}
