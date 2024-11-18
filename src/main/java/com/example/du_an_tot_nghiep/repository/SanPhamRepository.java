package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham,Long> {
    // Lấy danh sách sản phẩm theo loại sản phẩm
    List<SanPham> findByLoaiSanPhamIdIn(List<Long> loaiSanPhamIds);

    // Tìm kiếm sản phẩm theo tên
    Page<SanPham> findByTenSanPhamContaining(String keyword, Pageable pageable);

    // Lấy tất cả sản phẩm, sắp xếp theo ngày tạo giảm dần (phân trang)
    Page<SanPham> findAllByOrderByNgayTaoDesc(Pageable pageable);

    // Tìm kiếm sản phẩm theo tên, sắp xếp theo ngày tạo giảm dần (phân trang)
    Page<SanPham> findByTenSanPhamContainingOrderByNgayTaoDesc(String keyword, Pageable pageable);

}
