package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MauSacRepository extends JpaRepository<MauSac, Long> {
    // Các phương thức tùy chỉnh nếu cần
}
