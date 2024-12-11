package com.example.du_an_tot_nghiep.model;

import lombok.Data;

import java.util.Date;

@Data
public class ChiTietSanPhamDTO {

    private Long id; // ID của sản phẩm chi tiết
    private String tenSanPham; // Tên sản phẩm
    private Float gia; // Giá sản phẩm
    private String hinhAnh; // Hình ảnh của sản phẩm
    private String mauSac; // Màu sắc của sản phẩm
    private String kichCo; // Kích cỡ của sản phẩm
    private Integer soLuong; // Số lượng còn lại của sản phẩm
    private String trangThai; // Trạng thái của sản phẩm (ví dụ: "Còn hàng", "Hết hàng")
    private String moTa; // Mô tả chi tiết sản phẩm
    private Date ngayTao; // Ngày tạo sản phẩm chi tiết
    private Date ngayCapNhat; // Ngày cập nhật sản phẩm chi tiết

}