package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.*;
import com.example.du_an_tot_nghiep.model.ChiTietDonHangDTO;
import com.example.du_an_tot_nghiep.model.DonHangDTO;
import com.example.du_an_tot_nghiep.model.DonHangReq;
import com.example.du_an_tot_nghiep.model.DonHangRequest;
import com.example.du_an_tot_nghiep.repository.*;
import com.example.du_an_tot_nghiep.service.ChiTietDonHangService;
import com.example.du_an_tot_nghiep.service.DonHangService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/HienThiDonHang")
public class DonHangController {
    @Autowired
    ChiTietDonHangService chiTietDonHangService;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private DonHangRepository donHangRepository;
    @Autowired
    private DonHangService donHangService;
    @Autowired
    ChiTietDonHangRepository chiTietDonHangRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private MaGiamGiaKhachHangRepository maGiamGiaKhachHangRepository;

    // Hiển thị danh sách đơn hàng
    @PreAuthorize("hasRole('Quản lý')")
    @PostMapping("/hoantat/{id}")
    public ResponseEntity<String> hoanTatDonHang(@PathVariable Long id) {
        try {
            String message = donHangService.hoanTatDonHang(id);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/trangthai/{id}")
    public ResponseEntity<?> getTrangThaiDonHang(@PathVariable Long id) {
        DonHang donHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
        return ResponseEntity.ok(Map.of("trangThai", donHang.getTrangThai()));
    }

    @PostMapping("/xacNhanDonHang/{donHangId}")
    public String confirmOrder(@PathVariable Long donHangId) throws MessagingException {
        // Xác nhận đơn hàng
        donHangService.confirmOrder(donHangId);

        // Sau khi xác nhận, chuyển hướng về trang danh sách đơn hàng
        return "redirect:/HienThiDonHang/GetAll";
    }
    @GetMapping("/GetAll")
    public String showDH(Model model ) {
        model.addAttribute("listDonHang", donHangRepository.findAll());
        return "donhang/index"; // Trả về giao diện danh sách đơn hàng
    }
    @GetMapping("/magiamgia/khachhang/nguoiDungID/{id}")
    public String showMaGiamGiaKh(@PathVariable("id") Long nguoiDungID , Model model){
        List<MaGiamGiaKhachHang> listMaGiamGiaKhachHang = maGiamGiaKhachHangRepository.findByNguoiDungId(nguoiDungID);
        model.addAttribute("listMaGiamGiaKhachHang", listMaGiamGiaKhachHang);
        return "donhang/magiamgia"; // Trả về giao diện danh sách đơn hàng

    }
    @GetMapping("/apDung/{donHangID}")
    public String apDungMaGiamGia(
            @RequestParam("idMaGiamGiaKhachHang") Long idMaGiamGiaKhachHang,
            @PathVariable Long donHangID,
            RedirectAttributes redirectAttributes, Model model) {

        // Kiểm tra đơn hàng có tồn tại không
        Optional<DonHang> donHangOpt = donHangRepository.findById(donHangID);
        if (donHangOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại!");
            return "redirect:/HienThiDonHang/GetAll";
        }

        DonHang donHang = donHangOpt.get();
        model.addAttribute("donHang", donHang); // Thêm donHang vào model để hiển thị thông tin nếu cần

        // Kiểm tra mã giảm giá có tồn tại không
        Optional<MaGiamGiaKhachHang> maGiamGiaOpt = maGiamGiaKhachHangRepository.findById(idMaGiamGiaKhachHang);
        if (maGiamGiaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Mã giảm giá không tồn tại!");
            return "redirect:/HienThiDonHang/GetAll";
        }

        MaGiamGiaKhachHang maGiamGiaKhachHang = maGiamGiaOpt.get();

        // Kiểm tra trạng thái mã giảm giá
        if ("Đã sử dụng".equals(maGiamGiaKhachHang.getTrangThai())) {
            redirectAttributes.addFlashAttribute("error", "Mã giảm giá đã được sử dụng!");
            return "redirect:/HienThiDonHang/GetAll";
        }

        // Lấy tổng tiền của đơn hàng
        double tongTien = donHang.getTongTien();

        // Áp dụng mã giảm giá
        double phanTramGiam = maGiamGiaKhachHang.getMaGiamGia().getPhanTramGiam();
        double tienGiam = tongTien * (phanTramGiam / 100);
        double tongTienSauKhiGiam = tongTien - tienGiam;

        // Đảm bảo tổng tiền không âm
        if (tongTienSauKhiGiam < 0) {
            tongTienSauKhiGiam = 0;
        }

        // Cập nhật tổng tiền và trạng thái đơn hàng
        donHang.setTongTien(tongTienSauKhiGiam);
        donHangRepository.save(donHang);

        // Cập nhật trạng thái mã giảm giá
        maGiamGiaKhachHang.setTrangThai("Đã sử dụng");
        maGiamGiaKhachHangRepository.save(maGiamGiaKhachHang);

        // Thêm thông báo thành công
        redirectAttributes.addFlashAttribute("success", "Áp dụng mã giảm giá thành công!");

        // Trả về trang danh sách hoặc chi tiết đơn hàng
        return "redirect:/HienThiDonHang/GetAll";
    }
    @GetMapping("/chiTiet/{id}")
    public String viewOrderDetail(@PathVariable Long id, Model model) {
        // Tìm đơn hàng theo id
        DonHang donHang = donHangRepository.findById(id).orElse(null);

        // Kiểm tra nếu đơn hàng không tồn tại
        if (donHang == null) {
            model.addAttribute("error", "Đơn hàng không tồn tại.");
            return "error"; // Chuyển đến trang lỗi (nếu cần)
        }
        Double tongTien = donHang.getChiTietDonHangs().stream()
                .mapToDouble(chiTiet -> chiTiet.getTongGia())
                .sum();
        model.addAttribute("tongTien", tongTien);
        // Truyền các sản phẩm trong đơn hàng vào model
        model.addAttribute("donHang", donHang);
        model.addAttribute("chiTietDonHangs", donHang.getChiTietDonHangs());
        return "/donhang/chitiet"; // Trả về trang chi tiết đơn hàng
    }

    // Hiển thị form thêm đơn hàng
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Lấy danh sách tất cả khách hàng
        List<NguoiDung> listNguoiDung = nguoiDungRepository.findAll();

        // Gửi danh sách khách hàng đến view
        model.addAttribute("listNguoiDung", listNguoiDung);
        model.addAttribute("donHangRequest", new DonHangRequest());
        return "donhang/add"; // Giao diện thêm đơn hàng
    }

    // Xử lý thêm đơn hàng
    @PostMapping("/create")
    public String createDonHang(@ModelAttribute DonHangRequest donHangRequest) {
        // Tạo một đối tượng DonHang mới từ donHangRequest
        DonHang donHang = new DonHang();
        donHang.setTrangThai(donHangRequest.getTrangThai());
        donHang.setTongTien(donHangRequest.getTongTien());
        donHang.setTrangThaiThanhToan(donHangRequest.getTrangThaiThanhToan());
        donHang.setPhuongThucThanhToan(donHangRequest.getPhuongThucThanhToan());
        donHang.setNguoiDung(nguoiDungRepository.findById(donHangRequest.getNguoiDungId()).orElse(null)); // Thêm người dùng vào đơn hàng

        // Chuyển đổi LocalDateTime sang Date
        donHang.setNgayTao(Timestamp.valueOf(LocalDateTime.now())); // Gán giá trị hiện tại cho ngayTao

        // Lưu đơn hàng vào database
        donHangRepository.save(donHang);

        return "redirect:/HienThiDonHang/GetAll"; // Chuyển hướng về danh sách đơn hàng
    }

    // Hiển thị form chỉnh sửa đơn hàng
    @GetMapping("/listDonHang/edit/id/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        DonHang donHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        DonHangRequest donHangRequest = new DonHangRequest();
        donHangRequest.setTrangThai(donHang.getTrangThai());
        donHangRequest.setTongTien(donHang.getTongTien());
        donHangRequest.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
        donHangRequest.setPhuongThucThanhToan(donHang.getPhuongThucThanhToan());
        donHangRequest.setNguoiDungId(donHang.getNguoiDung().getId());

        model.addAttribute("detail", donHang); // Thêm thông tin đơn hàng vào model
        model.addAttribute("donHangRequest", donHangRequest); // Thêm thông tin để chỉnh sửa
        model.addAttribute("listNguoiDung", nguoiDungRepository.findAll()); // Thêm danh sách người dùng

        return "donhang/update"; // Trả về giao diện cập nhật đơn hàng
    }

    // Xử lý cập nhật đơn hàng
    @PostMapping("/listDonHang/edit/id/{id}")
    public String updateDonHang(@PathVariable("id") Long id, @ModelAttribute DonHangRequest donHangRequest) {
        DonHang donHang = donHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        donHang.setTrangThai(donHangRequest.getTrangThai());
        donHang.setTongTien(donHangRequest.getTongTien());
        donHang.setTrangThaiThanhToan(donHangRequest.getTrangThaiThanhToan());
        donHang.setPhuongThucThanhToan(donHangRequest.getPhuongThucThanhToan());
        donHang.setNguoiDung(nguoiDungRepository.findById(donHangRequest.getNguoiDungId()).orElse(null));

        donHangRepository.save(donHang); // Lưu cập nhật
        return "redirect:/HienThiDonHang/GetAll"; // Chuyển hướng về danh sách đơn hàng
    }

    // Xóa đơn hàng
    @PostMapping("/listDonHang/delete/id/{id}")
    public String deleteDonHang(@PathVariable("id") Long id) {
        donHangRepository.deleteById(id); // Xóa đơn hàng
        return "redirect:/HienThiDonHang/GetAll"; // Chuyển hướng về danh sách đơn hàng
    }
    @GetMapping("/danhsachcuakhachhang")
    public ResponseEntity<List<DonHangDTO>> getCustomerOrders(@RequestParam("nguoiDungID") Long nguoiDungID) {
        List<DonHang> orders = donHangService.getOrdersByCustomerId(nguoiDungID);

        if (orders == null || orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Chuyển đổi từ DonHang thành DonHangDTO
        List<DonHangDTO> orderDTOs = orders.stream()
                .map(order -> new DonHangDTO(
                        order.getId(),
                        order.getNguoiDung().getId(),
                        order.getNguoiDung().getHoTen(),
                        order.getNguoiDung().getDiaChi()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderDTOs);
    }
    @PostMapping("/dat-hang")
    public ResponseEntity<?> datHang(@RequestBody DonHangReq donHangReq) {
        try {
            // Kiểm tra tham số đầu vào
            if (donHangReq.getNguoiDungId() == null) {
                return ResponseEntity.badRequest().body("ID người dùng không được để trống.");
            }
            NguoiDung nguoiDung = nguoiDungRepository.findById(donHangReq.getNguoiDungId())
                    .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại với ID: " + donHangReq.getNguoiDungId()));

            nguoiDung.setHoTen(donHangReq.getHoTen());
            nguoiDung.setSoDienThoai(donHangReq.getSoDienThoai());
            nguoiDung.setEmail(donHangReq.getEmail());
            nguoiDung.setDiaChi(donHangReq.getDiaChi());
            nguoiDungRepository.save(nguoiDung); // Lưu lại người dùng
            if (donHangReq.getSanPhamList() == null || donHangReq.getSanPhamList().isEmpty()) {
                return ResponseEntity.badRequest().body("Danh sách sản phẩm không được để trống.");
            }

            // Kiểm tra giá trị phương thức thanh toán
            String phuongThuc = donHangReq.getPhuongThucThanhToan();
            if (!phuongThuc.equalsIgnoreCase("Tiền Mặt") && !phuongThuc.equalsIgnoreCase("Chuyển Khoản")) {
                return ResponseEntity.badRequest().body("Phương thức thanh toán không hợp lệ. Chỉ chấp nhận 'Tiền Mặt' hoặc 'Chuyển Khoản'.");
            }

            // Tạo đối tượng DonHang
            DonHang donHang = new DonHang();
            donHang.setNguoiDung(
                    nguoiDungRepository.findById(donHangReq.getNguoiDungId())
                            .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại với ID: " + donHangReq.getNguoiDungId()))
            );
            donHang.setTrangThai("Chờ xác nhận");
            donHang.setPhuongThucThanhToan(phuongThuc);
            donHang.setTrangThaiThanhToan("Chưa thanh toán");
            donHang.setNgayTao(new Date());
            donHang.setNgayCapNhat(new Date());

            // Tính tổng tiền từ danh sách sản phẩm
            double tongTien = donHangReq.getSanPhamList().stream()
                    .mapToDouble(sp -> sp.getSoLuong() * sp.getGiaDonVi())
                    .sum();

            // Cập nhật tổng tiền cho đơn hàng trước khi lưu
            donHang.setTongTien(tongTien);

            // Lưu đơn hàng vào cơ sở dữ liệu
            DonHang savedDonHang = donHangRepository.save(donHang);

            // Xử lý danh sách sản phẩm chi tiết
            List<ChiTietDonHang> chiTietDonHangs = donHangReq.getSanPhamList().stream().map(sp -> {
                ChiTietDonHang chiTiet = new ChiTietDonHang();
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(sp.getSanPhamChiTietId())
                        .orElseThrow(() -> new EntityNotFoundException("Sản phẩm chi tiết không tồn tại với ID: " + sp.getSanPhamChiTietId()));
                chiTiet.setSanPhamChiTiet(sanPhamChiTiet);
                chiTiet.setSoLuong(sp.getSoLuong());
                chiTiet.setGiaDonVi(sp.getGiaDonVi());
                chiTiet.setDonHang(savedDonHang);  // Sử dụng đối tượng đã lưu
                return chiTiet;
            }).collect(Collectors.toList());

            // Lưu danh sách chi tiết đơn hàng
            chiTietDonHangRepository.saveAll(chiTietDonHangs);

            // Tính lại tổng tiền của đơn hàng sau khi lưu chi tiết (nếu cần)
            double tongTienSauKhiLuu = chiTietDonHangs.stream()
                    .mapToDouble(chiTiet -> chiTiet.getSoLuong() * chiTiet.getGiaDonVi())
                    .sum();

            // Cập nhật lại tổng tiền của đơn hàng
            savedDonHang.setTongTien(tongTienSauKhiLuu);

            // Lưu lại đơn hàng với tổng tiền đã cập nhật
            donHangRepository.save(savedDonHang);

            // Tạo phản hồi
            Map<String, String> response = new HashMap<>();
            response.put("message", "Đơn hàng được lưu thành công.");
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu đơn hàng: " + e.getMessage());
        }
    }

    @GetMapping("/chi-tiet-san-pham/{id}")
    public ResponseEntity<?> getChiTietDonHang(@PathVariable Long id) {
        try {
            // Lấy đơn hàng theo id
            DonHang donHang = donHangService.getDonHangById(id);

            // Kiểm tra xem đơn hàng có tồn tại không
            if (donHang == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Đơn hàng không tồn tại.");
            }

            // Chuyển đổi danh sách ChiTietDonHang thành ChiTietDonHangDTO
            List<ChiTietDonHangDTO> chiTietDonHangDTOs = donHang.getChiTietDonHangs().stream()
                    .map(chiTiet -> new ChiTietDonHangDTO(
                            chiTiet.getSanPham().getTenSanPham(),
                            chiTiet.getSanPhamChiTiet().getMauSac().getTenMau(),
                            chiTiet.getSanPhamChiTiet().getKichCo().getTenKichCo(),
                            chiTiet.getSoLuong(),
                            chiTiet.getGiaDonVi(),
                            chiTiet.getSoLuong() * chiTiet.getGiaDonVi())) // tính tổng giá
                    .collect(Collectors.toList());

            // Trả về danh sách ChiTietDonHangDTO dưới dạng JSON
            return ResponseEntity.ok(chiTietDonHangDTOs);
        } catch (Exception e) {
            // In lỗi chi tiết nếu có
            e.printStackTrace();

            // Trả về lỗi 500 nếu có vấn đề trong khi xử lý
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lấy chi tiết đơn hàng.");
        }
    }
    @PostMapping("/huy/{id}")
    public String huyDonHang(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        try {
            // Tìm đơn hàng theo ID
            Optional<DonHang> optionalDonHang = donHangRepository.findById(id);
            if (!optionalDonHang.isPresent()) {
                model.addAttribute("error", "Không tìm thấy đơn hàng với ID: " + id);
                return "giohang/giohang"; // Load lại trang chi tiết giỏ hàng
            }

            DonHang donHang = optionalDonHang.get();

            // Kiểm tra trạng thái
            if ("Đang vận chuyển".equalsIgnoreCase(donHang.getTrangThai()) || "Đã huỷ".equalsIgnoreCase(donHang.getTrangThai())) {
                model.addAttribute("error", "Đơn hàng đang vận chuyển, không thể huỷ.");
                return "giohang/thatbai"; // Load lại trang chi tiết giỏ hàng
            }

            // Cập nhật trạng thái thành "Đã huỷ"
            donHang.setTrangThai("Đã huỷ");
            donHangRepository.save(donHang);

            // Thêm thông báo thành công vào Flash Attributes
            redirectAttributes.addFlashAttribute("message", "Đơn hàng đã được huỷ thành công.");
            return "giohang/dahuy"; // Chuyển hướng đến danh sách đơn hàng
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi trong quá trình huỷ đơn hàng.");
            return "giohang/giohang"; // Load lại trang chi tiết giỏ hàng
        }
    }

}
