package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.VaiTroNguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaiTroNguoiDungRepository extends JpaRepository< VaiTroNguoiDung,Long> {
    @Query("SELECT vtn FROM VaiTroNguoiDung vtn WHERE vtn.nguoiDung.hoTen LIKE %?1% OR vtn.vaiTro.tenVaiTro LIKE %?1%")
    Page<VaiTroNguoiDung> searchByNguoiDungOrVaiTro(String search, Pageable pageable);

    @Query(value = "SELECT vt.ten_vai_tro " +
            "FROM nguoi_dung nd " +
            "JOIN vai_tro_nguoi_dung vtn ON nd.id = vtn.nguoi_dung_id " +
            "JOIN vai_tro vt ON vtn.vai_tro_id = vt.id " +
            "WHERE nd.ten_dang_nhap = :tenDangNhap", nativeQuery = true)
    List<String> findRolesByUsername(@Param("tenDangNhap") String tenDangNhap);
}
