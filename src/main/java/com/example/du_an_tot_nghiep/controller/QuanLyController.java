package com.example.du_an_tot_nghiep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuanLyController {
    @GetMapping("/quan-ly")
    public String quanly(){
        return "giaodienquanly/giaodienquanly";
    }

}
