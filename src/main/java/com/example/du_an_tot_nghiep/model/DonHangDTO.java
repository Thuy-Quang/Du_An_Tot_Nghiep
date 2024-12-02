package com.example.du_an_tot_nghiep.model;

import com.example.du_an_tot_nghiep.entity.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Data
public class DonHangDTO {
    private Long id;
    private NguoiDung nguoiDung; // Đối tượng User liên kết với đơn hàng
    private String trangThai;
    private Double tongTien;
    private String phuongThucThanhToan;
    private Date ngayTao;
    private Date ngayCapNhat;
    private String trangThaiThanhToan;

    public DonHangDTO(Long id, Long id1, String hoTen, String diaChi) {
    }
}
