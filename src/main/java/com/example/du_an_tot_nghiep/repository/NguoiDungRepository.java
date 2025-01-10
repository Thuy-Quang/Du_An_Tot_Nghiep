package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.DonHang;
import com.example.du_an_tot_nghiep.entity.NguoiDung;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung,Long> {

    Optional<NguoiDung> findByEmail(String email);
    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);

    boolean existsByTenDangNhap(String tenDangNhap);

    boolean existsByEmail(String email);

    boolean existsBySoDienThoai(String soDienThoai);
    // Đếm người dùng mới trong khoảng thời gian
    // Đếm người dùng mới trong khoảng thời gian
    @Query("SELECT COUNT(u) FROM NguoiDung u WHERE u.ngayTao BETWEEN :startDate AND :endDate")
    Long demNguoiDungMoi(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    Long countByNgayTaoBetween(Date startDate, Date endDate);  // Đếm số n
}
