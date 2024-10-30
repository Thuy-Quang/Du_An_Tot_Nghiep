package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.model.SanPhamRequest;
import com.example.du_an_tot_nghiep.repository.KichCoRepository;
import com.example.du_an_tot_nghiep.repository.LoaiSanPhamRepository;
import com.example.du_an_tot_nghiep.repository.MauSacRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import com.example.du_an_tot_nghiep.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/HienThi")
public class SanPhamController {

    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    KichCoRepository kichCoRepository;

    @Autowired
    MauSacRepository mauSacRepository;

    @Autowired
    SanPhamService sanPhamService;



    @RequestMapping("/index")
    public String index(){
        return "layout/index";
    }


//Nam

    @RequestMapping("/AoNam")
    public String getSanPhamsByLoaiSanPhamId(Model model) {
        // Lấy tất cả các sản phẩm có loai_san_pham_id = 1
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(1L));
        model.addAttribute("lists", sanPhams);
        return "SanPham/QuanAoNam/Nam/AoNam";  // Tên trang Thymeleaf của bạn
    }

    @RequestMapping("/QuanNam")
    public String getSanPhamsByLoaiSanPhamId1(Model model) {
        // Lấy tất cả các sản phẩm có loai_san_pham_id = 1
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(2L));
        model.addAttribute("lists", sanPhams);
        return "SanPham/QuanAoNam/Nam/QuanNam";  // Tên trang Thymeleaf của bạn
    }

    @RequestMapping("/AoNamAll")
    public String getSanPhamsByLoaiSanPhamId2(Model model) {
        // Lấy tất cả các sản phẩm có loai_san_pham_id = 1
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(1L, 2L));
        model.addAttribute("lists", sanPhams);
        return "SanPham/QuanAoNam/Nam/All";  // Tên trang Thymeleaf của bạn
    }


//    Nữ

    @RequestMapping("/AoNu")
    public String getSanPhamsByLoaiSanPhamId3(Model model) {
        // Lấy tất cả các sản phẩm có loai_san_pham_id = 1
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(3L));
        model.addAttribute("lists", sanPhams);
        return "SanPham/QuanAoNam/Nu/AoNu";  // Tên trang Thymeleaf của bạn
    }

    @RequestMapping("/QuanNu")
    public String getSanPhamsByLoaiSanPhamId4(Model model) {
        // Lấy tất cả các sản phẩm có loai_san_pham_id = 1
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(4L));
        model.addAttribute("lists", sanPhams);
        return "SanPham/QuanAoNam/Nu/QuanNu";  // Tên trang Thymeleaf của bạn
    }

    @RequestMapping("/TatCa")
    public String getSanPhamsByLoaiSanPhamId5(Model model) {
        // Lấy tất cả các sản phẩm có loai_san_pham_id = 1
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(3L, 4L));
        model.addAttribute("lists", sanPhams);
        return "SanPham/QuanAoNam/Nu/TatCa";  // Tên trang Thymeleaf của bạn
    }


//    CRUD



    @GetMapping("/GetAll")
    public String listSanPham(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SanPham> sanPhamPage = sanPhamRepository.findAll(pageable);

        model.addAttribute("listSPham", sanPhamPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sanPhamPage.getTotalPages());
        model.addAttribute("listLSP",loaiSanPhamRepository.findAll());
        model.addAttribute("listMauSac",mauSacRepository.findAll());
        model.addAttribute("listKichCo",kichCoRepository.findAll());

        return "SanPham/HienThiSP/index"; // Return to your Thymeleaf template
    }

    @GetMapping("/create")
    private  String getProject(Model model){
        model.addAttribute("listSPham",sanPhamRepository.findAll());
        model.addAttribute("listLSP",loaiSanPhamRepository.findAll());
        model.addAttribute("listMauSac",mauSacRepository.findAll());
        model.addAttribute("listKichCo",kichCoRepository.findAll());
        return "SanPham/HienThiSP/Add";
    }

    @PostMapping("/create")
    private String postSanPham(SanPhamRequest sanPhamRequest) {
        sanPhamService.addSanPham(sanPhamRequest);
        return "redirect:/HienThi/GetAll";
    }

    @GetMapping("/listSPham/edit/id/{id}")
    private String putSanPham(@PathVariable("id") Long id, Model model) {
        model.addAttribute("detail", sanPhamRepository.findById(id).get());
        model.addAttribute("listLSP",loaiSanPhamRepository.findAll());
        model.addAttribute("listMauSac",mauSacRepository.findAll());
        model.addAttribute("listKichCo",kichCoRepository.findAll());
        return "SanPham/HienThiSP/Update";
    }

    @PostMapping("/listSPham/edit/id/{id}")
    private String putSanPham(@PathVariable Long id, SanPhamRequest sanPhamRequest) {
        sanPhamService.updateSanPham(id, sanPhamRequest);
        return "redirect:/HienThi/GetAll";
    }


    @GetMapping("/listSPham/detail/id/{id}")
    private String detail(@PathVariable Long  id,Model model){

        SanPham sanPham = sanPhamRepository.findById(id).get();
        model.addAttribute("detail",sanPham);
        model.addAttribute("listSPham",sanPhamRepository.findAll());
        model.addAttribute("listLSP",loaiSanPhamRepository.findAll());
        model.addAttribute("listMauSac",mauSacRepository.findAll());
        model.addAttribute("listKichCo",kichCoRepository.findAll());
        return "SanPham/HienThiSP/Detail";
    }

    @PostMapping("/listSPham/delete/id/{id}")
    private String delete(@PathVariable("id") Long id) {
        sanPhamRepository.delete(sanPhamRepository.findById(id).get());
        return "redirect:/HienThi/GetAll";
    }


}
