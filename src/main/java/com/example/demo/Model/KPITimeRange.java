package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class KPITimeRange {

    private int timeRange_id;
    private Date start_date;
    private int range_type;

    public KPITimeRange(Date start_date, int range_type){
        this.start_date = start_date;
        this.range_type = range_type;
    }

}
