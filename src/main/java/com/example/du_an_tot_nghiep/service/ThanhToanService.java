package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.DonHang;
import com.example.du_an_tot_nghiep.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ThanhToanService {
    @Autowired
    private DonHangRepository donHangRepository;

    public void thanhToanDonHang(Long donHangId) {
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        donHang.setTrangThai("Đã thanh toán");
        donHang.setTrangThaiThanhToan("Hoàn tất");
        donHang.setNgayCapNhat(new Date());

        donHangRepository.save(donHang);
    }
}
