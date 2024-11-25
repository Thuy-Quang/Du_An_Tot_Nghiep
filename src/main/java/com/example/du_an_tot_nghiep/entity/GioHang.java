package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gio_hang")
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id", referencedColumnName = "id", nullable = false)
    private NguoiDung nguoiDung; // Đối tượng NguoiDung liên kết với giỏ hàng

    @Column(name = "ngay_tao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "ngay_cap_nhat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @OneToMany(mappedBy = "gioHang", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SanPhamTrongGioHang> sanPhamTrongGioHangs; // Danh sách sản phẩm trong giỏ hàng

    // Constructor
    public GioHang(Date ngayTao) {
        this.ngayTao = ngayTao;
    }


    public GioHang(Date date, NguoiDung nguoiDung) {
        this.ngayTao = ngayTao;
        this.nguoiDung = nguoiDung;
    }
}