package com.example.demo.Controller;

import com.example.demo.Model.KPICheckPoint;
import com.example.demo.Model.KPIDisplay;
import com.example.demo.Services.CheckpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.ArrayList;

@CrossOrigin
@RestController
public class KPICheckPointController {

    @Autowired
    private CheckpointService checkpointService;

    @GetMapping(value = "/api/kpivalue/{component_type}/{category_id}")
    public ArrayList<KPIDisplay> getAllKPIData(@PathVariable int component_type,
                                               @PathVariable String category_id){
        return checkpointService.getAllDataBaseOnComponentType(component_type, category_id);
    }

    @GetMapping(value = "/api/kpivalue/{component_type}/{category_id}/{start_date}")
    public ArrayList<KPIDisplay> getAllKPIData(@PathVariable int component_type,
                                               @PathVariable String category_id,
                                               @PathVariable Date start_date){
        return checkpointService.getAllDataOnCategoryAndDate(component_type, category_id, start_date);
    }

    @DeleteMapping("/api/kpivalue/{kpi_id}/{start_date}/{range_type}/{component_type}")
    public ResponseEntity<Void> deleteData(@PathVariable String kpi_id,
                                           @PathVariable Date start_date,
                                           @PathVariable int range_type,
                                           @PathVariable int component_type){
        System.out.println("debug delete: " + kpi_id);
        System.out.println("debug delete: " + component_type);
        checkpointService.deleteDataKPI(kpi_id, start_date, range_type, component_type);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/kpivalue/{kpi_id}/{start_date}/{range_type}/{component_type}")
    public KPICheckPoint getSpecificData(@PathVariable String kpi_id,
                                         @PathVariable Date start_date,
                                         @PathVariable int range_type,
                                         @PathVariable int component_type){
        return checkpointService.getSpecificDataCheckPoint(kpi_id, start_date, range_type, component_type);
    }

    @PostMapping("/api/kpivalues/edit")
    public ResponseEntity<KPICheckPoint> updateData(@RequestBody KPICheckPoint kpi_check_point,
                                                    @RequestParam String kpi_id,
                                                    @RequestParam int component_type){
        checkpointService.editData(kpi_check_point, kpi_id, component_type);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/kpivalues/add")
    public ResponseEntity<KPICheckPoint> insertData(@RequestBody KPICheckPoint kpi_check_point,
                                                    @RequestParam Date start_date,
                                                    @RequestParam int range_type,
                                                    @RequestParam String kpi_id,
                                                    @RequestParam int component_type){
        checkpointService.insertCheckpoint(kpi_check_point, start_date, range_type,kpi_id, component_type);
        return ResponseEntity.noContent().build();
    }

    // api to paging
    // get number page
    @GetMapping("/api/kpivalue/numpage/{component_type}/{category_id}/{numrecord}")
    public int getNumberPage(@PathVariable int component_type,
                             @PathVariable String category_id,
                             @PathVariable int numrecord){
        return checkpointService.getTotalPage(component_type, category_id, numrecord);
    }

    // get array to paging
    @GetMapping("/api/kpivalue/kpipaging/{component_type}/{category_id}/{numrecord}/{pagecurrent}")
    public ArrayList<KPIDisplay> getArrayToPaging(@PathVariable int component_type,
                                                  @PathVariable String category_id,
                                                  @PathVariable int numrecord,
                                                  @PathVariable int pagecurrent){
        return checkpointService.getListDataPaging(component_type, category_id, numrecord, pagecurrent);
    }
}
