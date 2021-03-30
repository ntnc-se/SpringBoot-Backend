package com.example.demo.DAO;

import com.example.demo.DAO.Datasource.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class KPIUserDAO {

    // func to check if user is existed in db
    public boolean isUser(String username, String password){
        // System.out.println("isUser|" + username + "|" + password);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT kpi_user.`id`, kpi_user.`username`, kpi_user.`email`, " +
                "kpi_user.`password`, kpi_user.`room_id` FROM `kpi_user` WHERE kpi_user.`username`" +
                " = ? AND kpi_user.`password` = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
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
        return false;
    }
}
