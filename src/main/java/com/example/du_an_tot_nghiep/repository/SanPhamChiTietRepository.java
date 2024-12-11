package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.KichCo;
import com.example.du_an_tot_nghiep.entity.MauSac;
import com.example.du_an_tot_nghiep.entity.SanPhamChiTiet;
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
}
