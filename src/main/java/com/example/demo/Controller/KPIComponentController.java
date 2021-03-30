package com.example.demo.Controller;

import com.example.demo.Model.KPIComponent;
import com.example.demo.Services.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

@CrossOrigin
@RestController
public class KPIComponentController {

    @Autowired
    private ComponentService componentService;

    @GetMapping("/api/kpi_ids")
    public ArrayList<String> getAllKPI_ID(){
        return componentService.getAllKPI_Name();
    }

    @GetMapping("/api/kpi_component_id/{kpi_id}/{component_type}")
    public int getKPIComponentID(@PathVariable String kpi_id,
                                 @PathVariable int component_type){
        KPIComponent component =  componentService.getKPIComponentByID(kpi_id, component_type);
        System.out.println(component.getComponent_id());
        return component.getComponent_id();
    }

}
