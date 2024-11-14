package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.MaGiamGiaSanPham;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaGiamGiaSanPhamRepository extends JpaRepository<MaGiamGiaSanPham,Long> {
    List<MaGiamGiaSanPham> findByTrangThai(String trangThai);

}
