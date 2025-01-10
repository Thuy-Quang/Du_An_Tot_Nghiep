package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.DonHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang,Long> {
    List<DonHang> findAll();
    List<DonHang> findByNguoiDungId(Long nguoiDungId);

    // sắp xếp theo ngày tạo
    Page<DonHang> findAllByOrderByNgayTaoDesc(Pageable pageable);
    @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE d.trangThai = 'Hoàn Thành'")
    Double tinhTongDoanhThu();

    // Đếm số đơn hàng trong khoảng thời gian
    @Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayTao BETWEEN :startDate AND :endDate")
    Long demDonHang(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Tính tổng doanh thu trong khoảng thời gian
    @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE d.ngayTao BETWEEN :startDate AND :endDate")
    Long tongDoanhThu(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayTao >= :startDate AND d.ngayTao < :endDate")
    Long countDonHangHomNay(Date startDate, Date endDate);

    // Tính tổng doanh thu hôm nay
    @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE d.ngayTao >= :startDate AND d.ngayTao < :endDate")
    Long sumDoanhThuHomNay(Date startDate, Date endDate);
    Long countByNgayTaoBetween(Date startDate, Date endDate);  // Đếm số đơn hàng trong khoảng thời gian

    @Query("SELECT SUM(dh.tongTien) FROM DonHang dh WHERE dh.ngayTao BETWEEN :startDate AND :endDate")
    Double sumTongTienBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
