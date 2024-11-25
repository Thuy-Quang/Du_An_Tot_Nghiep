package com.example.du_an_tot_nghiep.model;

import lombok.Data;

@Data
public class KichCoDTO {
    private Long id;
    private String tenKichCo;

    public KichCoDTO(Long id, String tenKichCo) {
    }
}
