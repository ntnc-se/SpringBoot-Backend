package com.example.demo.Controller;

import com.example.demo.Helper.ExcelHelper;
import com.example.demo.Model.ResponseMessage;
import com.example.demo.Services.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Date;
import java.util.Map;

@CrossOrigin
@Controller
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping(value = "/api/upload", consumes = {"application/json", MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart(value = "file") MultipartFile file, @RequestHeader Map<String, String> header){
        String message = "";
        if(ExcelHelper.hasExcelFormat(file)){
            try {
                Date start_date = Date.valueOf(header.get("start_date"));
                int range_type = Integer.parseInt(header.get("range_type"));
                int component_type = Integer.parseInt(header.get("component_type"));

                excelService.saveExcelDataIntoTable(file, start_date, range_type, component_type);
                message = "Uploaded the file and data successfully: " + file.getOriginalFilename() +" and date:"+
                        start_date + " and type: " + range_type + " and component: " + component_type;

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

}
