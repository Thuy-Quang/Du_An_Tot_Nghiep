package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.ChiTietDonHang;
import com.example.du_an_tot_nghiep.repository.ChiTietDonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChiTietDonHangService {
    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;

    // Lấy tất cả các chi tiết đơn hàng
    public List<ChiTietDonHang> getAll() {
        return chiTietDonHangRepository.findAll();
    }

    // Lấy chi tiết đơn hàng theo ID
    public Optional<ChiTietDonHang> getById(Long id) {
        return chiTietDonHangRepository.findById(id);
    }

    // Thêm mới chi tiết đơn hàng
    public ChiTietDonHang add(ChiTietDonHang chiTietDonHang) {
        return chiTietDonHangRepository.save(chiTietDonHang);
    }

    // Cập nhật chi tiết đơn hàng
    public ChiTietDonHang update(ChiTietDonHang chiTietDonHang) {
        return chiTietDonHangRepository.save(chiTietDonHang);
    }

    // Xóa chi tiết đơn hàng
    public void delete(Long id) {
        chiTietDonHangRepository.deleteById(id);
    }

}
