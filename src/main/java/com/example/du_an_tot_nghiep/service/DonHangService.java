package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.*;
import com.example.du_an_tot_nghiep.model.DonHangRequest;
import com.example.du_an_tot_nghiep.repository.ChiTietDonHangRepository;
import com.example.du_an_tot_nghiep.repository.DonHangRepository;
import com.example.du_an_tot_nghiep.repository.MaGiamGiaKhachHangRepository;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

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
    private MaGiamGiaKhachHangRepository maGiamGiaKhachHangRepository;
    @Autowired
    private GioHangService gioHangService;
    @Autowired
    private DonHangRepository donHangRepository;
    @Autowired
    private MaGiamGiaService maGiamGiaService;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private EmailService emailService;

    public void confirmOrder(Long donHangId) {
        // Lấy đơn hàng theo ID
        Optional<DonHang> donHangOpt = donHangRepository.findById(donHangId);

        if (donHangOpt.isPresent()) {
            DonHang donHang = donHangOpt.get();

            // Lấy email người dùng từ đơn hàng
            Long nguoiDungId = donHang.getNguoiDung().getId();
            Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findById(nguoiDungId);

            if (nguoiDungOpt.isPresent()) {
                NguoiDung nguoiDung = nguoiDungOpt.get();
                String email = nguoiDung.getEmail();

                // Gửi email thanh toán
                String subject = "Xác Nhận Thanh Toán Đơn Hàng";
                String text = "Cảm ơn bạn đã mua hàng! Đơn hàng của bạn đã được xác nhận và đang vận chuyển.";
                emailService.sendPaymentEmail(email, subject, text);
            }

            // Cập nhật trạng thái đơn hàng thành "Đang vận chuyển"
            donHang.setTrangThai("Đang vận chuyển");
            donHangRepository.save(donHang);
        }
    }
    public DonHangService(NguoiDungRepository nguoiDungRepository, DonHangRepository donHangRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.donHangRepository = donHangRepository;
    }

    public List<DonHang> getAll() {
        return donHangRepository.findAll();
    }

    // Lấy đơn hàng theo phân trang
    public Page<DonHang> getAll(Pageable pageable) {
        return donHangRepository.findAll(pageable);
    }

    //lấy danh sách đơn hàng sắp xếp theo ngày tạo
    public Page<DonHang> getAllSortedByNgayTaoDesc(Pageable pageable) {
        return donHangRepository.findAllByOrderByNgayTaoDesc(pageable);
    }

    // Tìm đơn hàng theo ID
    public DonHang findById(Long id) {
        return donHangRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Đơn hàng không tồn tại với ID: " + id));
    }

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
//    public DonHang taoDonHang(Long nguoiDungId, Long maGiamGiaId, String phuongThucThanhToan) {
//        GioHang gioHang = gioHangService.layGioHangCuaNguoiDung(String.valueOf(nguoiDungId));
//        List<SanPhamTrongGioHang> sanPhamTrongGioHangs = gioHang.getSanPhamTrongGioHangs();
//
//        double tongTien = sanPhamTrongGioHangs.stream()
//                .mapToDouble(sp -> sp.getSoluong() * sp.getSanPham().getGia())
//                .sum();
//
//        // Áp dụng mã giảm giá nếu có
//        if (maGiamGiaId != null) {
//            tongTien = maGiamGiaService.apDungMaGiamGia(maGiamGiaId, (float) tongTien);
//        }
//
//        DonHang donHang = new DonHang();
//        donHang.setNguoiDung(gioHang.getNguoiDung());
//        donHang.setTongTien(tongTien);
//        donHang.setPhuongThucThanhToan(phuongThucThanhToan);
//        donHang.setNgayTao(new Date());
//        donHang.setTrangThai("Đang xử lý");
//
//        return donHangRepository.save(donHang);
//    }
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

    public double apDungMaGiamGia(Long idMaGiamGiaKhachHang, Double tongTien) {
        Optional<MaGiamGiaKhachHang> maGiamGiaKH = maGiamGiaKhachHangRepository.findById(idMaGiamGiaKhachHang);
        if (maGiamGiaKH.isEmpty()) {
            throw new IllegalArgumentException("Mã giảm giá không tồn tại");
        }

        MaGiamGiaKhachHang maGiamGiaKhachHang = maGiamGiaKH.get();
        if (!maGiamGiaKhachHang.getTrangThai().equals("Hoạt động")) {
            return tongTien; // Mã giảm giá không hoạt động
        }

        double phanTramGiam = maGiamGiaKhachHang.getMaGiamGia().getPhanTramGiam();
        double soTienGiam = tongTien * (phanTramGiam / 100);

        // Cập nhật trạng thái mã giảm giá khách hàng
        maGiamGiaKhachHang.setTrangThai("Không hoạt động");
        maGiamGiaKhachHangRepository.save(maGiamGiaKhachHang);

        return tongTien - soTienGiam;
    }
    public List<DonHang> getOrdersByUserId(Long userId) {
        return donHangRepository.findByNguoiDungId(userId);
    }
    public DonHang getDonHangById(Long id) {
        return donHangRepository.findById(id).orElse(null);  // Trả về đơn hàng nếu có, nếu không có trả về null
    }
}