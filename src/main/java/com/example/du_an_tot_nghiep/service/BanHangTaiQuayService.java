package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.*;
import com.example.du_an_tot_nghiep.model.ChiTietDonHangReq;
import com.example.du_an_tot_nghiep.model.DonHangReq;
import com.example.du_an_tot_nghiep.repository.ChiTietDonHangRepository;
import com.example.du_an_tot_nghiep.repository.DonHangRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamChiTietRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class BanHangTaiQuayService {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private ChiTietDonHangService chiTietDonHangService;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private NguoiDungService nguoiDungService;

    // Tạo đơn hàng mới
    public void taoDonHang(Long nguoiDungId, String phuongThucThanhToan) {
        // Lấy thông tin khách hàng
        NguoiDung nguoiDung = nguoiDungService.findById(nguoiDungId);

        // Tạo đối tượng đơn hàng
        DonHang donHang = new DonHang();
        donHang.setNguoiDung(nguoiDung);
        donHang.setPhuongThucThanhToan(phuongThucThanhToan);
        donHang.setTongTien(0.0); // Ban đầu để 0, sẽ cập nhật sau
        donHang.setTrangThai("Chưa xác nhận");
        donHang.setNgayTao(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        // Lưu vào cơ sở dữ liệu
        donHangRepository.save(donHang);
    }

    // xuất dữ liệu, phân trang
    public Map<String, Object> loadManagementPage(int pageSanPham, int pageDonHang, int pageChiTiet, int size, Long idDonHangDuocChon) {
        Map<String, Object> danhSach = new HashMap<>();

        // Lấy danh sách sản phẩm
        Page<SanPham> sanPhamPage = sanPhamService.getAll(PageRequest.of(pageSanPham, size));
        danhSach.put("listSanPham", sanPhamPage.getContent());
        danhSach.put("currentPageSanPham", pageSanPham);
        danhSach.put("totalPagesSanPham", sanPhamPage.getTotalPages());
        // Lấy toàn bộ sản phẩm không phân trang để hiển thị trong modal
        List<SanPham> sanPhamListFull = sanPhamService.getAll();
        danhSach.put("listSanPhamFull", sanPhamListFull);

        // Lấy danh sách đơn hàng, phân trang và sắp xếp giảm dần theo ngày tạo
        Page<DonHang> donHangPage = donHangRepository.findAllByOrderByNgayTaoDesc(PageRequest.of(pageDonHang, size));
        danhSach.put("listDonHang", donHangPage.getContent());
        danhSach.put("currentPageDonHang", pageDonHang);
        danhSach.put("totalPagesDonHang", donHangPage.getTotalPages());

        // Xử lý chi tiết đơn hàng nếu có đơn hàng được chọn
        if (idDonHangDuocChon != null) {
            DonHang donHangDuocChon = donHangService.findById(idDonHangDuocChon);
            Page<ChiTietDonHang> chiTietDonHangPage = chiTietDonHangService.getPagedChiTietByOrderId(idDonHangDuocChon, PageRequest.of(pageChiTiet, size));

            danhSach.put("donHangDuocChon", donHangDuocChon);
            danhSach.put("listChiTietDonHang", chiTietDonHangPage.getContent());
            danhSach.put("currentPageChiTiet", pageChiTiet);
            danhSach.put("totalPagesChiTiet", chiTietDonHangPage.getTotalPages());
        }

        return danhSach;
    }

    // thêm sản phẩm
    public void addSPVaoDonHang(Long donHangId, Long sanPhamId, int soLuong) {
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đơn hàng!"));

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(sanPhamId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm!"));

        // Kiểm tra số lượng tồn kho
        if (sanPhamChiTiet.getSoLuong() < soLuong) {
            throw new RuntimeException("Sản phẩm " + sanPhamChiTiet.getSanPham().getTenSanPham() +
                    " không đủ số lượng trong kho!");
        }

        // Cập nhật logic thêm sản phẩm vào đơn hàng (như đã có)
        Optional<ChiTietDonHang> existingDetail = donHang.getChiTietDonHangs().stream()
                .filter(ct -> ct.getSanPhamChiTiet().getId().equals(sanPhamId))
                .findFirst();

        if (existingDetail.isPresent()) {
            ChiTietDonHang chiTiet = existingDetail.get();
            chiTiet.setSoLuong(chiTiet.getSoLuong() + soLuong);
        } else {
            ChiTietDonHang chiTietMoi = new ChiTietDonHang();
            chiTietMoi.setDonHang(donHang);
            chiTietMoi.setSanPhamChiTiet(sanPhamChiTiet);
            chiTietMoi.setSoLuong(soLuong);
            chiTietMoi.setGiaDonVi(sanPhamChiTiet.getSanPham().getGia());
            donHang.getChiTietDonHangs().add(chiTietMoi);
        }

        // Lưu đơn hàng
        donHangRepository.save(donHang);
    }

    // tính tổng tiền
    public void tinhTongTien(DonHang donHang) {
        double tongTien = donHang.getChiTietDonHangs()
                .stream()
                .mapToDouble(ct -> ct.getSoLuong() * ct.getGiaDonVi())
                .sum();
        donHang.setTongTien(tongTien);
        donHangRepository.save(donHang); // Lưu tổng tiền vào DB
    }


    // cật nhật số lượng
    public void capNhatSoLuong(Long donHangId, Map<Long, Integer> soLuongMap) {
        DonHang donHang = donHangRepository.findById(donHangId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đơn hàng!"));

        for (ChiTietDonHang chiTiet : donHang.getChiTietDonHangs()) {
            Long sanPhamChiTietId = chiTiet.getSanPhamChiTiet().getId();
            if (soLuongMap.containsKey(sanPhamChiTietId)) {
                int soLuongMoi = soLuongMap.get(sanPhamChiTietId);

                // Kiểm tra số lượng tồn kho
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(sanPhamChiTietId)
                        .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm chi tiết!"));

                if (sanPhamChiTiet.getSoLuong() < soLuongMoi) {
                    throw new IllegalArgumentException("Số lượng sản phẩm vượt quá số lượng tồn kho: " + sanPhamChiTiet.getSanPham().getTenSanPham());
                }

                chiTiet.setSoLuong(soLuongMoi);
            }
        }

        donHangRepository.save(donHang);
    }

    // Thêm hoặc cập nhật chi tiết đơn hàng
    public void capNhatChiTiet(Long donHangId, Long sanPhamChiTietId, int soLuong, boolean isCongDon) {
        Optional<ChiTietDonHang> optionalChiTiet = chiTietDonHangRepository.findByDonHangIdAndSanPhamChiTietId(donHangId, sanPhamChiTietId);

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(sanPhamChiTietId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm chi tiết không tồn tại với ID: " + sanPhamChiTietId));

        // Kiểm tra số lượng tồn kho
        if (sanPhamChiTiet.getSoLuong() < soLuong) {
            throw new IllegalArgumentException("Số lượng sản phẩm vượt quá số lượng tồn kho: " + sanPhamChiTiet.getSanPham().getTenSanPham());
        }

        if (optionalChiTiet.isPresent()) {
            ChiTietDonHang chiTiet = optionalChiTiet.get();
            chiTiet.setSoLuong(isCongDon ? chiTiet.getSoLuong() + soLuong : soLuong);
            chiTietDonHangRepository.save(chiTiet);
        } else {
            ChiTietDonHang chiTietMoi = new ChiTietDonHang();
            chiTietMoi.setDonHang(new DonHang(donHangId));
            chiTietMoi.setSanPhamChiTiet(sanPhamChiTiet);
            chiTietMoi.setSoLuong(soLuong);
            chiTietMoi.setGiaDonVi(sanPhamChiTiet.getSanPham().getGia());
            chiTietDonHangRepository.save(chiTietMoi);
        }
    }

    // Thanh toán đơn hàng
    public void thanhToanDonHang(Long donHangId) {
        Optional<DonHang> optionalDonHang = donHangRepository.findById(donHangId);
        if (optionalDonHang.isPresent()) {
            DonHang donHang = optionalDonHang.get();

            // Lặp qua tất cả chi tiết đơn hàng
            for (ChiTietDonHang chiTiet : donHang.getChiTietDonHangs()) {
                SanPhamChiTiet sanPhamChiTiet = chiTiet.getSanPhamChiTiet();

                // Kiểm tra số lượng sản phẩm trong kho
                if (sanPhamChiTiet.getSoLuong() < chiTiet.getSoLuong()) {
                    throw new RuntimeException("Sản phẩm " + sanPhamChiTiet.getSanPham().getTenSanPham() +
                            " không đủ số lượng trong kho!");
                }

                // Trừ số lượng sản phẩm trong kho
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - chiTiet.getSoLuong());
                sanPhamChiTietRepository.save(sanPhamChiTiet);
            }

            // Tính tổng tiền (nếu cần)
            double tongTien = donHang.getChiTietDonHangs().stream()
                    .mapToDouble(ct -> ct.getSoLuong() * ct.getGiaDonVi())
                    .sum();
            donHang.setTongTien(tongTien);

            // Cập nhật trạng thái đơn hàng
            donHang.setTrangThai("Đã thanh toán");
            donHangRepository.save(donHang);
        } else {
            throw new RuntimeException("Đơn hàng không tồn tại.");
        }
    }

    public DonHang taoDonHangTuYeuCau(DonHangReq donHangReq) {
        // Kiểm tra danh sách sản phẩm
        if (donHangReq.getSanPhamList() == null || donHangReq.getSanPhamList().isEmpty()) {
            throw new IllegalArgumentException("Danh sách sản phẩm không được để trống.");
        }

        // Kiểm tra người dùng
        NguoiDung nguoiDung = nguoiDungService.findById(donHangReq.getNguoiDungId());
        if (nguoiDung == null) {
            throw new EntityNotFoundException("Người dùng không tồn tại.");
        }

        // Tạo đơn hàng mới
        DonHang donHang = new DonHang();
        donHang.setTongTien(0.0); // Mặc định tổng tiền ban đầu là 0
        donHang.setNguoiDung(nguoiDung);
        donHang.setNgayTao(new Date());
        donHang.setPhuongThucThanhToan(donHangReq.getPhuongThucThanhToan());
        donHang.setTrangThai("Đang xử lý");
        donHang.setTrangThaiThanhToan("Chưa thanh toán");

        double tongTien = 0.0;
        List<ChiTietDonHang> chiTietDonHangList = new ArrayList<>();

        for (ChiTietDonHangReq chiTietReq : donHangReq.getSanPhamList()) {
            // Kiểm tra dữ liệu đầu vào của từng sản phẩm
            if (chiTietReq.getSoLuong() == null || chiTietReq.getSoLuong() <= 0) {
                throw new IllegalArgumentException("Số lượng không hợp lệ cho sản phẩm ID: " + chiTietReq.getSanPhamId());
            }
            if (chiTietReq.getGiaDonVi() == null || chiTietReq.getGiaDonVi() <= 0) {
                throw new IllegalArgumentException("Giá đơn vị không hợp lệ cho sản phẩm ID: " + chiTietReq.getSanPhamId());
            }

            // Tìm chi tiết sản phẩm
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(chiTietReq.getSanPhamId())
                    .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại với ID: " + chiTietReq.getSanPhamId()));

            // Kiểm tra số lượng sản phẩm tồn kho
            if (sanPhamChiTiet.getSoLuong() < chiTietReq.getSoLuong()) {
                throw new IllegalArgumentException("Sản phẩm không đủ số lượng: " + sanPhamChiTiet.getSanPham().getTenSanPham());
            }

            // Cập nhật số lượng tồn kho
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - chiTietReq.getSoLuong());
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            // Tạo chi tiết đơn hàng
            ChiTietDonHang chiTiet = new ChiTietDonHang();
            chiTiet.setDonHang(donHang);
            chiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            chiTiet.setSoLuong(chiTietReq.getSoLuong());
            chiTiet.setGiaDonVi(chiTietReq.getGiaDonVi());

            chiTietDonHangList.add(chiTiet);

            // Cộng tổng tiền
            tongTien += chiTietReq.getSoLuong() * chiTietReq.getGiaDonVi();
        }

        // Gán tổng tiền và chi tiết đơn hàng vào đơn hàng
        donHang.setChiTietDonHangs(chiTietDonHangList);
        donHang.setTongTien(tongTien);

        // Lưu đơn hàng vào database
        return donHangRepository.save(donHang);
    }

}

