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

    // Lấy tất cả mã giảm giá
    public List<MaGiamGia> getAllMaGiamGia() {
        return maGiamGiaRepository.findAll();
    }

    // Lấy mã giảm giá theo ID
    public Optional<MaGiamGia> getMaGiamGiaById(Long id) {
        return maGiamGiaRepository.findById(id);
    }

    // Thêm hoặc cập nhật mã giảm giá
    public MaGiamGia saveOrUpdateMaGiamGia(MaGiamGia maGiamGia) {
        if (maGiamGia.getId() != null) {
            // Nếu ID không null, đây là một bản cập nhật
            MaGiamGia existingMaGiamGia = maGiamGiaRepository.findById(maGiamGia.getId()).orElse(null);
            if (existingMaGiamGia != null) {
                // Giữ lại ngày tạo cũ
                maGiamGia.setNgayTao(existingMaGiamGia.getNgayTao());
                // Cập nhật ngày cập nhật
                maGiamGia.setNgayCapNhat(new Date()); // Cập nhật ngày hiện tại
            }
        } else {
            // Nếu ID null, đây là bản ghi mới, thiết lập ngày tạo
            maGiamGia.setNgayTao(new Date());
            maGiamGia.setNgayCapNhat(new Date());
        }
        return maGiamGiaRepository.save(maGiamGia);
    }

    // Xóa mã giảm giá
    public void deleteMaGiamGia(Long id) {
        maGiamGiaRepository.deleteById(id);
    }
}