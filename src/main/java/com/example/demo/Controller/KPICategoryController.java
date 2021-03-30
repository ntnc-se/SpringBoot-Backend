package com.example.demo.Controller;

import com.example.demo.Model.KPICategory;
import com.example.demo.Model.KPICheckPoint;
import com.example.demo.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;

@CrossOrigin
@RestController
public class KPICategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/kpicategory/{room_id}")
    public ArrayList<KPICategory> getAllKPICategory(@PathVariable String room_id){
        return categoryService.getAllKPICategory(room_id);
    }

    @GetMapping("/api/kpicategory_id/{kpi_id}")
    public String getKPICategoryById(@PathVariable String kpi_id){
        return categoryService.getCategoryIdById(kpi_id);
    }

    @GetMapping("/api/kpicategoryspecific_id/{kpi_id}")
    public ArrayList<KPICategory> getListSpecificKPICategoryByID(@PathVariable String kpi_id){
        return categoryService.getListSpecificCategory(kpi_id);
    }

    @GetMapping("/api/kpicategoryspecific1_id/{kpi_id}")
    public KPICategory getSpecificKPICategoryByID(@PathVariable String kpi_id){
        return categoryService.getSpecificCategory(kpi_id);
    }

    @DeleteMapping("/api/kpicategory/{kpi_id}")
    public boolean deleteData(@PathVariable String kpi_id){
        return categoryService.deleteCategory(kpi_id);
    }

    @PostMapping("/api/kpicategory/edit")
    public ResponseEntity<KPICategory> updateData(@RequestBody KPICategory kpiCategory){
        categoryService.updateCategory(kpiCategory);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/kpicate_id/add")
    public ResponseEntity<KPICategory> insertData(@RequestBody KPICategory kpiCategory){
        categoryService.insertCategory(kpiCategory);
        return ResponseEntity.noContent().build();
    }

}
