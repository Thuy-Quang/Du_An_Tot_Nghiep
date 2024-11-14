package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.MaGiamGiaSanPham;
import com.example.du_an_tot_nghiep.repository.MaGiamGiaSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaGiamGiaSanPhamService {

    @Autowired
    private MaGiamGiaSanPhamRepository maGiamGiaSanPhamRepository;

    // Lấy tất cả các mã giảm giá sản phẩm
    public List<MaGiamGiaSanPham> getAllMaGiamGiaSanPham() {
        return maGiamGiaSanPhamRepository.findAll();
    }

    // Tìm mã giảm giá sản phẩm theo ID
    public Optional<MaGiamGiaSanPham> findById(Long id) {
        return maGiamGiaSanPhamRepository.findById(id);
    }

    // Lưu hoặc cập nhật mã giảm giá sản phẩm
    public MaGiamGiaSanPham save(MaGiamGiaSanPham maGiamGiaSanPham) {
        return maGiamGiaSanPhamRepository.save(maGiamGiaSanPham);
    }

    // Xóa mã giảm giá sản phẩm theo ID
    public void deleteById(Long id) {
        maGiamGiaSanPhamRepository.deleteById(id);
    }
}
