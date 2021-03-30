package com.example.demo.DAO;

import com.example.demo.DAO.Datasource.DataSource;
import com.example.demo.Model.KPIComponent;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class KPIComponentDAO {

    // func to get all component
    public ArrayList<KPIComponent> getAllComponent(String kpi_category_id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<KPIComponent> list = new ArrayList<>();
        String sql = "select kpi_component.id, kpi_component.kpi_id, kpi_component.component_type, kpi_component.input_type " +
                "from kpi_component where kpi_id = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, kpi_category_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                KPIComponent kpi_component = new KPIComponent(resultSet.getInt("id"),
                        resultSet.getString("kpi_id"), resultSet.getInt("component_type"),
                        resultSet.getInt("input_type"));
                list.add(kpi_component);
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

    public ArrayList<String> getAllKPI_Name(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<String> list = new ArrayList<>();
        //String sql = "select DISTINCT kpi_id from kpi_component";
        String sql = "select DISTINCT kpi_category.`name` from kpi_component\n" +
                "INNER JOIN kpi_category on kpi_component.kpi_id = kpi_category.id";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                list.add(resultSet.getString("name"));
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

    public KPIComponent getKPIComponentById(String kpi_id, int component_type){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //String sql = "select id from kpi_component where kpi_component.kpi_id = ? and kpi_component.component_type = 3";
        String sql = "select kpi_component.id, kpi_component.kpi_id,kpi_component.component_type,kpi_component.input_type from kpi_component inner join kpi_category on " +
                "kpi_component.kpi_id = kpi_category.id where kpi_category.`id` = ? and " +
                "kpi_component.component_type = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, kpi_id);
            preparedStatement.setInt(2, component_type);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                KPIComponent component = new KPIComponent();
                component.setKpi_id(resultSet.getString("kpi_id"));
                component.setInput_type(resultSet.getInt("input_type"));
                component.setComponent_type(resultSet.getInt("component_type"));
                component.setComponent_id(resultSet.getInt("id"));
                return component;
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
        return null;
    }

    public KPIComponent getKPIComponentByName(String kpi_name, int component_type){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //String sql = "select id from kpi_component where kpi_component.kpi_id = ? and kpi_component.component_type = 3";
        String sql = "select kpi_component.id, kpi_component.kpi_id,kpi_component.component_type,kpi_component.input_type  from kpi_component inner join kpi_category on " +
                "kpi_component.kpi_id = kpi_category.id where kpi_category.`name` = ? and " +
                "kpi_component.component_type = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, kpi_name);
            preparedStatement.setInt(2, component_type);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                KPIComponent component = new KPIComponent();
                component.setKpi_id(resultSet.getString("kpi_id"));
                component.setInput_type(resultSet.getInt("input_type"));
                component.setComponent_type(resultSet.getInt("component_type"));
                component.setComponent_id(resultSet.getInt("id"));
                return component;
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
        return null;
    }

}
