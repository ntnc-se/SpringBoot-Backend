package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

// object represent for data input into table
public class KPIDataInput {

    private String kpi_id;
    private long value;
    private Date start_date;
    private int rang_type;

    @Override
    public String toString(){
        return ("kpi-id:" + kpi_id +", value:" + value);
    }

}
