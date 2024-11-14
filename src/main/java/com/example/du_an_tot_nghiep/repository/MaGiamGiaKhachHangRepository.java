package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.MaGiamGiaKH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaGiamGiaKhachHangRepository extends JpaRepository<MaGiamGiaKH,Long> {
    // Lấy danh sách mã giảm giá theo ID người dùng
    List<MaGiamGiaKH> findByNguoiDungId(Long nguoiDungId);

    // Lấy danh sách mã giảm giá đang hoạt động
    @Query("SELECT m FROM MaGiamGiaKH m WHERE m.trangThai = 'Hoạt động'")
    List<MaGiamGiaKH> findActiveMaGiamGia();

    // Lấy mã giảm giá theo mã giảm giá ID và người dùng ID
    Optional<MaGiamGiaKH> findByMaGiamGiaIdAndNguoiDungId(Long maGiamGiaId, Long nguoiDungId);

    // Xóa mã giảm giá theo mã giảm giá ID và người dùng ID
    void deleteByMaGiamGiaIdAndNguoiDungId(Long maGiamGiaId, Long nguoiDungId);
}
