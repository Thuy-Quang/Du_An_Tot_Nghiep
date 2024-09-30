package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vai_tro_nguoi_dung")
public class VaiTroNguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nguoi_dung_id", referencedColumnName = "id", nullable = false)
    private User nguoiDung; // Đối tượng User liên kết với vai trò người dùng

    @ManyToOne
    @JoinColumn(name = "vai_tro_id", referencedColumnName = "id", nullable = false)
    private VaiTro vaiTro; // Đối tượng VaiTro liên kết với vai trò
}

