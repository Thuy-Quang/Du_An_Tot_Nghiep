package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.VaiTroNguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VaiTroNguoiDungRepository extends JpaRepository< VaiTroNguoiDung,Long> {
    @Query("SELECT vtn FROM VaiTroNguoiDung vtn WHERE vtn.nguoiDung.hoTen LIKE %?1% OR vtn.vaiTro.tenVaiTro LIKE %?1%")
    Page<VaiTroNguoiDung> searchByNguoiDungOrVaiTro(String search, Pageable pageable);
}
