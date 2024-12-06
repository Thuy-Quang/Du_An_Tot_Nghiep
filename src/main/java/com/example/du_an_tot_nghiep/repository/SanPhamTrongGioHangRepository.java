package com.example.du_an_tot_nghiep.repository;


import com.example.du_an_tot_nghiep.entity.SanPhamTrongGioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SanPhamTrongGioHangRepository extends JpaRepository< SanPhamTrongGioHang,Long> {
//    Optional<SanPhamTrongGioHang> findByGioHangIdAndSanPhamId(Long gioHangId, Long sanPhamId);
}

