    package com.example.du_an_tot_nghiep.entity;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.util.Date;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "ma_giam_gia")
    public class MaGiamGia {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "ma", nullable = false)
        private String ma;

        @Column(name = "phan_tram_giam", nullable = false)
        private float phanTramGiam;

        @Column(name = "ngay_bat_dau", nullable = false)
        private Date ngayBatDau;

        @Column(name = "ngay_het_han", nullable = false)
        private Date ngayHetHan;

        @Column(name = "trang_thai", nullable = false)
        private String trangThai;

        @Column(name = "ngay_tao", nullable = false)
        private Date ngayTao;

        @Column(name = "ngay_cap_nhat", nullable = false)
        private Date ngayCapNhat;
    }
