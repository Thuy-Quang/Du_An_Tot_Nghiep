package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.ChiTietDonHang;
import com.example.du_an_tot_nghiep.model.ChiTietDonHangDTO;
import com.example.du_an_tot_nghiep.repository.ChiTietDonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.du_an_tot_nghiep.entity.DonHang;
import com.example.du_an_tot_nghiep.entity.SanPhamChiTiet;
import com.example.du_an_tot_nghiep.repository.ChiTietDonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChiTietDonHangService {
    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;
    // danh sách chi tiết dh cảu của 1 đươn hàng cụ thể
    public Page<ChiTietDonHang> getPagedChiTietByOrderId(Long orderId, Pageable pageable) {
        return chiTietDonHangRepository.findByDonHangId(orderId, pageable);
    }

    //Trả về danh sách các Chi Tiết Đơn Hàng dưới dạng phân trang.
    public Page<ChiTietDonHang> getAll(Pageable pageable) {
        return chiTietDonHangRepository.findAll(pageable);
    }

    // Lấy tất cả các chi tiết đơn hàng
    public List<ChiTietDonHang> getAll() {
        return chiTietDonHangRepository.findAll();
    }

    // Lấy chi tiết đơn hàng theo ID
    public Optional<ChiTietDonHang> getById(Long id) {
        return chiTietDonHangRepository.findById(id);
    }

    // Thêm mới chi tiết đơn hàng
    public ChiTietDonHang add(ChiTietDonHang chiTietDonHang) {
        return chiTietDonHangRepository.save(chiTietDonHang);
    }

    // Cập nhật chi tiết đơn hàng
    public ChiTietDonHang update(ChiTietDonHang chiTietDonHang) {
        return chiTietDonHangRepository.save(chiTietDonHang);
    }

    // Xóa chi tiết đơn hàng
    public void delete(Long id) {
        chiTietDonHangRepository.deleteById(id);
    }

    // Thêm hoặc cập nhật chi tiết đơn hàng
    public void addOrUpdateChiTiet(Long donHangId, Long sanPhamId, int soLuong) {
        // Tìm kiếm chi tiết đơn hàng theo donHangId và sanPhamId
        Optional<ChiTietDonHang> existingChiTiet = chiTietDonHangRepository.findByDonHangIdAndSanPhamChiTietId(donHangId, sanPhamId);

        if (existingChiTiet.isPresent()) {
            // Nếu chi tiết đơn hàng đã tồn tại, cập nhật số lượng
            ChiTietDonHang chiTiet = existingChiTiet.get();
            chiTiet.setSoLuong(chiTiet.getSoLuong() + soLuong); // Cộng dồn số lượng
            chiTietDonHangRepository.save(chiTiet);
        } else {
            // Nếu chưa tồn tại, tạo mới chi tiết đơn hàng
            ChiTietDonHang newChiTiet = new ChiTietDonHang();
            newChiTiet.setDonHang(new DonHang(donHangId));
            newChiTiet.setSanPhamChiTiet(new SanPhamChiTiet(sanPhamId));
            newChiTiet.setSoLuong(soLuong);
            chiTietDonHangRepository.save(newChiTiet);
        }
    }

    public ChiTietDonHangService(ChiTietDonHangRepository chiTietDonHangRepository) {
        this.chiTietDonHangRepository = chiTietDonHangRepository;
    }

    public List<ChiTietDonHangDTO> getChiTietDonHangByDonHangId(Long donHangId) {
        return chiTietDonHangRepository.findByDonHangId(donHangId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private ChiTietDonHangDTO mapToDTO(ChiTietDonHang chiTietDonHang) {
        return new ChiTietDonHangDTO(
                chiTietDonHang.getSanPhamChiTiet().getSanPham().getTenSanPham(),
                chiTietDonHang.getSanPhamChiTiet().getMauSac().getTenMau(),
                chiTietDonHang.getSanPhamChiTiet().getKichCo().getTenKichCo(),
                chiTietDonHang.getSoLuong(),
                chiTietDonHang.getGiaDonVi(),
                chiTietDonHang.getTongGia()
        );
    }
}
