package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.*;
import com.example.du_an_tot_nghiep.model.DonHangReq;
import com.example.du_an_tot_nghiep.model.ThanhToanRequest;
import com.example.du_an_tot_nghiep.repository.*;
import com.example.du_an_tot_nghiep.service.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/bhtaiquay")
public class BHTaiQuayController {

    @Autowired
    private MaGiamGiaKhachHangRepository maGiamGiaKhachHangRepository;
    @Autowired
    private MaGiamGiaKHService maGiamGiaKHService;
    @Autowired
    private MaGiamGiaService maGiamGiaService;
    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private ChiTietDonHangService chiTietDonHangService;

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private BanHangTaiQuayService banHangTaiQuayService;


    // Hiển thị trang bán hàng tại quầy
    @GetMapping
    public String showPage(
            @RequestParam(defaultValue = "0") int pageSanPham,
            @RequestParam(defaultValue = "0") int pageDonHang,
            @RequestParam(defaultValue = "0") int pageChiTiet,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Long idDonHangDuocChon,
            Model model) {
        try {
            // Lấy danh sách khách hàng từ service
            List<NguoiDung> danhSachKhachHang = nguoiDungService.findAll();
            model.addAttribute("listKhachHang", danhSachKhachHang);

            // Lấy danh sách đơn hàng và sắp xếp giảm dần theo ngày tạo
            Page<DonHang> donHangPage = donHangRepository.findAllByOrderByNgayTaoDesc(PageRequest.of(pageDonHang, size));
            Page<ChiTietDonHang> chiTietDonHangPage = chiTietDonHangService.getAll(PageRequest.of(pageChiTiet, size));
            model.addAttribute("listDonHang", donHangPage.getContent()); // Danh sách đơn hàng trên trang hiện tại
            model.addAttribute("currentPageDonHang", pageDonHang); // Trang hiện tại
            model.addAttribute("totalPagesDonHang", donHangPage.getTotalPages()); // Tổng số trang

            // Tải toàn bộ dữ liệu trang
            Map<String, Object> danhSach = banHangTaiQuayService.loadManagementPage(pageSanPham, pageDonHang, pageChiTiet, size, idDonHangDuocChon);

            // Thêm đơn hàng được chọn vào Model
            if (idDonHangDuocChon != null) {
                DonHang donHangDuocChon = donHangService.findById(idDonHangDuocChon);
                banHangTaiQuayService.tinhTongTien(donHangDuocChon);
                model.addAttribute("donHangDuocChon", donHangDuocChon);
                model.addAttribute("hienThiModal", true); // Hiển thị modal khi có đơn hàng được chọn
            } else {
                model.addAttribute("donHangDuocChon", new DonHang());
                model.addAttribute("hienThiModal", false); // Không hiển thị modal khi không có đơn hàng được chọn
            }

            // Thêm danh sách vào model
            danhSach.forEach(model::addAttribute);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            e.printStackTrace();
        }
        return "banhang/index";
    }

