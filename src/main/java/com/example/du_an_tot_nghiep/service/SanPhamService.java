//package com.example.du_an_tot_nghiep.service;
//
//import com.example.du_an_tot_nghiep.entity.KichCo;
//import com.example.du_an_tot_nghiep.entity.LoaiSanPham;
//import com.example.du_an_tot_nghiep.entity.MauSac;
//import com.example.du_an_tot_nghiep.entity.SanPham;
//import com.example.du_an_tot_nghiep.model.SanPhamRequest;
//import com.example.du_an_tot_nghiep.repository.KichCoRepository;
//import com.example.du_an_tot_nghiep.repository.LoaiSanPhamRepository;
//import com.example.du_an_tot_nghiep.repository.MauSacRepository;
//import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class SanPhamService {
//
//
//    @Autowired
//    private SanPhamRepository sanPhamRepository;
//
//    @Autowired
//    private LoaiSanPhamRepository loaiSanPhamRepository;
//    @Autowired
//    private KichCoRepository kichCoRepository;
//    @Autowired
//    private MauSacRepository mauSacRepository;
//
//    private static final String UPLOAD_DIR = "C:\\images";
//
//    public SanPham addSanPham(SanPhamRequest sanPhamRequest) {
//        // Kiểm tra các trường hợp null cho sanPhamRequest
//        if (sanPhamRequest == null) {
//            throw new IllegalArgumentException("SanPhamRequest không được null");
//        }
//
//        // Khởi tạo SanPham
//        SanPham sanPham = new SanPham();
//        sanPham.setTenSanPham(sanPhamRequest.getTenSanPham());
//        sanPham.setMoTa(sanPhamRequest.getMoTa());
//        sanPham.setGia(sanPhamRequest.getGia());
//        sanPham.setSoLuong(sanPhamRequest.getSoLuong());
//        sanPham.setTrangThai(sanPhamRequest.getTrangThai());
//
//        // Lấy và thiết lập màu sắc
//        MauSac mauSac = mauSacRepository.findById(sanPhamRequest.getMauSac())
//                .orElseThrow(() -> new IllegalArgumentException("MauSac không tìm thấy"));
//        sanPham.setMauSac(mauSac);
//
//        // Lấy và thiết lập kích cỡ
//        KichCo kichCo = kichCoRepository.findById(sanPhamRequest.getKichCo())
//                .orElseThrow(() -> new IllegalArgumentException("KichCo không tìm thấy"));
//        sanPham.setKichCo(kichCo);
//
//        sanPham.setNgayTao(LocalDateTime.now());
//
//        // Lưu hình ảnh
//        MultipartFile hinhAnhFile = sanPhamRequest.getHinhAnh();
//        if (hinhAnhFile != null && !hinhAnhFile.isEmpty()) {
//            try {
//                // Lưu file vào thư mục
//                String fileName = hinhAnhFile.getOriginalFilename();
//                Path uploadPath = Paths.get(UPLOAD_DIR);
//                Files.createDirectories(uploadPath); // Tạo thư mục nếu chưa tồn tại
//
//                Path filePath = uploadPath.resolve(fileName);
//                hinhAnhFile.transferTo(filePath.toFile());
//
//                // Lưu tên file hoặc đường dẫn file vào DB
//                sanPham.setHinhAnh(fileName);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                throw new RuntimeException("Lỗi khi lưu hình ảnh: " + e.getMessage());
//            }
//        } else {
//            sanPham.setHinhAnh(null); // Nếu không có hình ảnh thì set là null
//        }
//
//        System.out.println(sanPham);
//        return sanPhamRepository.save(sanPham);
//    }
//
//
//    public SanPham updateSanPham(Long idSanPham, SanPhamRequest sanPhamRequest) {
//        // Tìm sản phẩm theo ID
//        SanPham sanPham = sanPhamRepository.findById(idSanPham)
//                .orElseThrow(() -> new IllegalArgumentException("SanPham không tìm thấy"));
//
//        // Cập nhật các thuộc tính sản phẩm
//        sanPham.setTenSanPham(sanPhamRequest.getTenSanPham());
//        sanPham.setMoTa(sanPhamRequest.getMoTa());
//        sanPham.setGia(sanPhamRequest.getGia());
//        sanPham.setSoLuong(sanPhamRequest.getSoLuong());
//        sanPham.setTrangThai(sanPhamRequest.getTrangThai());
//
//        // Cập nhật màu sắc
//        MauSac mauSac = mauSacRepository.findById(sanPhamRequest.getMauSac())
//                .orElseThrow(() -> new IllegalArgumentException("MauSac không tìm thấy"));
//        sanPham.setMauSac(mauSac);
//
//        // Cập nhật kích cỡ
//        KichCo kichCo = kichCoRepository.findById(sanPhamRequest.getKichCo())
//                .orElseThrow(() -> new IllegalArgumentException("KichCo không tìm thấy"));
//        sanPham.setKichCo(kichCo);
//
//        // Cập nhật loại sản phẩm
//        LoaiSanPham loaiSanPham = loaiSanPhamRepository.findById(sanPhamRequest.getLoaiSanPham())
//                .orElseThrow(() -> new IllegalArgumentException("LoaiSanPham không tìm thấy"));
//        sanPham.setLoaiSanPham(loaiSanPham);
//
//        // Lưu hình ảnh
//        MultipartFile hinhAnhFile = sanPhamRequest.getHinhAnh();
//        if (hinhAnhFile != null && !hinhAnhFile.isEmpty()) {
//            try {
//                // Lưu file vào thư mục
//                String fileName = hinhAnhFile.getOriginalFilename();
//                Path uploadPath = Paths.get(UPLOAD_DIR);
//                Files.createDirectories(uploadPath); // Tạo thư mục nếu chưa tồn tại
//
//                Path filePath = uploadPath.resolve(fileName);
//                hinhAnhFile.transferTo(filePath.toFile());
//
//                // Lưu tên file hoặc đường dẫn file vào DB
//                sanPham.setHinhAnh(fileName);
//            } catch (IOException e) {
//                e.printStackTrace();
//                throw new RuntimeException("Lỗi khi lưu hình ảnh: " + e.getMessage());
//            }
//        } // Nếu không có hình ảnh, giữ nguyên giá trị hiện tại
//
//        return sanPhamRepository.save(sanPham);
//    }
//    public Optional<SanPham> getSanPhamById(Long sanPhamId) {
//        return sanPhamRepository.findById(sanPhamId);
//    }
//
//    public List<SanPham> getAllSanPhams() {
//        return sanPhamRepository.findAll();
//    }
//}
