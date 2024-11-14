package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.MaGiamGiaKH;
import com.example.du_an_tot_nghiep.repository.MaGiamGiaKhachHangRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MaGiamGiaKHService {

    @Autowired
    private MaGiamGiaKhachHangRepository maGiamGiaKhachHangRepository;

    // Lấy tất cả mã giảm giá khách hàng
    public List<MaGiamGiaKH> findAll() {
        log.info("Lấy tất cả mã giảm giá khách hàng");
        return maGiamGiaKhachHangRepository.findAll();
    }

    // Tìm kiếm mã giảm giá khách hàng theo ID
    public Optional<MaGiamGiaKH> findById(Long id) {
        log.info("Tìm mã giảm giá khách hàng với ID: {}", id);
        return maGiamGiaKhachHangRepository.findById(id);
    }

    // Lưu hoặc cập nhật mã giảm giá khách hàng
    public MaGiamGiaKH save(MaGiamGiaKH maGiamGiaKH) {
        log.info("Lưu mã giảm giá khách hàng: {}", maGiamGiaKH);
        return maGiamGiaKhachHangRepository.save(maGiamGiaKH);
    }

    // Xóa mã giảm giá khách hàng theo ID
    public void deleteById(Long id) {
        log.info("Xóa mã giảm giá khách hàng với ID: {}", id);
        maGiamGiaKhachHangRepository.deleteById(id);
    }

    // Lấy danh sách mã giảm giá theo ID người dùng
    public List<MaGiamGiaKH> findByNguoiDungId(Long nguoiDungId) {
        log.info("Lấy danh sách mã giảm giá với người dùng ID: {}", nguoiDungId);
        return maGiamGiaKhachHangRepository.findByNguoiDungId(nguoiDungId);
    }

    // Lấy danh sách mã giảm giá đang hoạt động
    public List<MaGiamGiaKH> findActiveMaGiamGia() {
        log.info("Lấy danh sách mã giảm giá đang hoạt động");
        return maGiamGiaKhachHangRepository.findActiveMaGiamGia();
    }

    // Tìm kiếm mã giảm giá theo mã giảm giá ID và người dùng ID
    public Optional<MaGiamGiaKH> findByMaGiamGiaIdAndNguoiDungId(Long maGiamGiaId, Long nguoiDungId) {
        log.info("Tìm mã giảm giá với maGiamGiaId: {} và nguoiDungId: {}", maGiamGiaId, nguoiDungId);
        return maGiamGiaKhachHangRepository.findByMaGiamGiaIdAndNguoiDungId(maGiamGiaId, nguoiDungId);
    }

    // Xóa mã giảm giá theo mã giảm giá ID và người dùng ID
    public void deleteByMaGiamGiaIdAndNguoiDungId(Long maGiamGiaId, Long nguoiDungId) {
        log.info("Xóa mã giảm giá với maGiamGiaId: {} và nguoiDungId: {}", maGiamGiaId, nguoiDungId);
        maGiamGiaKhachHangRepository.deleteByMaGiamGiaIdAndNguoiDungId(maGiamGiaId, nguoiDungId);
    }
}
