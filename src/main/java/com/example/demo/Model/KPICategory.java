package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class KPICategory {

    private String category_id;
    private String name;
    private String room_id;
    private String unit;
    private String desc;

}
