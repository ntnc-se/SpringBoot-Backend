package com.example.demo.DAO;

import com.example.demo.DAO.Datasource.DataSource;
import com.example.demo.Model.KPIRoom;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class KPIRoomDAO {

    // func to get all rooms
    public ArrayList<KPIRoom> getAllRooms(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<KPIRoom> list = new ArrayList<>();
        String sql = "select kpi_room.name, kpi_room.id from kpi_room";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                KPIRoom kpi_room = new KPIRoom(resultSet.getString("name"), resultSet.getString("id"));
                list.add(kpi_room);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {}
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {}
            }
        }
        return list;
    }
}
