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

public class KPIDisplay {

    private String id;
    private String name;
    private String room_name;
    private String unit;
    private Long value;
    private Date start_date;
    private int range_type;

}
