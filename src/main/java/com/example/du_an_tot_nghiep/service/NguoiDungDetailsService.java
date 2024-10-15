package com.example.du_an_tot_nghiep.service;

import com.example.du_an_tot_nghiep.entity.NguoiDung;
import com.example.du_an_tot_nghiep.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NguoiDungDetailsService implements UserDetailsService {


    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));

        return new org.springframework.security.core.userdetails.User(
                nguoiDung.getTenDangNhap(), nguoiDung.getMatKhau(), new ArrayList<>()
        );
    }
    public String addNguoiDung(String tenDangNhap ){
        NguoiDung nguoiDung = new NguoiDung();
        if (nguoiDung.getTenDangNhap().equals(tenDangNhap)){
            return "ngươ dùng đẫ tồn tại trong ĐB";
        }else {

            nguoiDungRepository.save(nguoiDung);
            return "thêm nguowuf dung thành công";
        }
    }
//    public NguoiDung

    //them moi nguoi dung
    //1 check tồn tại username trong db chua
    // neu toi tai thi bao loi la da ton tai
    // them vao db


    //check user co trong db hay khong
//    public String checkUserInDb(string username, string password){
//        NguoiDungDb;
//
//        if(nguoiDung.username == username && nguoiDung.password == password){
//            returrn username;
//        }
//    }
    public Boolean checkUserInDB(String tenDangNhap , String matKhau){
        var userOpt = nguoiDungRepository.findByTenDangNhap(tenDangNhap);
        if (userOpt.isPresent() && userOpt.get().getMatKhau().equals(matKhau)) {
            return true;
        }
        return false;

    }
}
