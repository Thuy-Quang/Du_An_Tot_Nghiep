package com.example.du_an_tot_nghiep.controller.SanPham.Nam;

import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class AllController {
    @Autowired
    SanPhamRepository sanPhamRepository;

    @RequestMapping("/AoNamAll")
    public String getSanPhamsByLoaiSanPhamId(Model model) {
        // Lấy tất cả các sản phẩm có loai_san_pham_id = 1
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(1L, 2L));
        model.addAttribute("lists", sanPhams);
        return "SanPham/QuanAoNam/Nam/All";  // Tên trang Thymeleaf của bạn
    }

}
