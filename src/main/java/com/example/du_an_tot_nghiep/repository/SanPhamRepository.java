package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham,Long> {
    // Lấy danh sách sản phẩm theo loại sản phẩm
    List<SanPham> findByLoaiSanPhamIdIn(List<Long> loaiSanPhamIds);

    // Tìm kiếm sản phẩm theo tên
//    Page<SanPham> findByTenSanPhamContaining(String keyword, Pageable pageable);

    // Lấy tất cả sản phẩm, sắp xếp theo ngày tạo giảm dần (phân trang)
//    Page<SanPham> findAllByOrderByNgayTaoDesc(Pageable pageable);
//
//    // Tìm kiếm sản phẩm theo tên, sắp xếp theo ngày tạo giảm dần (phân trang)
//    Page<SanPham> findByTenSanPhamContainingOrderByNgayTaoDesc(String keyword, Pageable pageable);
    // Phương thức tìm sản phẩm theo khoảng giá
    List<SanPham> findByGiaBetween(int minPrice, int maxPrice);

    // Phương thức tìm sản phẩm có giá lớn hơn hoặc bằng một giá trị
    List<SanPham> findByGiaGreaterThanEqual(int price);

    // Phương thức tìm sản phẩm theo khoảng giá và tên sản phẩm chứa từ khóa
    List<SanPham> findByGiaBetweenAndTenSanPhamContaining(int minPrice, int maxPrice, String keyword);

    // Phương thức tìm sản phẩm có giá lớn hơn hoặc bằng và tên sản phẩm chứa từ khóa
    List<SanPham> findByGiaGreaterThanEqualAndTenSanPhamContaining(int price, String keyword);

    // Phương thức tìm tất cả sản phẩm theo từ khóa (dùng cho tìm kiếm)


    // Phương thức tìm tất cả sản phẩm, sắp xếp theo ngày tạo
    Page<SanPham> findByTenSanPhamContaining(String tenSanPham, Pageable pageable);

    // Tìm kiếm theo tên và sắp xếp theo ngày tạo
    Page<SanPham> findByTenSanPhamContainingOrderByNgayTaoDesc(String tenSanPham, Pageable pageable);



    Page<SanPham> findAllByOrderByNgayTaoDesc(Pageable pageable);
    @Query("SELECT s.tenSanPham, SUM(ctdh.soLuong) " +
            "FROM ChiTietDonHang ctdh " +
            "JOIN ctdh.sanPhamChiTiet spct " +
            "JOIN spct.sanPham s " +
            "WHERE ctdh.donHang.ngayTao BETWEEN :startOfMonth AND :endOfMonth " +
            "GROUP BY s.tenSanPham")
    List<Object[]> countSanPhamBanDuocTrongThang(
            @Param("startOfMonth") LocalDateTime startOfMonth,
            @Param("endOfMonth") LocalDateTime endOfMonth);

}
