package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.MaGiamGia;
import com.example.du_an_tot_nghiep.repository.MaGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MaGiamGiaService {

    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;
    public List<MaGiamGia> findAll() {
        return maGiamGiaRepository.findAll();
    }

    // Lấy thông tin mã giảm giá theo ID
    public MaGiamGia findById(Long id) {
        return maGiamGiaRepository.findById(id).orElse(null);
    }

    // Lấy tất cả mã giảm giá
    public List<MaGiamGia> getAllMaGiamGia() {
        return maGiamGiaRepository.findAll();
    }

    // Lấy mã giảm giá theo ID
    public Optional<MaGiamGia> getMaGiamGiaById(Long id) {
        return maGiamGiaRepository.findById(id);
    }

    // Thêm hoặc cập nhật mã giảm giá
//    public MaGiamGia saveOrUpdateMaGiamGia(MaGiamGia maGiamGia) {
//        if (maGiamGia.getId() != null) {
//            // Nếu ID không null, đây là một bản cập nhật
//            MaGiamGia existingMaGiamGia = maGiamGiaRepository.findById(maGiamGia.getId()).orElse(null);
//            if (existingMaGiamGia != null) {
//                // Giữ lại ngày tạo cũ
//                maGiamGia.setNgayTao(existingMaGiamGia.getNgayTao());
//                // Cập nhật ngày cập nhật
//                maGiamGia.setNgayCapNhat(new Date()); // Cập nhật ngày hiện tại
//            }
//        } else {
//            // Nếu ID null, đây là bản ghi mới, thiết lập ngày tạo
//            maGiamGia.setNgayTao(new Date());
//            maGiamGia.setNgayCapNhat(new Date());
//        }
//        return maGiamGiaRepository.save(maGiamGia);
//    }

    // Xóa mã giảm giá
    public void deleteMaGiamGia(Long id) {
        maGiamGiaRepository.deleteById(id);
    }
//    public float apDungMaGiamGia(Long maGiamGiaId, float tongTien) {
//        MaGiamGia maGiamGia = maGiamGiaRepository.findById(maGiamGiaId)
//                .orElseThrow(() -> new RuntimeException("Mã giảm giá không hợp lệ"));
//
//        if (new Date().before(maGiamGia.getNgayBatDau()) || new Date().after(maGiamGia.getNgayHetHan())) {
//            throw new RuntimeException("Mã giảm giá đã hết hạn");
//        }
//
//        return tongTien - (tongTien * maGiamGia.getPhanTramGiam() / 100);
//    }

}