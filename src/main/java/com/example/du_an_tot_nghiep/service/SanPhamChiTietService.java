package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.KichCo;
import com.example.du_an_tot_nghiep.entity.MauSac;
import com.example.du_an_tot_nghiep.entity.SanPhamChiTiet;
import com.example.du_an_tot_nghiep.repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;


    public SanPhamChiTiet getSanPhamChiTietBySanPhamId(Long id) {
        return sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm chi tiết với id: " + id));
    }
    public List<MauSac> getMauSacBySanPhamId(Long sanPhamId) {
        return sanPhamChiTietRepository.findDistinctMauSacBySanPhamId(sanPhamId);
    }

    public List<KichCo> getKichCoBySanPhamId(Long sanPhamId) {
        return sanPhamChiTietRepository.findDistinctKichCoBySanPhamId(sanPhamId);
    }

    public List<KichCo> getKichCoByMauSac(Long sanPhamId, Long mauSacId) {
        return sanPhamChiTietRepository.findKichCoBySanPhamIdAndMauSacId(sanPhamId, mauSacId);
    }


    public SanPhamChiTiet save(SanPhamChiTiet sanPhamChiTiet) {
        return sanPhamChiTietRepository.save(sanPhamChiTiet);
    }
}
