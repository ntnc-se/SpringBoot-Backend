package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class KPICheckPoint {

    private int kpi_component_id;
    private int time_id;
    private long value;
    private Timestamp insert_time;

//    public KPI_Check_Point(int category_id, BigInteger value, Timestamp insert_time, int timeRange_id, Date time){
//        this.category_id = category_id;
//        this.value = value;
//        this.insert_time = insert_time;
//        this.timeRange_id = timeRange_id;
//        this.time = time;
//    }

}
