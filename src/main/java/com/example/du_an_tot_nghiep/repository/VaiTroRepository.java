package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro,Long> {
    boolean existsByTenVaiTro(String tenVaiTro);
}
