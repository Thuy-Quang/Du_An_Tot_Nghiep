package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.KichCo;

import com.example.du_an_tot_nghiep.entity.MauSac;
import com.example.du_an_tot_nghiep.model.KichCoDTO;
import com.example.du_an_tot_nghiep.repository.KichCoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class KichCoService {
    @Autowired
    private KichCoRepository kichCoRepository;  // Repository để truy vấn dữ liệu từ DB

    public List<KichCo> findAll() {
        return kichCoRepository.findAll();
    }

    public List<KichCo> getAllSizes() {
        return kichCoRepository.findAll();  // Trả về tất cả các màu sắc trong cơ sở dữ liệu
    }


}
