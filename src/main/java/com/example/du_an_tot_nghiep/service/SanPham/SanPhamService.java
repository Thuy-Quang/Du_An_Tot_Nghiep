package com.example.du_an_tot_nghiep.service.SanPham;

import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.model.SanPhamRequest;
import com.example.du_an_tot_nghiep.repository.LoaiSanPhamRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class SanPhamService {


    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    private static final String UPLOAD_DIR = "C:\\images";

    public SanPham addSanPham(SanPhamRequest sanPhamRequest) {
        SanPham sanPham = new SanPham();
        sanPham.setTenSanPham(sanPhamRequest.getTenSanPham());
        sanPham.setMoTa(sanPhamRequest.getMoTa());
        sanPham.setGia(sanPhamRequest.getGia());
        sanPham.setMauSac(sanPhamRequest.getMauSac());
        sanPham.setKichCo(sanPhamRequest.getKichCo());
        sanPham.setSoLuong(sanPhamRequest.getSoLuong());
        sanPham.setNgayTao(LocalDateTime.now());
        sanPham.setTrangThai(sanPhamRequest.getTrangThai());
        sanPham.setLoaiSanPham(loaiSanPhamRepository.findById(sanPhamRequest.getLoaiSanPham()).get());
        MultipartFile hinhAnhFile = sanPhamRequest.getHinhAnh();
        if (hinhAnhFile != null && !hinhAnhFile.isEmpty()) {
            try {
                // Lưu file vào thư mục
                String fileName = hinhAnhFile.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                Files.createDirectories(uploadPath); // Tạo thư mục nếu chưa tồn tại

                Path filePath = uploadPath.resolve(fileName);
                hinhAnhFile.transferTo(filePath.toFile());

                // Lưu tên file hoặc đường dẫn file vào DB
                sanPham.setHinhAnh(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sanPham.setHinhAnh(null);
        }
        System.out.println(sanPham);
        return sanPhamRepository.save(sanPham);
    }

    public SanPham updateSanPham(Long idSanPham, SanPhamRequest sanPhamRequest) {
        SanPham sanPham = sanPhamRepository.findById(idSanPham).get();
        sanPham.setTenSanPham(sanPhamRequest.getTenSanPham());
        sanPham.setMoTa(sanPhamRequest.getMoTa());
        sanPham.setGia(sanPhamRequest.getGia());
        sanPham.setMauSac(sanPhamRequest.getMauSac());
        sanPham.setKichCo(sanPhamRequest.getKichCo());
        sanPham.setSoLuong(sanPhamRequest.getSoLuong());
        sanPham.setTrangThai(sanPhamRequest.getTrangThai());
        sanPham.setLoaiSanPham(loaiSanPhamRepository.findById(sanPhamRequest.getLoaiSanPham()).get());
        MultipartFile hinhAnhFile = sanPhamRequest.getHinhAnh();
        if (hinhAnhFile != null && !hinhAnhFile.isEmpty()) {
            try {
                // Lưu file vào thư mục
                String fileName = hinhAnhFile.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                Files.createDirectories(uploadPath); // Tạo thư mục nếu chưa tồn tại

                Path filePath = uploadPath.resolve(fileName);
                hinhAnhFile.transferTo(filePath.toFile());

                // Lưu tên file hoặc đường dẫn file vào DB
                sanPham.setHinhAnh(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sanPham.setHinhAnh(null);
        }
        return sanPhamRepository.save(sanPham);
    }

}
