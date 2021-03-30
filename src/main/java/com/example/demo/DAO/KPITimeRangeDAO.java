package com.example.demo.DAO;

import com.example.demo.DAO.Datasource.DataSource;
import com.example.demo.Model.KPITimeRange;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Component;
import java.sql.*;

@Component
public class KPITimeRangeDAO {

    // func to get time-id to delete
    public KPITimeRange getKpiTimeRangeByStartDate(Date start_date, int range_type){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from kpi_time_range where start_date = ? and range_type = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, start_date);
            preparedStatement.setInt(2, range_type);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                KPITimeRange timeRange = new KPITimeRange();
                timeRange.setTimeRange_id(resultSet.getInt("id"));
                timeRange.setStart_date(resultSet.getDate("start_date"));
                timeRange.setRange_type(resultSet.getInt("range_type"));
                return timeRange;
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

    public int getOrCreateTimeID(Date start_date, int range_type){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            KPITimeRange kpi_time_range= new KPITimeRange(start_date, range_type);
            return insertIntoRangeTypeTable(kpi_time_range);
        } catch (SQLException throwables) {
            String sql = "select id from kpi_time_range where start_date = ? and range_type = ?";
            try {
                BasicDataSource bds = DataSource.getInstance().getBds();
                connection = bds.getConnection();
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDate(1, start_date);
                preparedStatement.setInt(2, range_type);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    return resultSet.getInt("id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        return 0;
    }

    // get or create range type
    public int insertIntoRangeTypeTable(KPITimeRange kpiTimeRange) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "insert into kpi_time_range (start_date, range_type) values (?, ?)";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, kpiTimeRange.getStart_date());
            preparedStatement.setInt(2, kpiTimeRange.getRange_type());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next())
                return resultSet.getInt(1);
        }  finally {
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
        return 0;
    }

    // func to check if checkpoint is a month goal
    public KPITimeRange getKPITimeRangeByID(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from kpi_time_range where id = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                KPITimeRange kpiTimeRange = new KPITimeRange(
                        resultSet.getInt("id"),
                        resultSet.getDate("start_date"),
                        resultSet.getInt("range_type")
                );
               return kpiTimeRange;
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
