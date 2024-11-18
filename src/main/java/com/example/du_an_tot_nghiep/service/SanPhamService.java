package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.model.SanPhamRequest;
import com.example.du_an_tot_nghiep.repository.KichCoRepository;
import com.example.du_an_tot_nghiep.repository.LoaiSanPhamRepository;
import com.example.du_an_tot_nghiep.repository.MauSacRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;


@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    KichCoRepository kichCoRepository;

    @Autowired
    MauSacRepository mauSacRepository;

    private static final String UPLOAD_DIR = "C:\\images";

    public SanPham addSanPham(SanPhamRequest sanPhamRequest) {
        SanPham sanPham = new SanPham();
        sanPham.setTenSanPham(sanPhamRequest.getTenSanPham());
        sanPham.setMoTa(sanPhamRequest.getMoTa());
        sanPham.setGia(sanPhamRequest.getGia());
        sanPham.setMauSac(mauSacRepository.findById(sanPhamRequest.getMauSac()).orElse(null));
        sanPham.setKichCo(kichCoRepository.findById(sanPhamRequest.getKichCo()).orElse(null));
        sanPham.setSoLuong(sanPhamRequest.getSoLuong());
        sanPham.setNgayTao(LocalDate.now());
        sanPham.setTrangThai(sanPhamRequest.getTrangThai());
        sanPham.setLoaiSanPham(loaiSanPhamRepository.findById(sanPhamRequest.getLoaiSanPham()).orElse(null));

        handleFileUpload(sanPham, sanPhamRequest.getHinhAnh());

        return sanPhamRepository.save(sanPham);
    }

    public SanPham updateSanPham(Long idSanPham, SanPhamRequest sanPhamRequest) {
        SanPham sanPham = sanPhamRepository.findById(idSanPham).orElse(null);
        if (sanPham == null) return null;

        sanPham.setTenSanPham(sanPhamRequest.getTenSanPham());
        sanPham.setMoTa(sanPhamRequest.getMoTa());
        sanPham.setGia(sanPhamRequest.getGia());
        sanPham.setMauSac(mauSacRepository.findById(sanPhamRequest.getMauSac()).orElse(null));
        sanPham.setKichCo(kichCoRepository.findById(sanPhamRequest.getKichCo()).orElse(null));
        sanPham.setSoLuong(sanPhamRequest.getSoLuong());
        sanPham.setTrangThai(sanPhamRequest.getTrangThai());
        sanPham.setLoaiSanPham(loaiSanPhamRepository.findById(sanPhamRequest.getLoaiSanPham()).orElse(null));

        if (sanPhamRequest.getHinhAnh() != null && !sanPhamRequest.getHinhAnh().isEmpty()) {
            handleFileUpload(sanPham, sanPhamRequest.getHinhAnh());
        }

        return sanPhamRepository.save(sanPham);
    }

    private void handleFileUpload(SanPham sanPham, MultipartFile hinhAnhFile) {
        if (hinhAnhFile != null && !hinhAnhFile.isEmpty()) {
            try {
                String fileName = hinhAnhFile.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                Files.createDirectories(uploadPath);

                Path filePath = uploadPath.resolve(fileName);
                hinhAnhFile.transferTo(filePath.toFile());

                sanPham.setHinhAnh(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public List<SanPham> getall(){
        List<SanPham> list = sanPhamRepository.findAll();
        return list;
    }
}
