package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "kich_co")
public class KichCo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_kich_co", nullable = false)
    private String tenKichCo;

    @Column(name = "trang_thai", nullable = false)
    private String trangThai = "Còn Hàng"; // Mặc định là Còn Hàng

    @Column(name = "ngay_tao", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    // Getters và Setters
}
