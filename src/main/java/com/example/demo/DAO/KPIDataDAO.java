package com.example.demo.DAO;

import com.example.demo.DAO.Datasource.DataSource;
import com.example.demo.Model.KPIData;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Component;
import java.sql.*;
import java.util.Map;

@Component
public class KPIDataDAO {
    public void updateKPIDataGoalBatch(Map<Date,Long> valueMap, String kpi_id, String type_kpi) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String sql = "update kpi_data set kpi_goal = ?," +
                " completed_plan_ratio = 100" +
                " where kpi_id = ? and" +
                " createdday = ? and" +
                " type_kpi = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (Map.Entry<Date, Long> entry: valueMap.entrySet()) {
                preparedStatement.setLong(1, entry.getValue());
                preparedStatement.setString(2, kpi_id);
                preparedStatement.setDate(3, entry.getKey());
                preparedStatement.setString(4, type_kpi);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
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

    public void updateKPIDataGoal(Date createdDay, String kpi_id, long kpi_goal, String type_kpi) {
        System.out.println("Debug: " + createdDay);
        System.out.println("Debug: " + kpi_id);
        System.out.println("Debug: " + kpi_goal);
        System.out.println("Debug: " + type_kpi);
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String sql = "update kpi_data set kpi_goal = ?," +
                " completed_plan_ratio = (case when kpi_value = 0 then 0 else (case when kpi_goal = 0 then 100 else (kpi_value/kpi_goal) * 100 end) end)" +
                " where kpi_id = ? and" +
                " createdday = ?" +
                " and type_kpi = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, kpi_goal);
            preparedStatement.setString(2, kpi_id);
            preparedStatement.setDate(3, createdDay);
            preparedStatement.setString(4, type_kpi);
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

    public void updateKPIDataDayGoalByMonth(int month, int year, String kpi_id, long kpi_goal) {
        System.out.println("update day by month");
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String sql = "update kpi_data set kpi_goal = ?," +
                " completed_plan_ratio = (case when kpi_value = 0 then 0 else (case when kpi_goal = 0 then 100 else (kpi_value/kpi_goal) * 100 end) end)" +
                " where kpi_id = ? and" +
                " month(createdday) = ? and" +
                " year(createdday) = ? and" +
                " type_kpi = 'DAY'";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, kpi_goal);
            preparedStatement.setString(2, kpi_id);
            preparedStatement.setInt(3, month);
            preparedStatement.setInt(4, year);
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

    public long getKpiData(String kpi_id, Date createDay, String kpiType){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select kpi_value from kpi_data where kpi_id = ? and createdday = ? and type_kpi = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, kpi_id);
            preparedStatement.setDate(2, createDay);
            preparedStatement.setString(3, kpiType);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("kpi_value");
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
        return 0;
    }

    public void insertKPIDataValue(KPIData data) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String sql = "replace into kpi_data (room, kpi_name, kpi_value,\n" +
                "type_kpi, completed_plan_ratio, growth_rate, week, month,\n"+
                "quarter, year, createdday, kpi_id, kpi_goal, previous_value) \n" +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, data.getRoom());
            preparedStatement.setString(2, data.getKpi_name());
            preparedStatement.setLong(3, data.getKpi_value());
            preparedStatement.setString(4, data.getType_kpi());
            preparedStatement.setFloat(5, data.getCompleted_plan_ratio());
            preparedStatement.setFloat(6, data.getGrowth_rate());
            preparedStatement.setString(7, data.getWeek());
            preparedStatement.setString(8, data.getMonth());
            preparedStatement.setString(9,data.getQuarter());
            preparedStatement.setString(10, data.getYear());
            preparedStatement.setDate(11, data.getCreatedday());
            preparedStatement.setString(12, data.getKpi_id());
            preparedStatement.setDouble(13, data.getKpi_goal());
            preparedStatement.setLong(14, data.getPrevious_value());
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
