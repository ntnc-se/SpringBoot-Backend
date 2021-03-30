package com.example.demo.Controller;

import com.example.demo.Model.KPIRoom;
import com.example.demo.Services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

@CrossOrigin
@RestController
public class KPIRoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/api/rooms")
    public ArrayList<KPIRoom> getAllRooms(){
        System.out.println("getAllRooms");
        return roomService.getAllRooms();
    }

}
