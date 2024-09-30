package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GioHangRepository extends JpaRepository<Integer, GioHang> {
}
