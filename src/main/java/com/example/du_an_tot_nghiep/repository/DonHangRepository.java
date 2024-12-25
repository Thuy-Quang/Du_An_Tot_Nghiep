package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.DonHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang,Long> {
    List<DonHang> findAll();
    List<DonHang> findByNguoiDungId(Long nguoiDungId);

    // sắp xếp theo ngày tạo
    Page<DonHang> findAllByOrderByNgayTaoDesc(Pageable pageable);

}
