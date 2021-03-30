package com.example.demo.DAO;

import com.example.demo.DAO.Datasource.DataSource;
import com.example.demo.Model.KPICategory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Component;
import java.sql.*;
import java.util.ArrayList;

@Component
public class KPICategoryDAO {

    // func to get all KPI category for each room
    public ArrayList<KPICategory> getAllKPICategory(String room_id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<KPICategory> list = new ArrayList<>();
        String sql = "select kpi_category.name , kpi_category.room_id, kpi_category.unit, " +
                "kpi_category.id, kpi_category.desc from kpi_category where room_id = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, room_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                KPICategory kpi_category= new KPICategory(resultSet.getString("id"),
                        resultSet.getString("name"), 
                        resultSet.getString("room_id"),
                        resultSet.getString("unit"), 
                        resultSet.getString("desc"));
                list.add(kpi_category);
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

    // search
    // func to get kpi-category by id
    public ArrayList<KPICategory> getListCategorybyID(String id){
        ArrayList<KPICategory> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from kpi_category where id = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                list.add(new KPICategory(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("room_id"),
                        resultSet.getString("unit"),
                        resultSet.getString("desc")));
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

    // get KPI by ID
    public KPICategory getCategorybyID(String id){
        ArrayList<KPICategory> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from kpi_category where id = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return  new KPICategory(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("id"),
                        resultSet.getString("unit"),
                        resultSet.getString("desc"));
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

    // func to get kpi-category by name
    public String getCategorybyId(String id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from kpi_category where id = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                System.out.printf(resultSet.getString("id"));
                return resultSet.getString("id");
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

    public boolean deleteCategory(String kpi_id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "delete from kpi_category \n" +
                "where id = ?\n";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, kpi_id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        } finally {
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
    }

    public void insertCategory(KPICategory kpiCategory){
        System.out.println("inserted");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "replace into kpi_category (kpi_category.`name`, kpi_category.room_id, kpi_category.unit, " +
                "kpi_category.id, kpi_category.`desc`) values (?, ?, ?, ?, ?);";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, kpiCategory.getName());
            preparedStatement.setString(2, kpiCategory.getRoom_id());
            preparedStatement.setString(3, kpiCategory.getUnit());
            preparedStatement.setString(4, kpiCategory.getCategory_id());
            preparedStatement.setString(5, kpiCategory.getDesc());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
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
    }

    public void editKPICategory(KPICategory kpiCategory) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "update kpi_category set kpi_category.name = ?, kpi_category.room_id = ?, " +
                "kpi_category.unit = ?, kpi_category.desc = ? "+
                " WHERE id = ?";
        System.out.println(kpiCategory.getDesc());
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, kpiCategory.getName());
            preparedStatement.setString(2, kpiCategory.getRoom_id());
            preparedStatement.setString(3, kpiCategory.getUnit());
            preparedStatement.setString(4, kpiCategory.getDesc());
            preparedStatement.setString(5, kpiCategory.getCategory_id());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
    }

}
