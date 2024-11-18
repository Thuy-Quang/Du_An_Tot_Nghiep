//package com.example.du_an_tot_nghiep.controller;
//
//import com.example.du_an_tot_nghiep.entity.ChiTietDonHang;
//import com.example.du_an_tot_nghiep.entity.DonHang;
//import com.example.du_an_tot_nghiep.repository.ChiTietDonHangRepository;
//import com.example.du_an_tot_nghiep.repository.DonHangRepository;
//import com.example.du_an_tot_nghiep.service.SanPhamService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.Date;
//
//@Controller
//    @RequestMapping("/checkout")
//    public class CheckoutController {
//
//        @Autowired
//        private SanPhamService sanPhamService;
//        @Autowired
//        private ChiTietDonHangRepository chiTietDonHangRepository;
//        @Autowired
//        private DonHangRepository donHangRepository;
//
//        @PostMapping
//        public String processCheckout(
//                @RequestParam("productId") Long sanPhamId,
//                @RequestParam("quantity") int soLuong,
//                Model model) {
//
//            SanPham sanPham = sanPhamService.getById(sanPhamId);
//
//            if (sanPham == null) {
//                model.addAttribute("error", "Sản phẩm không tồn tại.");
//                return "redirect:/product-list";
//            }
//
//            if (sanPham.getSoLuong() < soLuong) {
//                model.addAttribute("error", "Số lượng sản phẩm không đủ.");
//                return "redirect:/product-list";
//            }
//
//            // Trừ số lượng sản phẩm trong kho
//            sanPham.setSoLuong(sanPham.getSoLuong() - soLuong);
//            sanPhamService.save(sanPham);
//
//            // Thêm sản phẩm và số lượng vào Model để hiển thị ở trang thanh toán
//            model.addAttribute("checkoutItem", sanPham);
//            model.addAttribute("quantity", soLuong);
//
//            return "MuaNgay/muangay"; // Trả về trang thanh toán
//        }
//    @PostMapping("/confirm")
//    public String confirmCheckout(@RequestParam("productId") Long sanPhamId,
//                                  @RequestParam("quantity") int soLuong,
//                                  Model model) {
//
//        // Logic lưu hóa đơn vào cơ sở dữ liệu
//        DonHang donHang = new DonHang();
//        donHang.setNgayTao(new Date());
//        donHang.setTongTien(sanPhamService.getById(sanPhamId).getGia() * soLuong);
//        donHangRepository.save(donHang);
//
//        ChiTietDonHang chiTietDonHang = new ChiTietDonHang();
//        chiTietDonHang.setDonHang(donHang);
//        chiTietDonHang.setSanPham(sanPhamService.getById(sanPhamId));
//        chiTietDonHang.setSoLuong(soLuong);
//        chiTietDonHang.setGiaDonVi(sanPhamService.getById(sanPhamId).getGia());
//        chiTietDonHangRepository.save(chiTietDonHang);
//
//        return "redirect:/MuaNgay/muangay"; // Chuyển hướng đến trang cảm ơn hoặc trang khác
//    }
//
//}
//
