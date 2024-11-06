package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.GioHang;
import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.entity.SanPhamTrongGioHang;
import com.example.du_an_tot_nghiep.repository.GioHangRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamTrongGioHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GioHangService {
    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private SanPhamTrongGioHangRepository sanPhamTrongGioHangRepository;

    public GioHang layGioHangTheoNguoiDungId(Long nguoiDungId) {
        return gioHangRepository.findByNguoiDungId(nguoiDungId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại cho người dùng với ID: " + nguoiDungId));
    }

    public void themSanPhamVaoGio(Long nguoiDungId, Long sanPhamId, int soLuong) {
        GioHang gioHang = layGioHangTheoNguoiDungId(nguoiDungId);

        SanPham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        SanPhamTrongGioHang sanPhamTrongGio = sanPhamTrongGioHangRepository
                .findByGioHangIdAndSanPhamId(gioHang.getId(), sanPhamId)
                .orElseGet(() -> new SanPhamTrongGioHang(gioHang, sanPham, soLuong));

        sanPhamTrongGio.setSoluong(sanPhamTrongGio.getSoluong() + soLuong);
        sanPhamTrongGioHangRepository.save(sanPhamTrongGio);
    }
}
