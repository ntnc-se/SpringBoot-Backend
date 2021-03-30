package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class KPIComponent {

    private int component_id;
    private String kpi_id;
    private int component_type;
    private int input_type;

}
