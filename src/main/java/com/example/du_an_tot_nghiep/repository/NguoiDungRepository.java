package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.NguoiDung;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NguoiDungRepository extends JpaRepository<Integer, NguoiDung> {
}
