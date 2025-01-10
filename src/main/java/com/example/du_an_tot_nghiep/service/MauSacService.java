package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.MauSac;
import com.example.du_an_tot_nghiep.repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MauSacService {
    @Autowired
    private MauSacRepository mauSacRepository;  // Repository để truy vấn dữ liệu từ DB

    public List<MauSac> findAll() {
        return mauSacRepository.findAll();
    }

    // Phương thức lấy tất cả màu sắc từ cơ sở dữ liệu
    public List<MauSac> getAllColors() {
        return mauSacRepository.findAll();  // Trả về tất cả các màu sắc trong cơ sở dữ liệu
    }

    public List<MauSac> getAllMauSac() {
        return mauSacRepository.findAll();
    }
}
