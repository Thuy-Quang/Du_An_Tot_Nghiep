package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.entity.VaiTroNguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaiTroNguoiDungRepository extends JpaRepository<Integer, VaiTroNguoiDung> {
}
