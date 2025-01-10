package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.KichCo;
import com.example.du_an_tot_nghiep.entity.MauSac;
import com.example.du_an_tot_nghiep.entity.SanPham;
import com.example.du_an_tot_nghiep.entity.SanPhamChiTiet;
import com.example.du_an_tot_nghiep.model.ChiTietSanPhamDTO;
import com.example.du_an_tot_nghiep.model.SanPhamDTO;
import com.example.du_an_tot_nghiep.model.SanPhamRequest;
import com.example.du_an_tot_nghiep.repository.*;
import com.example.du_an_tot_nghiep.service.KichCoService;
import com.example.du_an_tot_nghiep.service.MauSacService;
import com.example.du_an_tot_nghiep.service.SanPhamChiTietService;
import com.example.du_an_tot_nghiep.service.SanPhamService;
import jakarta.validation.constraints.Size;
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

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/HienThi")
public class SanPhamController {


    @Autowired
    SanPhamRepository sanPhamRepository;
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

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

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @GetMapping("/sanpham/{id}")
    public String hienThiChiTietSanPham(@PathVariable Long id, Model model) {
        try {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.getSanPhamChiTietBySanPhamId(id);
            List<MauSac> danhSachMauSac = sanPhamChiTietService.getMauSacBySanPhamId(id);
            List<KichCo> danhSachKichCo = sanPhamChiTietService.getKichCoBySanPhamId(id);
            if (sanPhamChiTiet == null) {
                throw new IllegalArgumentException("Không tìm thấy sản phẩm chi tiết với id: " + id);
            }
            model.addAttribute("danhSachMauSac", danhSachMauSac);
            model.addAttribute("danhSachKichCo", danhSachKichCo);
            model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);
            return "sanphamchitiet/hienthi"; // Tên template
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Đã xảy ra lỗi khi lấy chi tiết sản phẩm");
            return "error";
        }
    }

    @GetMapping("/{sanPhamId}/kichco/{mauSacId}")
    public ResponseEntity<List<KichCo>> layKichCoTheoMauSac(
            @PathVariable Long sanPhamId,
            @PathVariable Long mauSacId) {
        try {
            List<KichCo> danhSachKichCo = sanPhamChiTietService.getKichCoByMauSac(sanPhamId, mauSacId);
            if (danhSachKichCo == null || danhSachKichCo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
            }
            return ResponseEntity.ok(danhSachKichCo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @RequestMapping("/index")
    public String index(){
        return "layout/index";
    }


    //Nam
    @RequestMapping("/AoNam")
    public String getSanPhamsByLoaiSanPhamId0(Model model,
                                              @RequestParam(value = "price", defaultValue = "") String[] price,
                                              @RequestParam(defaultValue = "") String searchQuery) {
        // Lấy sản phẩm theo loại
        // Lấy sản phẩm theo loại
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(1L));

        // Lọc theo giá (nếu có)
        if (price.length > 0) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> kiemTraGia(sp, price))
                    .collect(Collectors.toList());
        }

        // Lọc theo từ khóa tìm kiếm
        if (!searchQuery.isEmpty()) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> sp.getTenSanPham().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Thêm dữ liệu vào model
        model.addAttribute("lists", sanPhams);
        model.addAttribute("price", price);  // Để giữ lại bộ lọc giá
        model.addAttribute("searchQuery", searchQuery);  // Giữ lại từ khóa tìm kiếm
        model.addAttribute("currentPath", "/HienThi/AoNam");  // Đường dẫn hiện tại
        return "San_Pham/QuanAoNam/Nam/AoNam";
    }

    @RequestMapping("/QuanNam")
    public String getSanPhamsByLoaiSanPhamId1(Model model,
                                              @RequestParam(value = "price", defaultValue = "") String[] price,
                                              @RequestParam(defaultValue = "") String searchQuery) {
        // Lấy sản phẩm theo loại
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(2L));

        // Lọc theo giá (nếu có)
        if (price.length > 0) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> kiemTraGia(sp, price))
                    .collect(Collectors.toList());
        }

        // Lọc theo từ khóa tìm kiếm
        if (!searchQuery.isEmpty()) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> sp.getTenSanPham().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Thêm dữ liệu vào model
        model.addAttribute("lists", sanPhams);
        model.addAttribute("price", price);  // Để giữ lại bộ lọc giá
        model.addAttribute("searchQuery", searchQuery);  // Giữ lại từ khóa tìm kiếm
        model.addAttribute("currentPath", "/HienThi/QuanNam");  // Đường dẫn hiện tại

        return "San_Pham/QuanAoNam/Nam/QuanNam";  // Tên view
    }
    @RequestMapping("/AoNamAll")
    public String getSanPhamsByLoaiSanPhamId2(Model model,
                                              @RequestParam(value = "price", defaultValue = "") String[] price,
                                              @RequestParam(defaultValue = "") String searchQuery) {
        // Lấy sản phẩm theo loại
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(1L, 2L));

        // Lọc theo giá (nếu có)
        if (price.length > 0) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> kiemTraGia(sp, price))
                    .collect(Collectors.toList());
        }

        // Lọc theo từ khóa tìm kiếm
        if (!searchQuery.isEmpty()) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> sp.getTenSanPham().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Thêm dữ liệu vào model
        model.addAttribute("lists", sanPhams);
        model.addAttribute("price", price);  // Để giữ lại bộ lọc giá
        model.addAttribute("searchQuery", searchQuery);  // Giữ lại từ khóa tìm kiếm
        model.addAttribute("currentPath", "/HienThi/AoNamAll");  // Đường dẫn hiện tại

        return "San_Pham/QuanAoNam/Nam/All";  // Tên view
    }
    @RequestMapping("/AoNu")
    public String getSanPhamsByLoaiSanPhamId3(Model model,
                                              @RequestParam(value = "price", defaultValue = "") String[] price,
                                              @RequestParam(defaultValue = "") String searchQuery) {
        // Lấy sản phẩm theo loại
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(3L));

        // Lọc theo giá (nếu có)
        if (price.length > 0) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> kiemTraGia(sp, price))
                    .collect(Collectors.toList());
        }

        // Lọc theo từ khóa tìm kiếm
        if (!searchQuery.isEmpty()) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> sp.getTenSanPham().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Thêm dữ liệu vào model
        model.addAttribute("lists", sanPhams);
        model.addAttribute("price", price);  // Để giữ lại bộ lọc giá
        model.addAttribute("searchQuery", searchQuery);  // Giữ lại từ khóa tìm kiếm
        model.addAttribute("currentPath", "/HienThi/AoNu");  // Đường dẫn hiện tại

        return "San_Pham/QuanAoNam/Nu/AoNu";  // Tên view
    }

    @RequestMapping("/QuanNu")
    public String getSanPhamsByLoaiSanPhamId4(Model model,
                                              @RequestParam(value = "price", defaultValue = "") String[] price,
                                              @RequestParam(defaultValue = "") String searchQuery) {
        // Lấy sản phẩm theo loại
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(4L));

        // Lọc theo giá (nếu có)
        if (price.length > 0) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> kiemTraGia(sp, price))
                    .collect(Collectors.toList());
        }

        // Lọc theo từ khóa tìm kiếm
        if (!searchQuery.isEmpty()) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> sp.getTenSanPham().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Thêm dữ liệu vào model
        model.addAttribute("lists", sanPhams);
        model.addAttribute("price", price);  // Để giữ lại bộ lọc giá
        model.addAttribute("searchQuery", searchQuery);  // Giữ lại từ khóa tìm kiếm
        model.addAttribute("currentPath", "/HienThi/QuanNu");  // Đường dẫn hiện tại

        return "San_Pham/QuanAoNam/Nu/QuanNu";  // Tên view
    }

    @RequestMapping("/TatCa")
    public String getSanPhamsByLoaiSanPhamId5(Model model,
                                              @RequestParam(value = "price", defaultValue = "") String[] price,
                                              @RequestParam(defaultValue = "") String searchQuery) {
        // Lấy sản phẩm theo loại
        List<SanPham> sanPhams = sanPhamRepository.findByLoaiSanPhamIdIn(Arrays.asList(3L, 4L));

        // Lọc theo giá (nếu có)
        if (price.length > 0) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> kiemTraGia(sp, price))
                    .collect(Collectors.toList());
        }

        // Lọc theo từ khóa tìm kiếm
        if (!searchQuery.isEmpty()) {
            sanPhams = sanPhams.stream()
                    .filter(sp -> sp.getTenSanPham().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Thêm dữ liệu vào model
        model.addAttribute("lists", sanPhams);
        model.addAttribute("price", price);  // Để giữ lại bộ lọc giá
        model.addAttribute("searchQuery", searchQuery);  // Giữ lại từ khóa tìm kiếm
        model.addAttribute("currentPath", "/HienThi/TatCa");  // Đường dẫn hiện tại

        return "San_Pham/QuanAoNam/Nu/TatCa";  // Tên view
    }


    @GetMapping("/giohang")
    public String hienThiDanhSachSanPham(Model model,
                                         @RequestParam(defaultValue = "") String searchQuery,
                                         @RequestParam(value = "price", defaultValue = "") String[] price) {
        // Lấy tất cả sản phẩm
        List<SanPham> sanPhams = sanPhamService.getall();

        // Lọc theo giá nếu có
        if (price.length > 0) {
            sanPhams = sanPhams.stream()
                    .filter(sanPham -> {
                        double productPrice = sanPham.getGia();  // Lấy giá của sản phẩm
                        for (String priceRange : price) {
                            String[] range = priceRange.split("-");

                            // Xử lý cho các dải giá
                            if (range.length == 2) {
                                double minPrice = Double.parseDouble(range[0]);
                                double maxPrice = Double.parseDouble(range[1]);
                                if (productPrice >= minPrice && productPrice <= maxPrice) {
                                    return true;
                                }
                            } else if (range[0].equals("2000000+")) {
                                // Xử lý cho giá trên 2000
                                if (productPrice > 2000000) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        // Lấy tất cả màu sắc và kích cỡ để hiển thị trong form lọc
        List<MauSac> mauSacs = mauSacRepository.findAll();

        // Thêm dữ liệu vào model
        model.addAttribute("sanPhams", sanPhams);  // Danh sách sản phẩm sau khi lọc
        model.addAttribute("mauSacs", mauSacs);    // Danh sách màu sắc để hiển thị
        model.addAttribute("searchQuery", searchQuery);  // Từ khóa tìm kiếm
        model.addAttribute("price", price);         // Bộ lọc giá

        return "giohang/giohang";  // Trả về trang giỏ hàng
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
//        sanPhamService.addSanPham(sanPhamRequest);
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
//        sanPhamService.updateSanPham(id, sanPhamRequest);
        return "redirect:/HienThi/GetAll";
    }

    @PostMapping("/listSPham/delete/id/{id}")
    private String delete(@PathVariable("id") Long id) {
        sanPhamRepository.delete(sanPhamRepository.findById(id).get());
        return "redirect:/HienThi/GetAll";
    }
    @GetMapping("/getone/{id}")
    public ResponseEntity<?> getOne(
            @PathVariable("id") Long id,
            @RequestParam("color") Long colorId,
            @RequestParam("size") Long sizeId
    ) {
        Optional<SanPhamChiTiet> optionalSanPhamChiTiet = sanPhamChiTietRepository.findBySanPhamIdAndMauSacIdAndKichCoId(id, colorId, sizeId);

        if (optionalSanPhamChiTiet.isPresent()) {
            SanPhamChiTiet sanPhamChiTiet = optionalSanPhamChiTiet.get();

            // Chuyển đổi sang DTO và trả về chi tiết sản phẩm
            ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO();
            dto.setId(sanPhamChiTiet.getId());
            dto.setTenSanPham(sanPhamChiTiet.getSanPham().getTenSanPham());
            dto.setGia(sanPhamChiTiet.getSanPham().getGia());
            dto.setHinhAnh(sanPhamChiTiet.getSanPham().getHinhAnh());
            dto.setMauSac(sanPhamChiTiet.getMauSac().getTenMau());
            dto.setKichCo(sanPhamChiTiet.getKichCo().getTenKichCo());
            dto.setSoLuong(sanPhamChiTiet.getSoLuong());
            dto.setTrangThai(sanPhamChiTiet.getTrangThai());
            dto.setMoTa(sanPhamChiTiet.getSanPham().getMoTa()); // Thêm mô tả chi tiết sản phẩm
            dto.setNgayTao(sanPhamChiTiet.getNgayTao());
            dto.setNgayCapNhat(sanPhamChiTiet.getNgayCapNhat());

            return ResponseEntity.ok(dto); // Trả về chi tiết sản phẩm
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy sản phẩm chi tiết với các tham số yêu cầu.");
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



    @GetMapping("/colors/{colorId}")
    public ResponseEntity<?> getColor(@PathVariable Long colorId) {
        Optional<MauSac> colorOptional = mauSacRepository.findById(colorId);

        if (colorOptional.isPresent()) {
            return ResponseEntity.ok(colorOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Color not found");
        }
    }

    // API để lấy tên kích cỡ theo sizeId
    @GetMapping("/sizes/{sizeId}")
    public ResponseEntity<?> getSize(@PathVariable Long sizeId) {
        Optional<KichCo> sizeOptional = kichCoRepository.findById(sizeId);

        if (sizeOptional.isPresent()) {
            return ResponseEntity.ok(sizeOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Size not found");
        }
    }
    public boolean kiemTraGia(SanPham sanPham, String[] priceRanges) {
        double gia = sanPham.getGia();
        for (String range : priceRanges) {
            String[] parts = range.split("-");
            if (parts.length == 2) {
                double min = Double.parseDouble(parts[0]);
                double max = Double.parseDouble(parts[1]);
                if (gia >= min && gia <= max) return true;
            } else if (range.equals("2000000+")) {
                if (gia > 2000000) return true;
            }
        }
        return false;
    }
}
