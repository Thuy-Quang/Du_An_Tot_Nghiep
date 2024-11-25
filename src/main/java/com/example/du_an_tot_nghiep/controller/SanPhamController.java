package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.KichCo;
import com.example.du_an_tot_nghiep.entity.MauSac;
import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.model.SanPhamDTO;
import com.example.du_an_tot_nghiep.model.SanPhamRequest;
import com.example.du_an_tot_nghiep.repository.KichCoRepository;
import com.example.du_an_tot_nghiep.repository.LoaiSanPhamRepository;
import com.example.du_an_tot_nghiep.repository.MauSacRepository;
import com.example.du_an_tot_nghiep.repository.SanPhamRepository;
import com.example.du_an_tot_nghiep.service.KichCoService;
import com.example.du_an_tot_nghiep.service.MauSacService;
import com.example.du_an_tot_nghiep.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/HienThi")
public class SanPhamController {

    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    KichCoService kichCoService;
    @Autowired
    MauSacRepository mauSacRepository;
    @Autowired
    KichCoRepository kichCoRepository;

    @Autowired
    MauSacService mauSacService;

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





    @GetMapping("/GetAll")
    public String listSanPham(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SanPham> sanPhamPage;

        if (keyword.isEmpty()) {
            sanPhamPage = sanPhamRepository.findAllByOrderByNgayTaoDesc(pageable);
        } else {
            sanPhamPage = sanPhamRepository.findByTenSanPhamContainingOrderByNgayTaoDesc(keyword, pageable);
        }

        model.addAttribute("listSPham", sanPhamPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sanPhamPage.getTotalPages());
        model.addAttribute("size", size); // Truyền biến size vào model
        model.addAttribute("listLSP", loaiSanPhamRepository.findAll());
        model.addAttribute("listMauSac", mauSacRepository.findAll());
        model.addAttribute("listKichCo", kichCoRepository.findAll());
        model.addAttribute("keyword", keyword);

        return "SanPham/HienThiSP/index";
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
        Optional<SanPham> optionalSanPham = sanPhamRepository.findById(id);
        if (optionalSanPham.isPresent()) {
            SanPham sanPham = optionalSanPham.get();
            System.out.println("Trạng thái hiện tại của sản phẩm: " + sanPham.getTrangThai());
            model.addAttribute("detail", sanPham);
        } else {
            throw new RuntimeException("Product not found");
        }
        model.addAttribute("listLSP", loaiSanPhamRepository.findAll());
        model.addAttribute("listMauSac", mauSacRepository.findAll());
        model.addAttribute("listKichCo", kichCoRepository.findAll());
        return "SanPham/HienThiSP/Update";
    }

    @PostMapping("/listSPham/edit/id/{id}")
    private String putSanPham(@PathVariable Long id, SanPhamRequest sanPhamRequest) {
        System.out.println("Updating product with ID: " + id);
        System.out.println("Trạng Thái received: " + sanPhamRequest.getTrangThai());
        sanPhamService.updateSanPham(id, sanPhamRequest);
        return "redirect:/HienThi/GetAll";
    }

    @PostMapping("/listSPham/delete/id/{id}")
    private String delete(@PathVariable("id") Long id) {
        sanPhamRepository.delete(sanPhamRepository.findById(id).get());
        return "redirect:/HienThi/GetAll";
    }
    @GetMapping("/getone/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        Optional<SanPham> optionalSanPham = sanPhamRepository.findById(id);

        if (optionalSanPham.isPresent()) {
            SanPham sanPham = optionalSanPham.get();

            // Chuyển đổi sang DTO
            SanPhamDTO dto = new SanPhamDTO();
            dto.setId(sanPham.getId());
            dto.setTenSanPham(sanPham.getTenSanPham());
            dto.setGia(sanPham.getGia());
            dto.setMoTa(sanPham.getMoTa());
            dto.setHinhAnh(sanPham.getHinhAnh());

            return ResponseEntity.ok(dto); // Trả về DTO
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy sản phẩm với id: " + id);
        }
    }
    @GetMapping("/mau-sac")
    public ResponseEntity<List<MauSac>> getAllColors() {
        List<MauSac> list = mauSacService.getAllColors();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 nếu danh sách rỗng
        }
        return ResponseEntity.ok(list); // 200 trả về danh sách
    }

    @GetMapping("/kich-co")
    public ResponseEntity<List<KichCo>> getAllSizes() {
        List<KichCo> list = kichCoService.getAllSizes();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // Trả về HTTP 204 nếu danh sách rỗng
        }
        return ResponseEntity.ok(list); // Trả về HTTP 200 với dữ liệu JSON
    }





}
