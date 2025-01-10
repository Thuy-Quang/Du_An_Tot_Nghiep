package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.GioHang;
import com.example.du_an_tot_nghiep.entity.NguoiDung;
import com.example.du_an_tot_nghiep.entity.SanPhamTrongGioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GioHangRepository extends JpaRepository< GioHang,Long> {


//    @Query("SELECT g FROM GioHang g JOIN g.sanPhamTrongGioHangs sp WHERE g.id = :gioHangId AND sp.sanPham.id = :sanPhamId")
//    Optional<GioHang> findByGioHangIdAndSanPhamId(@Param("gioHangId") Long gioHangId, @Param("sanPhamId") Long sanPhamId);
   Optional<GioHang> findByNguoiDung(NguoiDung nguoiDung);

}
