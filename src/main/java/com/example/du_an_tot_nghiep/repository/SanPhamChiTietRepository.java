package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.KichCo;
import com.example.du_an_tot_nghiep.entity.MauSac;
import com.example.du_an_tot_nghiep.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Long> {
    Optional<SanPhamChiTiet> findBySanPhamId(Long sanPhamId);

    @Query("SELECT DISTINCT spct.mauSac FROM SanPhamChiTiet spct WHERE spct.sanPham.id = :sanPhamId")
    List<MauSac> findDistinctMauSacBySanPhamId(@Param("sanPhamId") Long sanPhamId);

    @Query("SELECT DISTINCT spct.kichCo FROM SanPhamChiTiet spct WHERE spct.sanPham.id = :sanPhamId")
    List<KichCo> findDistinctKichCoBySanPhamId(@Param("sanPhamId") Long sanPhamId);

    // Tạo phương thức tìm kiếm sản phẩm chi tiết theo SanPhamId, MauSacId và KichCoId
    Optional<SanPhamChiTiet> findBySanPhamIdAndMauSacIdAndKichCoId(Long sanPhamId, Long mauSacId, Long kichCoId);

    @Query("SELECT sp FROM SanPhamChiTiet sp WHERE " +
            "sp.sanPham.tenSanPham LIKE %:searchTerm% OR " +
            "sp.mauSac.tenMau LIKE %:searchTerm% OR " +
            "sp.kichCo.tenKichCo LIKE %:searchTerm%")
    Page<SanPhamChiTiet> findBySearchTerm(String searchTerm, Pageable pageable);

    @Query("SELECT kc FROM SanPhamChiTiet spct " +
            "JOIN spct.kichCo kc " +
            "WHERE spct.sanPham.id = :sanPhamId AND spct.mauSac.id = :mauSacId")
    List<KichCo> findKichCoBySanPhamIdAndMauSacId(@Param("sanPhamId") Long sanPhamId,
                                                  @Param("mauSacId") Long mauSacId);
}
