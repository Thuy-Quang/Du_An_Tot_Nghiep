package com.example.du_an_tot_nghiep.model;

import com.example.du_an_tot_nghiep.entity.NguoiDung;
import lombok.Data;

@Data
public class NguoiDungRequest {
    private Long id;
    private String hoTen;
    private String email;
    private String diaChi;
    private String soDienThoai;

    // Constructor nhận đối tượng NguoiDung
    public NguoiDungRequest(NguoiDung nguoiDung) {
        this.id = nguoiDung.getId();
        this.hoTen = nguoiDung.getHoTen();
        this.email = nguoiDung.getEmail();
        this.diaChi = nguoiDung.getDiaChi();
        this.soDienThoai = nguoiDung.getSoDienThoai();
    }
}
