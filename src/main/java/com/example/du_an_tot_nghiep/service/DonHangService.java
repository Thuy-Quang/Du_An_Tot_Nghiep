package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.*;
import com.example.du_an_tot_nghiep.model.DonHangRequest;
import com.example.du_an_tot_nghiep.repository.ChiTietDonHangRepository;
import com.example.du_an_tot_nghiep.repository.DonHangRepository;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DonHangService {
    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;
    @Autowired
    private GioHangService gioHangService;
    @Autowired
    private DonHangRepository donHangRepository;
    @Autowired
    private MaGiamGiaService maGiamGiaService;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    public DonHangService(NguoiDungRepository nguoiDungRepository, DonHangRepository donHangRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.donHangRepository = donHangRepository;
    }
    // Lấy tất cả đơn hàng
//    public List<DonHang> getAll() {
//        return donHangRepository.findAll();
//    }
//
//    // L
//    public Page<DonHang> getAll(Pageable pageable) {
//        return donHangRepository.findAll(pageable);
//    }

    public DonHang addDonHang(DonHangRequest donHangRequest) {
        DonHang donHang = new DonHang();
        Date now = new Date(); // Thiết lập thời gian hiện tại

        // Thiết lập các trường cho đối tượng DonHang
        donHang.setNgayTao(now);
        donHang.setTrangThai(donHangRequest.getTrangThai());
        donHang.setTongTien(donHangRequest.getTongTien());
        donHang.setPhuongThucThanhToan(donHangRequest.getPhuongThucThanhToan());
        donHang.setNgayCapNhat(now);

        // Lấy người dùng từ repository và thiết lập cho đơn hàng
        NguoiDung nguoiDung = nguoiDungRepository.findById(donHangRequest.getNguoiDungId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        donHang.setNguoiDung(nguoiDung);

        // Lưu đơn hàng vào database
        return donHangRepository.save(donHang);
    }


    // Cập nhật đơn hàng
    public DonHang updateDonHang(Long idDonHang, DonHangRequest donHangRequest) {
        Optional<DonHang> optionalDonHang = donHangRepository.findById(idDonHang);
        if (optionalDonHang.isPresent()) {
            DonHang donHang = optionalDonHang.get();
            donHang.setTrangThai(donHangRequest.getTrangThai());
            donHang.setTongTien(donHangRequest.getTongTien());
            donHang.setNguoiDung(nguoiDungRepository.findById(donHangRequest.getNguoiDungId()).get());
            return donHangRepository.save(donHang);
        } else {
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
    }

    // Lấy chi tiết đơn hàng theo ID
    public DonHang getDonHangDetail(Long idDonHang) {
        Optional<DonHang> optionalDonHang = donHangRepository.findById(idDonHang);
        if (optionalDonHang.isPresent()) {
            return optionalDonHang.get();
        } else {
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
    }

    // Xóa đơn hàng theo ID
    public void deleteDonHang(Long idDonHang) {
        Optional<DonHang> optionalDonHang = donHangRepository.findById(idDonHang);
        if (optionalDonHang.isPresent()) {
            donHangRepository.delete(optionalDonHang.get());
        } else {
            throw new RuntimeException("Đơn hàng không tồn tại!");
        }
    }
    public DonHang taoDonHang(Long nguoiDungId, Long maGiamGiaId, String phuongThucThanhToan) {
        GioHang gioHang = gioHangService.layGioHangCuaNguoiDung(String.valueOf(nguoiDungId));
        List<SanPhamTrongGioHang> sanPhamTrongGioHangs = gioHang.getSanPhamTrongGioHangs();

        double tongTien = sanPhamTrongGioHangs.stream()
                .mapToDouble(sp -> sp.getSoluong() * sp.getSanPham().getGia())
                .sum();

        // Áp dụng mã giảm giá nếu có
        if (maGiamGiaId != null) {
            tongTien = maGiamGiaService.apDungMaGiamGia(maGiamGiaId, (float) tongTien);
        }

        DonHang donHang = new DonHang();
        donHang.setNguoiDung(gioHang.getNguoiDung());
        donHang.setTongTien(tongTien);
        donHang.setPhuongThucThanhToan(phuongThucThanhToan);
        donHang.setNgayTao(new Date());
        donHang.setTrangThai("Đang xử lý");

        return donHangRepository.save(donHang);
    }
    public List<DonHang> getOrdersByCustomerId(Long nguoiDungID) {
        return donHangRepository.findByNguoiDungId(nguoiDungID);
    }
    @Transactional
    public DonHang luuDonHang(DonHang donHang, List<ChiTietDonHang> chiTietDonHangs) {
        // Lưu đơn hàng
        DonHang savedDonHang = donHangRepository.save(donHang);

        // Lưu chi tiết đơn hàng
        for (ChiTietDonHang chiTiet : chiTietDonHangs) {
            chiTiet.setDonHang(savedDonHang);
            chiTietDonHangRepository.save(chiTiet);
        }

        return savedDonHang;
    }

}