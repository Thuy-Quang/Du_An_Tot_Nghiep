package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.MaGiamGiaKhachHang;
import com.example.du_an_tot_nghiep.model.MaGiamGiaKhachHangDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaGiamGiaKhachHangRepository extends JpaRepository<MaGiamGiaKhachHang,Long> {
    // Lấy danh sách mã giảm giá theo ID người dùng
    List<MaGiamGiaKhachHang> findByNguoiDungId(Long nguoiDungId);

    // Lấy danh sách mã giảm giá đang hoạt động
    @Query("SELECT m FROM MaGiamGiaKhachHang m WHERE m.trangThai = 'Hoạt động'")
    List<MaGiamGiaKhachHang> findActiveMaGiamGia();

    // Lấy mã giảm giá theo mã giảm giá ID và người dùng ID
    Optional<MaGiamGiaKhachHang> findByMaGiamGiaIdAndNguoiDungId(Long maGiamGiaId, Long nguoiDungId);

    // Xóa mã giảm giá theo mã giảm giá ID và người dùng ID
    void deleteByMaGiamGiaIdAndNguoiDungId(Long maGiamGiaId, Long nguoiDungId);


}
