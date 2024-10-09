package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.LoaiSanPham;
import com.example.du_an_tot_nghiep.repository.LoaiSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoaiSanPhamService {

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    // Lấy tất cả loại sản phẩm
    public List<LoaiSanPham> getAllLoaiSanPham() {
        return loaiSanPhamRepository.findAll();
    }
    public Optional<LoaiSanPham> getLoaiSanPhamById(Long id) {
        return loaiSanPhamRepository.findById(id);
    }

    public LoaiSanPham saveOrUpdateLoaiSanPham(LoaiSanPham loaiSanPham) {
        if (loaiSanPham.getId() != null) {
            // Nếu ID không null, có nghĩa là đây là một bản cập nhật
            LoaiSanPham existingLoaiSanPham = loaiSanPhamRepository.findById(loaiSanPham.getId()).orElse(null);
            if (existingLoaiSanPham != null) {
                // Giữ lại ngày tạo cũ
                loaiSanPham.setNgayTao(existingLoaiSanPham.getNgayTao());
                // Cập nhật ngày cập nhật
                loaiSanPham.setNgayCapNhat(new Date()); // Cập nhật ngày hiện tại
            }
        } else {
            // Nếu ID null, đây là bản ghi mới, thiết lập ngày tạo
            loaiSanPham.setNgayTao(new Date());
            loaiSanPham.setNgayCapNhat(new Date());
        }
        return loaiSanPhamRepository.save(loaiSanPham);
    }

    // Xóa loại sản phẩm
    public void deleteLoaiSanPham(Long id) {
        loaiSanPhamRepository.deleteById(id);
    }
}
