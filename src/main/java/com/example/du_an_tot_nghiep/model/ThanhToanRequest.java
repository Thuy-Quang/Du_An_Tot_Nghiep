package com.example.du_an_tot_nghiep.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThanhToanRequest {
    private double tongTien;

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}
