package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.DonHang;
import com.example.du_an_tot_nghiep.entity.NguoiDung;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung,Long> {

}
