package com.example.demo.Services;

import com.example.demo.DAO.KPICheckPointDAO;
import com.example.demo.DAO.KPIComponentDAO;
import com.example.demo.DAO.KPITimeRangeDAO;
import com.example.demo.Helper.DateTimeHelper;
import com.example.demo.Helper.ExcelHelper;
import com.example.demo.Model.KPICheckPoint;
import com.example.demo.Model.KPIComponent;
import com.example.demo.Model.KPIDataInput;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private KPITimeRangeDAO kpiTimeRangeDAO;

    @Autowired
    private KPICheckPointDAO kpiCheckPointDAO;

    @Autowired
    private KPIComponentDAO kpiComponentDAO;

    @Autowired
    private CheckpointService checkpointService;

    public void saveExcelDataIntoTable(MultipartFile file, Date start_date, int range_type, int component_type) {
        try {
            DateTimeHelper dateTimeHelper = new DateTimeHelper();
            Date new_start_date;
            switch (range_type){
                // daily
                case 1:
                    new_start_date = start_date;
                    break;
                // week
                case 2:
                    new_start_date = new Date(dateTimeHelper.firstDayOfWeek(new DateTime(start_date.getTime())).getMillis());
                    break;
                // month
                case 3:
                    new_start_date = new Date(dateTimeHelper.firstDayOfMonth(new DateTime(start_date.getTime())).getMillis());
                    break;
                // quarter
                case 4:
                    new_start_date = new Date(dateTimeHelper.firstDayOfQuater(new DateTime(start_date.getTime())).getMillis());
                    break;
                // year
                case 5:
                    new_start_date = new Date(dateTimeHelper.firstDayOfYear(new DateTime(start_date.getTime())).getMillis());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + range_type);
            }
            List<KPIDataInput> list = ExcelHelper.excelToDataObject(file.getInputStream());
            for (int i = 0; i < list.size(); i++) {
                int time_id =  kpiTimeRangeDAO.getOrCreateTimeID(new_start_date, range_type);
                KPIComponent kpiComponent = kpiComponentDAO.getKPIComponentById( list.get(i).getKpi_id() , component_type);
                KPICheckPoint kpiCheckPoint = new KPICheckPoint();
                kpiCheckPoint.setKpi_component_id(kpiComponent.getComponent_id());
                kpiCheckPoint.setTime_id(time_id);
                kpiCheckPoint.setValue(list.get(i).getValue());
                kpiCheckPointDAO.insertCheckpoint(kpiCheckPoint);
                System.out.println("debug: " + kpiCheckPoint.getTime_id());
                System.out.println("debug: " + kpiCheckPoint.getKpi_component_id());
                System.out.println("debug: " + kpiCheckPoint.getValue());
                System.out.println("debug: " + list.get(i).getKpi_id());
                System.out.println("debug: " + component_type);
                checkpointService.updateDashboard(kpiCheckPoint, list.get(i).getKpi_id(), component_type);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
