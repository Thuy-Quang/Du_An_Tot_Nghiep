package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham,Long> {
    //List<SanPham> findAllByDeletedFalse();
    List<SanPham> findByTrangThaiNot(String trangThai);
    List<SanPham> findByLoaiSanPhamIdIn(List<Long> loaiSanPhamIds);

}
