package com.example.demo.Services;

import com.example.demo.DAO.KPICategoryDAO;
import com.example.demo.Model.KPICategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class CategoryService {

    @Autowired
    private KPICategoryDAO kpiCategoryDAO;

    public ArrayList<KPICategory> getAllKPICategory(String room_id){
       return kpiCategoryDAO.getAllKPICategory(room_id);
    }

    public String getCategoryIdById(String id){
        return kpiCategoryDAO.getCategorybyId(id);
    }

    public boolean deleteCategory(String kpi_id){
        System.out.println("debug:" + kpiCategoryDAO.deleteCategory(kpi_id));
        return kpiCategoryDAO.deleteCategory(kpi_id);
    }

    public void updateCategory(KPICategory kpiCategory){
        kpiCategoryDAO.editKPICategory(kpiCategory);
    }

    public void insertCategory(KPICategory kpiCategory){
        kpiCategoryDAO.insertCategory(kpiCategory);
    }

    public ArrayList<KPICategory> getListSpecificCategory(String id){
        return kpiCategoryDAO.getListCategorybyID(id);
    }

    public KPICategory getSpecificCategory(String id){
        return kpiCategoryDAO.getCategorybyID(id);
    }
}
