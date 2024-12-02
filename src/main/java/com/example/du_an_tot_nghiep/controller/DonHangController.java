package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.*;
import com.example.du_an_tot_nghiep.model.DonHangDTO;
import com.example.du_an_tot_nghiep.model.DonHangReq;
import com.example.du_an_tot_nghiep.model.DonHangRequest;
import com.example.du_an_tot_nghiep.repository.*;
import com.example.du_an_tot_nghiep.service.DonHangService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/HienThiDonHang")
public class DonHangController {
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private DonHangRepository donHangRepository;
    @Autowired
    private DonHangService donHangService;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private MaGiamGiaKhachHangRepository maGiamGiaKhachHangRepository;

    // Hiển thị danh sách đơn hàng
    @GetMapping("/GetAll")
    public String showDH(Model model ) {
        model.addAttribute("listDonHang", donHangRepository.findAll());
        return "donhang/index"; // Trả về giao diện danh sách đơn hàng
    }
    @GetMapping("/magiamgia/khachhang/nguoiDungID/{id}")
    public String showMaGiamGiaKh(@PathVariable("id") Long nguoiDungID , Model model){
        System.out.println("NguoiDungID nhận được: " + nguoiDungID);
        List<MaGiamGiaKH> listMaGiamGiaKhachHang = maGiamGiaKhachHangRepository.findByNguoiDungId(nguoiDungID);
        if (listMaGiamGiaKhachHang != null) {
            listMaGiamGiaKhachHang.forEach(maGiamGiaKH -> {
                System.out.println("Mã giảm giá: " + maGiamGiaKH.getMaGiamGia());
                System.out.println("Trạng thái: " + maGiamGiaKH.getTrangThai());
                System.out.println("Người dùng: " + maGiamGiaKH.getNguoiDung().getHoTen());
                System.out.println("Phần trăm giảm: " + maGiamGiaKH.getMaGiamGia().getPhanTramGiam());
            });
        } else {
            System.out.println("Danh sách mã giảm giá trống.");
        }
        model.addAttribute("listMaGiamGiaKhachHang", listMaGiamGiaKhachHang);

        return "donhang/index"; // Trả về giao diện danh sách đơn hàng

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

            if (donHangReq.getSanPhamList() == null || donHangReq.getSanPhamList().isEmpty()) {
                return ResponseEntity.badRequest().body("Danh sách sản phẩm không được để trống.");
            }

            // Kiểm tra giá trị phương thức thanh toán
            String phuongThuc = donHangReq.getPhuongThucThanhToan();
            System.out.println("dữ liệu :"+phuongThuc);
            if (!phuongThuc.equalsIgnoreCase("Tiền Mặt") && !phuongThuc.equalsIgnoreCase("Chuyển Khoản")) {
                return ResponseEntity.badRequest().body("Phương thức thanh toán không hợp lệ. Chỉ chấp nhận 'TienMat' hoặc 'ChuyenKhoan'.");
            }

            // Tạo và lưu đối tượng DonHang
            DonHang donHang = new DonHang();
            donHang.setNguoiDung(
                    nguoiDungRepository.findById(donHangReq.getNguoiDungId())
                            .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại với ID: " + donHangReq.getNguoiDungId()))
            );
            donHang.setTrangThai("Chờ xác nhận");
            donHang.setTongTien(donHangReq.getTongTien());
            donHang.setPhuongThucThanhToan(phuongThuc);
            donHang.setTrangThaiThanhToan("chưa thanh toán ");
//            System.out.println("Đối tượng DonHang sau khi gán phương thức thanh toán: " + donHang);
            donHang.setNgayTao(new Date());
            donHang.setNgayCapNhat(new Date());

            List<ChiTietDonHang> chiTietDonHangs = donHangReq.getSanPhamList().stream().map(sp -> {
                ChiTietDonHang chiTiet = new ChiTietDonHang();
                SanPham sanPham = sanPhamRepository.findById(sp.getSanPhamId())
                        .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại với ID: " + sp.getSanPhamId()));
                chiTiet.setSanPham(sanPham);
                chiTiet.setSoLuong(sp.getSoLuong());
                chiTiet.setGiaDonVi(sp.getGiaDonVi());
                chiTiet.setDonHang(donHang);
                return chiTiet;
            }).toList();

            donHangService.luuDonHang(donHang, chiTietDonHangs);
//             Tạo phản hồi dưới dạng JSON
            Map<String, String> response = new HashMap<>();
            response.put("message", "Đơn hàng được lưu thành công.");
            return ResponseEntity.ok(response);


        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu đơn hàng: " + e.getMessage());
        }
    }



}
