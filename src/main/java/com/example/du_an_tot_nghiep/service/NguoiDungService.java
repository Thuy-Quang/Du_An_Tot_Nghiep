package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.NguoiDung;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NguoiDungService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;


    // Lấy danh sách tất cả người dùng
    public List<NguoiDung> findAll() {
        return nguoiDungRepository.findAll();
    }

    // Lấy thông tin người dùng theo ID
    public NguoiDung findById(Long id) {
        return nguoiDungRepository.findById(id).orElse(null);
    }

    public Page<NguoiDung> getAllNguoiDung(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return nguoiDungRepository.findAll(pageable);
    }
    public Optional<NguoiDung> findByUsername(String username) {
        return nguoiDungRepository.findByTenDangNhap(username);
    }




    public NguoiDung save(NguoiDung nguoiDung) {
        // Kiểm tra xem email đã tồn tại hay chưa
        Optional<NguoiDung> existingUser = nguoiDungRepository.findByEmail(nguoiDung.getEmail());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(nguoiDung.getId())) {
            throw new IllegalArgumentException("Email đã tồn tại.");
        }
        return nguoiDungRepository.save(nguoiDung);
    }

    public void deleteById(Long id) {
        Optional<NguoiDung> nguoiDung = nguoiDungRepository.findById(id);
        if (nguoiDung.isPresent()) {
            // Nếu cần kiểm tra liên kết với các bảng khác, bạn có thể thêm logic tại đây
            nguoiDungRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Người dùng không tồn tại.");
        }
    }

}
