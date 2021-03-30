package com.example.demo.Services;

import com.example.demo.DAO.KPIRoomDAO;
import com.example.demo.Model.KPIRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class RoomService {

    @Autowired
    private KPIRoomDAO kpiRoomDAO;

    public ArrayList<KPIRoom> getAllRooms(){
       return kpiRoomDAO.getAllRooms();
    }

}
