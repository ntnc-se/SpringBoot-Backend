package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor

public class KPIUser {

    private int user_id;
    private String username;
    private String email;
    private String password;
    private String room_id;

}
