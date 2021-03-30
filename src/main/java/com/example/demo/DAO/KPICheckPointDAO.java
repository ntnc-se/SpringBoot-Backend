package com.example.demo.DAO;

import com.example.demo.DAO.Datasource.DataSource;
import com.example.demo.Helper.DateTimeHelper;
import com.example.demo.Model.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Component;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;

@Component
public class KPICheckPointDAO {

    // get all data for each table
    public ArrayList<KPIDisplay> getAllDataBaseOnComponentType(int component_type, String category_id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<KPIDisplay> list = new ArrayList<>();
        String sql = "select category.id, category.`name`, room.`id` as room_name, category.unit, checkpoint.value, " +
                "timerange.start_date, timerange.range_type " +
                "from kpi_check_point checkpoint\n" +
                "INNER JOIN kpi_time_range timerange on checkpoint.time_id = timerange.id\n" +
                "INNER JOIN kpi_component component on checkpoint.kpi_component_id = component.id\n" +
                "INNER JOIN kpi_category category on component.kpi_id = category.id\n" +
                "INNER JOIN kpi_room room on category.room_id = room.`id`\n" +
                "where component.component_type = ? and category.`id` = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, component_type);
            preparedStatement.setString(2, category_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                KPIDisplay kpi_display = new KPIDisplay(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("room_name"),
                        resultSet.getString("unit"),
                        resultSet.getLong("value"),
                        resultSet.getDate("start_date"),
                        resultSet.getInt("range_type")
                );
                list.add(kpi_display);
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

    // func to get component-id to delete
    public int getKPIComponentID(String kpi_id, int component_type){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select id from kpi_component where  kpi_component.kpi_id = ? and kpi_component.component_type = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, kpi_id);
            preparedStatement.setInt(2, component_type);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("id");
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

    // func to delete data
    public void deleteCheckpoint(String kpi_id, int component_type, KPITimeRange timeRange){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "delete from kpi_check_point \n" +
                "where kpi_component_id = ?\n" +
                "and time_id = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, getKPIComponentID(kpi_id, component_type));
            preparedStatement.setInt(2, timeRange.getTimeRange_id());
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

    // func to get specific data
    public KPICheckPoint getKPICheckPoint(String kpi_id, int component_type, KPITimeRange timeRange){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select kpi_check_point.kpi_component_id, kpi_check_point.time_id, kpi_check_point.value, " +
                "kpi_check_point.insert_time from kpi_check_point where kpi_component_id = ? and time_id = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, getKPIComponentID(kpi_id, component_type));
            preparedStatement.setInt(2, timeRange.getTimeRange_id());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new KPICheckPoint(
                        resultSet.getInt("kpi_component_id"),
                        resultSet.getInt("time_id"),
                        resultSet.getLong("value"),
                        resultSet.getTimestamp("insert_time"));
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

    public long getKPICheckPoint(KPIComponent component, KPITimeRange timeRange){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select kpi_check_point.value from kpi_check_point" +
                " where kpi_component_id = ? and " +
                " time_id = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, component.getComponent_id());
            preparedStatement.setInt(2, timeRange.getTimeRange_id());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("value");
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

    // function to add data
    public void insertCheckpoint(KPICheckPoint kpiCheckPoint){
        // check duplicate checkpoint has the same both component_id and time id
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        // fail constraint component-id
        String sql = "replace into kpi_check_point (kpi_component_id, time_id, value, insert_time) values (?, ?, ?, ?);";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
        connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, kpiCheckPoint.getKpi_component_id());
            preparedStatement.setInt(2, kpiCheckPoint.getTime_id());
            preparedStatement.setLong(3, kpiCheckPoint.getValue());
            // time insert
            Instant now = Instant.now();
            preparedStatement.setTimestamp(4, Timestamp.from(now));
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

    // get list days need to update
    public ArrayList<KPIData> getAllDayNeedToUpdate(String month, String kpi_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<KPIData> list = new ArrayList<>();
        String sql = "select * from kpi_data WHERE kpi_data.`month` = ? and kpi_data.kpi_id = ?" +
                " and kpi_data.type_kpi = 'DAY'";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, month);
            preparedStatement.setString(2, kpi_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new KPIData(resultSet.getString("room"),
                        resultSet.getString("kpi_name"),
                        resultSet.getLong("kpi_value"),
                        resultSet.getString("type_kpi"),
                        resultSet.getFloat("completed_plan_ratio"),
                        resultSet.getFloat("growth_rate"),
                        resultSet.getString("week"),
                        resultSet.getString("month"),
                        resultSet.getString("quarter"),
                        resultSet.getString("year"),
                        resultSet.getDate("createdday"),
                        resultSet.getString("kpi_id"),
                        resultSet.getInt("id"),
                        resultSet.getFloat("kpi_goal"),
                        resultSet.getLong("previous_value")));
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

    // func to update data table check point
    public void editDataCheckPoint(KPICheckPoint kpiCheckPoint) {
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = "update kpi_check_point set kpi_check_point.`value` = ?, kpi_check_point.insert_time = ?" +
                " WHERE time_id = ? and kpi_component_id = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, kpiCheckPoint.getValue());
            // time insert
            Instant now = Instant.now();
            preparedStatement.setTimestamp(2, Timestamp.from(now));
            preparedStatement.setInt(3, kpiCheckPoint.getTime_id());
            preparedStatement.setInt(4, kpiCheckPoint.getKpi_component_id());
            preparedStatement.executeUpdate();
            // update in kpi_data
            // body and variable in params ~ insertData
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

    public ArrayList<KPIDisplay> getAllDataBaseOnCategoryAndDate(int component_type, String category_id, Date start_date){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<KPIDisplay> list = new ArrayList<>();
        String sql = "select category.id, category.`name`, room.`id` as room_name, category.unit, checkpoint.value, " +
                "timerange.start_date, timerange.range_type " +
                "from kpi_check_point checkpoint\n" +
                "INNER JOIN kpi_time_range timerange on checkpoint.time_id = timerange.id\n" +
                "INNER JOIN kpi_component component on checkpoint.kpi_component_id = component.id\n" +
                "INNER JOIN kpi_category category on component.kpi_id = category.id\n" +
                "INNER JOIN kpi_room room on category.room_id = room.`id`\n" +
                "where component.component_type = ? and category.`id` = ? and timerange.`start_date` = ?";
        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, component_type);
            preparedStatement.setString(2, category_id);
            preparedStatement.setDate(3, start_date);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                KPIDisplay kpi_display = new KPIDisplay(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("room_name"),
                        resultSet.getString("unit"),
                        resultSet.getLong("value"),
                        resultSet.getDate("start_date"),
                        resultSet.getInt("range_type"));
                list.add(kpi_display);
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
        return list;
    }

    // func to paging table
    // func to get number of page need to paging
    public int getTotalPage(int component_type, String category_name, int numberOfRecordsInPage) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select COUNT(category.id)\n" +
                "from kpi_check_point checkpoint\n" +
                "INNER JOIN kpi_time_range timerange on checkpoint.time_id = timerange.id\n" +
                "INNER JOIN kpi_component component on checkpoint.kpi_component_id = component.id\n" +
                "INNER JOIN kpi_category category on component.kpi_id = category.id\n" +
                "INNER JOIN kpi_room room on category.room_id = room.`id`\n" +
                "where component.component_type = ? and category.`name` = ? ";

        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, component_type);
            preparedStatement.setString(2, category_name);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int numberRecords = resultSet.getInt(1);
                int totalPage = numberRecords / numberOfRecordsInPage;
                return (numberRecords % numberOfRecordsInPage == 0) ? totalPage : ++totalPage;
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

    // func to get array of kpi between start index and end index
    public ArrayList<KPIDisplay> getListDataPaging(int component_type, String category_id, int numberOfRecordsInPage, int pageCurrent) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<KPIDisplay> list = new ArrayList<>();

        String sql = "select category.id, category.`name`, room.`id` as room_name, category.unit, checkpoint.value,\n" +
                "timerange.start_date, timerange.range_type \n" +
                "from kpi_check_point checkpoint\n" +
                "INNER JOIN kpi_time_range timerange on checkpoint.time_id = timerange.id\n" +
                "INNER JOIN kpi_component component on checkpoint.kpi_component_id = component.id\n" +
                "INNER JOIN kpi_category category on component.kpi_id = category.id\n" +
                "INNER JOIN kpi_room room on category.room_id = room.`id`\n" +
                "where component.component_type = ? and category.`id` = ? ORDER BY start_date LIMIT ? , ?";

        try {
            BasicDataSource bds = DataSource.getInstance().getBds();
            connection = bds.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, component_type);
            preparedStatement.setString(2, category_id);
            int indexFrom = (pageCurrent - 1) * numberOfRecordsInPage ;
            //int indexTo = indexFrom + numberOfRecordsInPage; // not indexTo to checkk
            preparedStatement.setInt(3, indexFrom);
            preparedStatement.setInt(4, numberOfRecordsInPage);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                KPIDisplay kpi_display = new KPIDisplay(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("room_name"),
                        resultSet.getString("unit"),
                        resultSet.getLong("value"),
                        resultSet.getDate("start_date"),
                        resultSet.getInt("range_type"));
                list.add(kpi_display);
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
