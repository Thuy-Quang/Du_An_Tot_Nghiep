package com.example.du_an_tot_nghiep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SanPhamController {
    @RequestMapping("/index")
    public String index(){
        return "layout/index";
    }

}
