package com.example.demo;

import com.example.demo.DAO.KPICategoryDAO;
import com.example.demo.Model.KPICategory;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        KPICategoryDAO cateService = new KPICategoryDAO();
        ArrayList<KPICategory> cate = cateService.getAllKPICategory("HTKD");
        for (KPICategory cat: cate
             ) {
            System.out.println(cat.getName());
            System.out.println(cat.getCategory_id());
        }
    }
}
