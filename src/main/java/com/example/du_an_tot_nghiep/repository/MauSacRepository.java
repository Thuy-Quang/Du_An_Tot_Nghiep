package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MauSacRepository extends JpaRepository<MauSac, Long> {
    // Các phương thức tùy chỉnh nếu cần


}
