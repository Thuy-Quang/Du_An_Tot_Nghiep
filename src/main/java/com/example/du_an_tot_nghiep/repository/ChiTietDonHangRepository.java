package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.ChiTietDonHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Long> {

    // Lấy danh sách tất cả chi tiết đơn hàng của một đơn hàng dựa vào ID đơn hàng
    List<ChiTietDonHang> findByDonHangId(Long donHangId);

    // Lấy chi tiết đơn hàng cụ thể dựa vào ID đơn hàng và ID sản phẩm chi tiết
    Optional<ChiTietDonHang> findByDonHangIdAndSanPhamChiTietId(Long donHangId, Long sanPhamChiTietId);

    // Lấy danh sách chi tiết đơn hàng của một đơn hàng cụ thể (có hỗ trợ phân trang)
    Page<ChiTietDonHang> findByDonHangId(Long donHangId, Pageable pageable);

    // Truy vấn tất cả chi tiết đơn hàng kèm theo thông tin đơn hàng và người dùng (có hỗ trợ phân trang)
    @Query("SELECT c FROM ChiTietDonHang c JOIN FETCH c.donHang d JOIN FETCH d.nguoiDung")
    Page<ChiTietDonHang> findAll(Pageable pageable);

    Page<ChiTietDonHang> findBySanPham_TenSanPhamContaining(String search, Pageable pageable);
    void deleteById(Long id);
}
