package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
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




}
