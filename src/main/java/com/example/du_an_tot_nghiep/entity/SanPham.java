package com.example.du_an_tot_nghiep.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "san_pham")
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 6, max = 30, message = "Tên sản phẩm phải từ 6 đến 30 ký tự")
    @Column(name = "ten_san_pham", nullable = false)
    private String tenSanPham;

    @Size(max = 500, message = "Mô tả không được dài quá 500 ký tự")
    @Column(name = "mo_ta")
    private String moTa;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "101.0", message = "Giá phải lớn hơn 100")
    @Column(name = "gia", nullable = false)
    private Float gia;


    @NotBlank(message = "Hình ảnh không được để trống")
    @Pattern(regexp = ".*\\.(jpg|jpeg|png)$", message = "Hình ảnh phải có định dạng .jpg, .jpeg, hoặc .png")
    @Column(name = "hinh_anh", nullable = false)
    private String hinhAnh;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    @Max(value = 99, message = "Số lượng phải nhỏ hơn 100")
    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(nullable = false)
    private LocalDate ngayTao;

    @NotNull(message = "ID loại sản phẩm không được để trống")
    @ManyToOne
    @JoinColumn(name = "loai_san_pham_id", referencedColumnName = "id", nullable = false)
    private LoaiSanPham loaiSanPham;

    @NotNull(message = "ID kích cỡ không được để trống")
    @ManyToOne
    @JoinColumn(name = "kich_co_id", referencedColumnName = "id", nullable = false)
    private KichCo kichCo;

    @NotNull(message = "ID màu sắc không được để trống")
    @ManyToOne
    @JoinColumn(name = "mau_sac_id", referencedColumnName = "id", nullable = false)
    private MauSac mauSac;


//    @OneToMany(mappedBy = "sanPham", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<ChiTietDonHang> chiTietDonHangs; // Một sản phẩm có nhiều chi tiết đơn hàng
//
//    @OneToMany(mappedBy = "sanPham", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<MaGiamGiaSanPham> maGiamGiaSanPhams; // Một sản phẩm có thể có nhiều mã giảm giá
}