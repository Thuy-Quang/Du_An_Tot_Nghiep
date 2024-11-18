package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KichCoRepository extends JpaRepository<KichCo, Long> {
    // Các phương thức tùy chỉnh nếu cần
}
