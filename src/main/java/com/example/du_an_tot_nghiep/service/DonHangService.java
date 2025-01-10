package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.*;
import com.example.du_an_tot_nghiep.model.DonHangRequest;
import com.example.du_an_tot_nghiep.repository.*;

import jakarta.mail.MessagingException;
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
    SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired

    private DonHangRepository donHangRepository;
    @Autowired
    private MaGiamGiaService maGiamGiaService;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private EmailService emailService;

    public void confirmOrder(Long donHangId) throws MessagingException {
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

                // Tạo bảng chi tiết sản phẩm và tổng tiền
                StringBuilder emailContent = new StringBuilder();
                emailContent.append("<h2>Xác Nhận Thanh Toán Đơn Hàng</h2>");
                emailContent.append("<p>Cảm ơn bạn đã mua hàng! Đơn hàng của bạn đã được xác nhận và đang vận chuyển.</p>");
                emailContent.append("<h3>Chi Tiết Sản Phẩm</h3>");
                emailContent.append("<table border='1' cellpadding='10' cellspacing='0'>");
                emailContent.append("<thead><tr><th>Sản phẩm</th><th>Số lượng</th><th>Giá</th><th>Tổng</th></tr></thead>");
                emailContent.append("<tbody>");

                double totalAmount = 0;
                for (ChiTietDonHang chiTiet : donHang.getChiTietDonHangs()) {
                    double totalPrice = chiTiet.getGiaDonVi() * chiTiet.getSoLuong();
                    totalAmount += totalPrice;
                    emailContent.append("<tr>");
                    emailContent.append("<td>").append(chiTiet.getSanPham().getTenSanPham()).append("</td>");
                    emailContent.append("<td>").append(chiTiet.getSoLuong()).append("</td>");
                    emailContent.append("<td>").append(chiTiet.getGiaDonVi()).append("</td>");
                    emailContent.append("<td>").append(totalPrice).append("</td>");
                    emailContent.append("</tr>");
                }

                emailContent.append("</tbody>");
                emailContent.append("</table>");
                emailContent.append("<p><strong>Tổng Tiền: </strong>").append(totalAmount).append("</p>");

                // Thêm logo vào email
                emailContent.append("<img src='cid:logo' alt='Logo' style='width: 150px; height: auto;'/>");

                // Gửi email thanh toán dưới dạng HTML
                String subject = "Xác Nhận Thanh Toán Đơn Hàng";
                emailService.sendPaymentEmail(email, subject, emailContent.toString());
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
    public String hoanTatDonHang(Long donHangId) {
        // Lấy đơn hàng theo ID
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        // Kiểm tra trạng thái đơn hàng
        if (!"Đang vận chuyển".equals(donHang.getTrangThai())) {
            return "Đơn hàng không ở trạng thái Đang vận chuyển, không thể hoàn tất";
        }

        // Kiểm tra chi tiết đơn hàng
        for (ChiTietDonHang chiTiet : donHang.getChiTietDonHangs()) {
            SanPhamChiTiet sanPhamChiTiet = chiTiet.getSanPhamChiTiet();
            if (sanPhamChiTiet.getSoLuong() < chiTiet.getSoLuong()) {
                return "Sản phẩm " + sanPhamChiTiet.getSanPham().getTenSanPham() + " không đủ số lượng";
            }
        }

        // Cập nhật số lượng tồn kho
        for (ChiTietDonHang chiTiet : donHang.getChiTietDonHangs()) {
            SanPhamChiTiet sanPhamChiTiet = chiTiet.getSanPhamChiTiet();
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - chiTiet.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);
        }

        // Cập nhật trạng thái đơn hàng
        donHang.setTrangThai("Hoàn tất");
        donHang.setNgayCapNhat(new Date());
        donHangRepository.save(donHang);

        return "Hoàn tất đơn hàng thành công!";
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
    public String huyDonHang(Long id) {
        // Tìm đơn hàng theo ID
        Optional<DonHang> optionalDonHang = donHangRepository.findById(id);
        if (!optionalDonHang.isPresent()) {
            throw new IllegalStateException("Không tìm thấy đơn hàng với ID: " + id);
        }

        DonHang donHang = optionalDonHang.get();

        // Kiểm tra trạng thái
        if ("Đang vận chuyển".equalsIgnoreCase(donHang.getTrangThai())) {
            throw new IllegalStateException("Đơn hàng đang vận chuyển, không thể huỷ.");
        }

        // Cập nhật trạng thái thành "Đã huỷ"
        donHang.setTrangThai("Đã huỷ");
        donHangRepository.save(donHang);

        return "Đơn hàng đã được huỷ thành công.";
    }
}