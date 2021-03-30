package com.example.demo.Services;

import com.example.demo.DAO.KPIComponentDAO;
import com.example.demo.Model.KPIComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class ComponentService {

    @Autowired
    private KPIComponentDAO kpiComponentDAO;

    public ArrayList<String> getAllKPI_Name(){
       return kpiComponentDAO.getAllKPI_Name();
    }

    public KPIComponent getKPIComponentByID(String kpi_id, int component_type){
        return kpiComponentDAO.getKPIComponentById(kpi_id, component_type);
    }

}
