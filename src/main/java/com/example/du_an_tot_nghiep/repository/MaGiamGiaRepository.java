package com.example.du_an_tot_nghiep.repository;

import com.example.du_an_tot_nghiep.entity.MaGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaGiamGiaRepository extends JpaRepository<MaGiamGia,Long> {
    Page<MaGiamGia> findByMaContainingIgnoreCase(String search, Pageable pageable);

}
