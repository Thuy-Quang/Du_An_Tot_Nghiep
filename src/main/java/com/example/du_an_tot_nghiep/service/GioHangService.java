package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.GioHang;
import com.example.du_an_tot_nghiep.entity.NguoiDung;
import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.entity.SanPhamTrongGioHang;
import com.example.du_an_tot_nghiep.repository.GioHangRepository;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamTrongGioHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class GioHangService {
    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private SanPhamTrongGioHangRepository sanPhamTrongGioHangRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

//    public boolean themSanPhamVaoGioHang(String tenNguoiDung, Long sanPhamId, int soLuong) {
//        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(tenNguoiDung)
//                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
//
//        GioHang gioHang = gioHangRepository.findByNguoiDung(nguoiDung)
//                .orElseGet(() -> gioHangRepository.save(new GioHang(new Date(), nguoiDung)));
//
//        SanPham sanPham = sanPhamRepository.findById(sanPhamId)
//                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
//
//        Optional<SanPhamTrongGioHang> spTrongGio = gioHang.getSanPhamTrongGioHangs()
//                .stream()
//                .filter(sp -> sp.getSanPham().equals(sanPham))
//                .findFirst();
//
//        if (spTrongGio.isPresent()) {
//            spTrongGio.get().setSoluong(spTrongGio.get().getSoluong() + soLuong);
//        } else {
//            gioHang.getSanPhamTrongGioHangs().add(new SanPhamTrongGioHang(gioHang, sanPham, soLuong));
//        }
//
//        gioHangRepository.save(gioHang);
//        return true;
//    }

//    public GioHang layGioHangCuaNguoiDung(String tenNguoiDung) {
//        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(tenNguoiDung)
//                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
//
//        return gioHangRepository.findByNguoiDung(nguoiDung)
//                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));
//    }
    public void themSanPhamVaoGioHang(SanPham sanPham, int soLuong) {
        // Thêm logic xử lý lưu trữ sản phẩm vào giỏ hàng (cơ sở dữ liệu hoặc session)
        System.out.println("Thêm sản phẩm: " + sanPham.getTenSanPham() + " - Số lượng: " + soLuong);
    }

    public GioHang layGioHangCuaNguoiDung(String tenNguoiDung) {
        // Trả về giỏ hàng mẫu (hoặc từ cơ sở dữ liệu)
        GioHang gioHang = new GioHang();
        // Thêm logic lấy giỏ hàng từ DB dựa vào tên người dùng
        return gioHang;
    }

}
