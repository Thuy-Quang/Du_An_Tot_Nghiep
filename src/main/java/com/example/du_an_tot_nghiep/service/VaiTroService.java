package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.VaiTro;
import com.example.du_an_tot_nghiep.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VaiTroService {
    @Autowired
    private VaiTroRepository vaiTroRepository;

    public List<VaiTro> findAll() {
        return vaiTroRepository.findAll();
    }

    public Optional<VaiTro> findById(Long id) {
        return vaiTroRepository.findById(id);
    }

    public VaiTro save(VaiTro vaiTro) {
        return vaiTroRepository.save(vaiTro);
    }

    // Phương thức kiểm tra tên vai trò đã tồn tại
    public boolean existsByTenVaiTro(String tenVaiTro) {
        return vaiTroRepository.existsByTenVaiTro(tenVaiTro);
    }

    public void deleteById(Long id) {
        vaiTroRepository.deleteById(id);
    }

    public void update(Long id, VaiTro vaiTro) {
        VaiTro existingVaiTro = vaiTroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại!"));
        existingVaiTro.setTenVaiTro(vaiTro.getTenVaiTro());
        vaiTroRepository.save(existingVaiTro);
    }
}
