package com.example.du_an_tot_nghiep.controller;

import com.example.du_an_tot_nghiep.entity.NguoiDung;
import com.example.du_an_tot_nghiep.model.NguoiDungRequest;
import com.example.du_an_tot_nghiep.security.JwtUtil;
import com.example.du_an_tot_nghiep.service.NguoiDungService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/nguoi-dung")
public class NguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;
    @Autowired
    JwtUtil jwtUtil;

    // Hiển thị danh sách người dùng
    @GetMapping("/hienthi")
    public String listNguoiDung(@RequestParam(defaultValue = "0") int page, Model model) {
        int size = 5; // Số lượng người dùng trên mỗi trang
        Page<NguoiDung> nguoiDungPage = nguoiDungService.getAllNguoiDung(page, size);
        model.addAttribute("nguoiDungs", nguoiDungPage.getContent());
        model.addAttribute("totalPages", nguoiDungPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "NguoiDung/list";
    }

    @GetMapping("/thong-tin")
    public ResponseEntity<NguoiDungRequest> getUserInfo(@RequestHeader("Authorization") String token) {
        // Kiểm tra và trích xuất token từ header
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Trả về lỗi nếu token không hợp lệ
        }

        // Trích xuất username từ token
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);

        // Tìm người dùng từ username
        Optional<NguoiDung> user = nguoiDungService.findByUsername(username);

        // Kiểm tra nếu người dùng tồn tại
        if (user.isPresent()) {
            // Tạo đối tượng NguoiDungRequest từ đối tượng NguoiDung
            NguoiDungRequest userRequest = new NguoiDungRequest(user.get());
            return ResponseEntity.ok(userRequest);  // Trả về thông tin người dùng nếu tìm thấy
        } else {
            // Trả về lỗi nếu không tìm thấy người dùng
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // Trả về lỗi Unauthorized nếu không tìm thấy người dùng
        }
    }





    // Form thêm mới người dùng
    @GetMapping("/them")
    public String showAddForm(Model model) {
        model.addAttribute("nguoiDung", new NguoiDung());
        return "NguoiDung/add";
    }

    // Lưu người dùng mới với kiểm tra tính hợp lệ
    @PostMapping("/luu")
    public String saveNguoiDung(@Valid @ModelAttribute NguoiDung nguoiDung, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Nếu có lỗi, trả về form thêm người dùng và hiển thị lỗi
            model.addAttribute("nguoiDung", nguoiDung);
            return "NguoiDung/add";
        }
        nguoiDung.setNgayTao(new Date());
        nguoiDung.setNgayCapNhat(new Date());
        nguoiDungService.save(nguoiDung);
        return "redirect:/nguoi-dung";
    }

    // Form sửa thông tin người dùng
//    @GetMapping("/sua/{id}")
//    public String showUpdateForm(@PathVariable Long id, Model model) {
////        Optional<NguoiDung> nguoiDung = nguoiDungService.findById(id);
//        if (nguoiDung.isPresent()) {
//            model.addAttribute("nguoiDung", nguoiDung.get());
//            return "NguoiDung/edit";
//        }
//        return "redirect:/nguoi-dung";
//    }

    // Cập nhật người dùng với kiểm tra tính hợp lệ
    @PostMapping("/cap-nhat/{id}")
    public String updateNguoiDung(@PathVariable Long id, @Valid @ModelAttribute NguoiDung nguoiDung, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("nguoiDung", nguoiDung);
            return "NguoiDung/edit";
        }
        nguoiDung.setNgayCapNhat(new Date());
        nguoiDungService.save(nguoiDung);
        return "redirect:/nguoi-dung";
    }

    // Xóa người dùng
    @GetMapping("/xoa/{id}")
    public String deleteNguoiDung(@PathVariable Long id) {
        nguoiDungService.deleteById(id);
        return "redirect:/nguoi-dung";
    }

    // Xem chi tiết người dùng
    @GetMapping("/chi-tiet/{id}")
    public String viewDetail(@PathVariable Long id, Model model) {
        Optional<NguoiDung> nguoiDung = Optional.ofNullable(nguoiDungService.findById(id));
        if (nguoiDung.isPresent()) {
            model.addAttribute("nguoiDung", nguoiDung.get());
            return "NguoiDung/detail";
        }
        return "redirect:/nguoi-dung";
    }
}