    // Tạo đơ  hàng
    @PostMapping("/taoDonHang")
    public String taoDonHang(@RequestParam("nguoiDungId") Long nguoiDungId,
                             @RequestParam("phuongThucThanhToan") String phuongThucThanhToan,
                             RedirectAttributes redirectAttributes) {
        try {
            // Gọi service để tạo đơn hàng
            banHangTaiQuayService.taoDonHang(nguoiDungId, phuongThucThanhToan);

            // Thông báo thành công
            redirectAttributes.addFlashAttribute("message", "Đơn hàng đã được tạo thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi tạo đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/bhtaiquay"; // Chuyển hướng về trang bán hàng tại quầy
    }

    // thêm sản phẩm
    @PostMapping("/addSanPham")
    public String addSanPham(@RequestParam Long donHangId,
                             @RequestParam Long sanPhamId,
                             @RequestParam int soLuong,
                             RedirectAttributes redirectAttributes) {
        try {
            // Gọi service để thêm sản phẩm
            banHangTaiQuayService.addSPVaoDonHang(donHangId, sanPhamId, soLuong);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thêm vào đơn hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/bhtaiquay?idDonHangDuocChon=" + donHangId;
    }


    // Sửa đơn hàng trong danh sách đơn hàng
    @GetMapping("/editDonHang")
    public String editDonHang(@RequestParam Long donHangId, Model model) {
        try {
            // Lấy chi tiết đơn hàng
            DonHang donHang = donHangService.findById(donHangId);
            model.addAttribute("donHangDuocChon", donHang);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Đơn hàng không tồn tại: " + e.getMessage());
        }
        return "banhang/chitietdonhang"; // Trả về giao diện chứa modal
    }


    // sửa đơn hàng trong đơn hàng
    @PostMapping("/updateDonHang")
    @Transactional
    public String updateDonHang(
            @RequestParam Long donHangId,
            @RequestParam List<Long> sanPhamChiTietIds,
            @RequestParam List<Integer> soLuongs,
            RedirectAttributes redirectAttributes) {
        try {
            // Lặp qua các sản phẩm để cập nhật
            for (int i = 0; i < sanPhamChiTietIds.size(); i++) {
                banHangTaiQuayService.capNhatChiTiet(donHangId, sanPhamChiTietIds.get(i), soLuongs.get(i), false);
            }
            redirectAttributes.addFlashAttribute("message", "Cập nhật đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/bhtaiquay?idDonHangDuocChon=" + donHangId;
    }

    @PostMapping("/thanhToan")
    public String thanhToan(@RequestParam Long donHangId, Model model) {
        try {
            banHangTaiQuayService.thanhToanDonHang(donHangId);

            // Cập nhật danh sách đơn hàng để hiển thị trạng thái mới
            model.addAttribute("listDonHang", donHangRepository.findAll());
            return "redirect:/bhtaiquay";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Thanh toán thất bại: " + e.getMessage());

            // Trả về danh sách đơn hàng để hiển thị nếu có lỗi
            model.addAttribute("listDonHang", donHangRepository.findAll());
            return "banhang/index";
        }
    }

    // Xử lý logic bán hàng tại quầy
    @PostMapping("/ban-hang-tai-quay")
    @Transactional
    public ResponseEntity<?> banHangTaiQuay(@RequestBody DonHangReq donHangReq) {
        try {
            // Sử dụng dịch vụ để xử lý toàn bộ logic
            DonHang donHang = banHangTaiQuayService.taoDonHangTuYeuCau(donHangReq);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đơn hàng đã được tạo thành công.");
            response.put("donHangId", donHang.getId());
            response.put("tongTien", donHang.getTongTien());
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lỗi: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }


    @GetMapping("/banhangtaiquay")
    public String banhang(){
        return "banhang/banhang";
    }
    // API tạo đơn hàng mới
    @PostMapping("/tao-moi")
    public ResponseEntity<DonHang> taoDonHangMoi() {
        DonHang donHang = new DonHang();
        donHang.setNgayTao(new Date()); // Set ngày tạo mặc định
        donHang.setTrangThai(" Chờ thanh toán"); // Set trạng thái mặc định
        donHang.setTrangThaiThanhToan("Chưa thanh toán"); // Set trạng thái thanh toán mặc định

        // Lưu đơn hàng vào cơ sở dữ liệu
        DonHang savedDonHang = donHangRepository.save(donHang);

        return ResponseEntity.ok(savedDonHang);
    }
    @GetMapping("/chiTiet/{id}")
    public String viewOrderDetail(@PathVariable Long id, Model model) {
        // Tìm đơn hàng theo id
        DonHang donHang = donHangRepository.findById(id).orElse(null);
        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findAll();
        List<MaGiamGiaKhachHang> maGiamGiaKhachHangList = maGiamGiaKhachHangRepository.findByNguoiDungId(donHang.getNguoiDung().getId());

        // Kiểm tra nếu đơn hàng không tồn tại
        if (donHang == null) {
            model.addAttribute("error", "Đơn hàng không tồn tại.");
            return "error"; // Chuyển đến trang lỗi (nếu cần)
        }


        Double tamtinh = donHang.getChiTietDonHangs().stream()
                .mapToDouble(chiTiet -> chiTiet.getTongGia())
                .sum();
        Double giamGia = 0.0;
        Double tongTien = tamtinh - giamGia;
        model.addAttribute("tamtinh", tamtinh);
        model.addAttribute("giamGia", giamGia); // Giảm giá = 0 khi chưa áp dụng mã giảm giá
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("maGiamGiaKhachHangList", maGiamGiaKhachHangList);

        // Truyền các sản phẩm trong đơn hàng vào model
        model.addAttribute("donHang", donHang);
        model.addAttribute("sanPhamChiTiets", sanPhamChiTiets);
        model.addAttribute("chiTietDonHangs", donHang.getChiTietDonHangs());

        return "/banhang/detail"; // Trả về trang chi tiết đơn hàng
    }

    @PostMapping("/{donHangId}/them-san-pham")
    public ResponseEntity<String> themSanPhamVaoDonHang(
            @PathVariable Long donHangId,
            @RequestParam Long sanPhamChiTietId,
            @RequestParam Integer soLuong) {
        // Lấy đơn hàng
        Optional<DonHang> optionalDonHang = donHangRepository.findById(donHangId);
        if (optionalDonHang.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Đơn hàng không tồn tại!");
        }

        DonHang donHang = optionalDonHang.get();

        // Lấy sản phẩm chi tiết
        Optional<SanPhamChiTiet> optionalSanPhamChiTiet = sanPhamChiTietRepository.findById(sanPhamChiTietId);
        if (optionalSanPhamChiTiet.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sản phẩm không tồn tại!");
        }

        SanPhamChiTiet sanPhamChiTiet = optionalSanPhamChiTiet.get();

        // Kiểm tra số lượng tồn kho
        if (soLuong > sanPhamChiTiet.getSoLuong()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số lượng sản phẩm không đủ trong kho!");
        }

        // Tạo chi tiết đơn hàng
        ChiTietDonHang chiTietDonHang = new ChiTietDonHang();
        chiTietDonHang.setDonHang(donHang);
        chiTietDonHang.setSanPhamChiTiet(sanPhamChiTiet);
        chiTietDonHang.setSoLuong(soLuong);
        chiTietDonHang.setGiaDonVi(sanPhamChiTiet.getSanPham().getGia());

        // Cập nhật tồn kho sản phẩm
        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);

        // Lưu vào database
        chiTietDonHangRepository.save(chiTietDonHang);
        sanPhamChiTietRepository.save(sanPhamChiTiet);

        return ResponseEntity.ok("Thêm sản phẩm vào đơn hàng thành công!");
    }
    @PostMapping("/{donHangId}/xoa-san-pham")
    public ResponseEntity<String> xoaSanPham(@PathVariable Long donHangId, @RequestParam Long chiTietDonHangId) {
        try {
            boolean isDeleted = donHangService.xoaSanPhamKhoiDonHang(donHangId, chiTietDonHangId);
            if (isDeleted) {
                return ResponseEntity.ok("Sản phẩm đã được xóa khỏi đơn hàng.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể xóa sản phẩm.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi xảy ra: " + e.getMessage());
        }
    }
    @PostMapping("/{donHangId}/thanh-toan")
    public ResponseEntity<String> thanhToan(@PathVariable Long donHangId,@RequestBody Map<String, Double> payload) {
        Double tongTien = payload.get("tongTien");

        // Kiểm tra đơn hàng
        DonHang donHang = donHangService.findById(donHangId);
        if (donHang == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đơn hàng.");
        }

        // Kiểm tra trạng thái đơn hàng
        if (donHang.getTrangThai().equals("Hoàn tất")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đơn hàng này đã được thanh toán rồi.");
        }

        // Kiểm tra tổng tiền có hợp lệ không
        if (tongTien <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tổng tiền không hợp lệ.");
        }

        // Cập nhật tổng tiền vào đơn hàng
        donHang.setTongTien(tongTien);
        donHang.setTrangThaiThanhToan("Đã thanh toán");
        // Cập nhật trạng thái đơn hàng thành "Hoàn tất"
        donHang.setTrangThai("Hoàn tất");
        donHangService.save(donHang); // Lưu đơn hàng đã cập nhật trạng thái và tổng tiền

        // Cập nhật số lượng sản phẩm trong chi tiết đơn hàng
        for (ChiTietDonHang chiTiet : donHang.getChiTietDonHangs()) {
            if (chiTiet == null || chiTiet.getSanPhamChiTiet() == null) {
                continue; // Bỏ qua nếu chi tiết đơn hàng hoặc sản phẩm chi tiết không hợp lệ
            }

            SanPhamChiTiet sanPhamChiTiet = chiTiet.getSanPhamChiTiet();

            // Kiểm tra số lượng sản phẩm
            if (sanPhamChiTiet.getSoLuong() < chiTiet.getSoLuong()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số lượng sản phẩm không đủ.");
            }

            // Cập nhật số lượng sản phẩm
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - chiTiet.getSoLuong());
            sanPhamChiTietService.save(sanPhamChiTiet); // Lưu sản phẩm đã cập nhật số lượng
        }

        return ResponseEntity.ok("Thanh toán thành công.");
    }



    @PostMapping("/ap-dung")
    public ResponseEntity<?> apDungMaGiamGia(@RequestBody ApDungMaGiamGiaRequest request) {
        MaGiamGiaKhachHang mggKhachHang = maGiamGiaKhachHangRepository.findById(request.getMaGiamGiaId())
                .orElseThrow(() -> new RuntimeException("Mã giảm giá không tồn tại"));

        LocalDateTime now = LocalDateTime.now();

        // Kiểm tra trạng thái và ngày hết hạn
        if (mggKhachHang.getTrangThai().equalsIgnoreCase("Đã sử dụng")) {
            return ResponseEntity.badRequest().body("Mã giảm giá không khả dụng!");
        }

        if (mggKhachHang.getMaGiamGia().getNgayHetHan().isBefore(now)) {
            return ResponseEntity.badRequest().body("Mã giảm giá đã hết hạn!");
        }

        // Tính toán giảm giá
        Float giamGia = request.getTamTinh() * mggKhachHang.getMaGiamGia().getPhanTramGiam() / 100;

        // Cập nhật trạng thái mã giảm giá
        mggKhachHang.setTrangThai("Đã sử dụng");
        maGiamGiaKhachHangRepository.save(mggKhachHang);

        return ResponseEntity.ok(giamGia);
    }








}
