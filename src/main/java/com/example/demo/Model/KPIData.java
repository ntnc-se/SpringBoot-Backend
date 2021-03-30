package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class KPIData {

    private String room;
    private String kpi_name;
    private long kpi_value;
    private String type_kpi;
    private float completed_plan_ratio;
    private float growth_rate;
    private String week;
    private String month;
    private String quarter;
    private String year;
    private Date createdday;
    private String kpi_id;
    private int id;
    private float kpi_goal;
    private long previous_value;

    public KPIData(String room, String kpi_name, long kpi_value, String type_kpi, float completed_plan_ratio, float growth_rate, String week, String month, String quarter, String year, Date createdday, String kpi_id, float kpi_goal, long previous_value) {
        this.room = room;
        this.kpi_name = kpi_name;
        this.kpi_value = kpi_value;
        this.type_kpi = type_kpi;
        this.completed_plan_ratio = completed_plan_ratio;
        this.growth_rate = growth_rate;
        this.week = week;
        this.month = month;
        this.quarter = quarter;
        this.year = year;
        this.createdday = createdday;
        this.kpi_id = kpi_id;
        this.kpi_goal = kpi_goal;
        this.previous_value = previous_value;
    }
}
