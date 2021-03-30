package com.example.demo.Services;

import com.example.demo.DAO.*;
import com.example.demo.Helper.DateTimeHelper;
import com.example.demo.Model.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.ArrayList;

@Service
public class CheckpointService {
    @Autowired
    private KPICheckPointDAO kpiCheckPointDAO;

    @Autowired
    private KPITimeRangeDAO kpiTimeRangeDAO;

    @Autowired
    private KPIDataDAO kpiDataDAO;

    @Autowired
    private KPIComponentDAO kpiComponentDAO;

    @Autowired
    private KPICategoryDAO kpiCateDAO;

    public void editData(KPICheckPoint kpiCheckPoint, String kpi_id, int component_type){
        kpiCheckPointDAO.editDataCheckPoint(kpiCheckPoint);
        updateDashboard(kpiCheckPoint,kpi_id,component_type);
    }

    public void updateDashboard(KPICheckPoint kpiCheckPoint, String kpi_id, int component_type) {
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        KPITimeRange timeRange = kpiTimeRangeDAO.getKPITimeRangeByID(kpiCheckPoint.getTime_id());
        DateTime firstDay = new DateTime(timeRange.getStart_date().getTime());
        if (component_type == 3) {
            switch (timeRange.getRange_type() ){
                case 3: {
                    int noDaysOfMonth = dateTimeHelper.noDayOfMonth(firstDay);
                    //System.out.println("value:" + kpiCheckPoint.getValue() + "number days:"+ numberOfDays);
                    kpiDataDAO.updateKPIDataDayGoalByMonth(firstDay.getMonthOfYear(),firstDay.getYear(), kpi_id,kpiCheckPoint.getValue()/noDaysOfMonth);
                    // update data by month
                    kpiDataDAO.updateKPIDataGoal(new Date(firstDay.getMillis()), kpi_id, kpiCheckPoint.getValue(), "MONTH");
                    // update data by week
//                    Map<Date, Long> weekValueMap= new HashMap<>();
                    for(DateTime i = dateTimeHelper.firstDayOfWeek(firstDay);i.isBefore(firstDay.plusMonths(1)); i = i.plusDays(7)) {
                        if(dateTimeHelper.firstDayOfWeek(i).getMonthOfYear() == dateTimeHelper.lastDayOfWeek(i).getMonthOfYear()){
                           kpiDataDAO.updateKPIDataGoal(new Date(i.getMillis()), kpi_id, (kpiCheckPoint.getValue()/noDaysOfMonth) * 7, "WEEK");
//                            weekValueMap.put(new Date(i.getMillis()), (kpiCheckPoint.getValue()/noDaysOfMonth) * 7);
                        } else {
                            // get goal two month
                            KPIComponent component = kpiComponentDAO.getKPIComponentById(kpi_id, component_type);

                            KPITimeRange timeRange1 = kpiTimeRangeDAO.getKpiTimeRangeByStartDate(new Date(dateTimeHelper.firstDayOfMonth
                                    (dateTimeHelper.firstDayOfWeek(i)).getMillis()), component_type);
                            long month_goal_first = timeRange1 != null ? kpiCheckPointDAO.getKPICheckPoint(component, timeRange1) : 0;

                            KPITimeRange timeRange2 = kpiTimeRangeDAO.getKpiTimeRangeByStartDate(new Date(dateTimeHelper.firstDayOfMonth
                                    (dateTimeHelper.lastDayOfWeek(i)).getMillis()), component_type);

                            long month_goal_second = timeRange2 != null ? kpiCheckPointDAO.getKPICheckPoint(component, timeRange2) : 0;
                            // get number day two month
                            int num_day_first = dateTimeHelper.noDayOfMonth(dateTimeHelper.firstDayOfWeek(i));
                            int num_day_second = dateTimeHelper.noDayOfMonth(dateTimeHelper.lastDayOfWeek(i));

                            // divide => goal 1 day per month
                            long day_first_goal = month_goal_first / num_day_first;

                            long day_second_goal = month_goal_second /num_day_second;

                            // number day between
                            int head_gap = dateTimeHelper.getDayBetween(dateTimeHelper.firstDayOfWeek(i),
                                    dateTimeHelper.lastDayOfMonth(dateTimeHelper.firstDayOfWeek(i))) + 1;
                            int tail_gap = dateTimeHelper.getDayBetween(dateTimeHelper.firstDayOfMonth(dateTimeHelper.lastDayOfWeek(i)),
                                    dateTimeHelper.lastDayOfWeek(i)) + 1;
                            // week goal
                            kpiDataDAO.updateKPIDataGoal(new Date(i.getMillis()), kpi_id, day_first_goal * head_gap + day_second_goal * tail_gap, "WEEK");
//                            weekValueMap.put(new Date(i.getMillis()), day_first_goal * head_gap + day_second_goal * tail_gap);
                        }
                    }
//                    kpiDataDAO.updateKPIDataGoalBatch(weekValueMap, kpi_id, "WEEK");
                    break;
                }
                case 4: {
                    kpiDataDAO.updateKPIDataGoal(new Date(firstDay.getMillis()), kpi_id, kpiCheckPoint.getValue(), "QUARTER");
                    break;
                }
                case 5: {
                    kpiDataDAO.updateKPIDataGoal(new Date(firstDay.getMillis()), kpi_id, kpiCheckPoint.getValue(), "YEAR");
                    break;
                }
            }
        } else if (component_type == 2) {
            KPICheckPoint checkPoint = getSpecificDataCheckPoint(kpi_id, new Date(firstDay.getMillis()), timeRange.getRange_type(), 3);
            long goal = 0;
            if (checkPoint != null) {
                goal = checkPoint.getValue();
            }
            float goalPercent;
            if(kpiCheckPoint.getValue() == 0) {
                goalPercent = 0;
            } else if(goal <= 0){
                goalPercent = 100;
            } else {
                goalPercent = ((float)kpiCheckPoint.getValue()/ goal) * 100;
            }
            Date previousDate;
            String type_kpi;
            switch (timeRange.getRange_type()) {
                case 3: {
                    previousDate = new Date(firstDay.minusMonths(1).getMillis());
                    type_kpi = "MONTH";
                    break;
                }
                case 4: {
                    previousDate = new Date(firstDay.minusMonths(3).getMillis());
                    type_kpi = "QUARTER";
                    break;
                }
                case 5: {
                    previousDate = new Date(firstDay.minusYears(1).getMillis());
                    type_kpi = "YEAR";
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + timeRange.getRange_type());
            }
            long previousValue = kpiDataDAO.getKpiData(kpi_id, previousDate, type_kpi);
            long lastYearValue;
            if (timeRange.getRange_type() == 5) {
                lastYearValue = 0;
            } else {
                lastYearValue = kpiDataDAO.getKpiData(kpi_id, new Date(firstDay.minusYears(1).getMillis()), type_kpi);
            }

            float growthRate;
            if (kpiCheckPoint.getValue() == 0) {
                growthRate = 0;
            } else if (previousValue == 0) {
                growthRate = 100;
            } else {
                growthRate = (((float) kpiCheckPoint.getValue()/dateTimeHelper.dayOfMonth(firstDay)) /
                        ((float)previousValue/dateTimeHelper.dayOfMonth(firstDay.minusMonths(1))) - 1) * 100;
            }
            KPICategory cate = kpiCateDAO.getCategorybyID(kpi_id);
            String week = String.format("%02d/%4d", firstDay.getWeekOfWeekyear(),firstDay.getWeekyear());
            String month = String.format("%02d/%4d", firstDay.getMonthOfYear(), firstDay.getYear());
            String quarter = String.format("%02d/%4d", dateTimeHelper.getQuarterOfYear(firstDay),firstDay.getYear());
            String year = String.format("%4d",firstDay.getYear());
            KPIData kpiData = new KPIData(
                    String.format("P.%s",cate.getRoom_id()),
                    cate.getName(),
                    kpiCheckPoint.getValue(),
                    type_kpi,
                    goalPercent,
                    growthRate,
                    week,
                    month,
                    quarter,
                    year,
                    new Date(firstDay.getMillis()),
                    kpi_id,
                    goal,
                    lastYearValue
            );

            kpiDataDAO.insertKPIDataValue(kpiData);
        }

    }

    public void insertCheckpoint(KPICheckPoint kpiCheckPoint, Date start_date, int range_type, String kpi_id, int component_type) {
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        Date new_start_date;
        switch (range_type){
            // daily
            case 1:
                new_start_date = start_date;
                break;
            // week
            case 2:
                new_start_date = new Date(dateTimeHelper.firstDayOfWeek(new DateTime(start_date.getTime())).getMillis());
                break;
            // month
            case 3:
                new_start_date = new Date(dateTimeHelper.firstDayOfMonth(new DateTime(start_date.getTime())).getMillis());
                break;
            // quarter
            case 4:
                new_start_date = new Date(dateTimeHelper.firstDayOfQuater(new DateTime(start_date.getTime())).getMillis());
                break;
            // year
            case 5:
                new_start_date = new Date(dateTimeHelper.firstDayOfYear(new DateTime(start_date.getTime())).getMillis());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + range_type);
        }
        kpiCheckPoint.setTime_id(kpiTimeRangeDAO.getOrCreateTimeID(new_start_date, range_type));
        kpiCheckPointDAO.insertCheckpoint(kpiCheckPoint);
        updateDashboard(kpiCheckPoint, kpi_id, component_type);
    }

    public ArrayList<KPIDisplay> getAllDataBaseOnComponentType(int component_type, String category_id) {
        return kpiCheckPointDAO.getAllDataBaseOnComponentType(component_type,category_id);
    }

    public void deleteDataKPI(String kpi_id, Date start_date, int range_type, int component_type) {
        KPITimeRange timeRange = kpiTimeRangeDAO.getKpiTimeRangeByStartDate(start_date, range_type);
        kpiCheckPointDAO.deleteCheckpoint(kpi_id, component_type, timeRange);
        KPICheckPoint checkPoint = new KPICheckPoint();
        checkPoint.setTime_id(timeRange.getTimeRange_id());
        checkPoint.setValue(0);
        updateDashboard(checkPoint, kpi_id, component_type);
    }

    public KPICheckPoint getSpecificDataCheckPoint(String kpi_id, Date start_date, int range_type, int component_type) {
        KPITimeRange timeRange = kpiTimeRangeDAO.getKpiTimeRangeByStartDate(start_date, range_type);
        return kpiCheckPointDAO.getKPICheckPoint(kpi_id, component_type, timeRange);
    }

    public ArrayList<KPIDisplay> getAllDataOnCategoryAndDate(int component_type, String category_id, Date start_date){
        return kpiCheckPointDAO.getAllDataBaseOnCategoryAndDate(component_type, category_id, start_date);
    }

    // func to paging
    public int getTotalPage(int component_type, String category_id, int numberOfRecordsInPage){
        int totalPage = kpiCheckPointDAO.getAllDataBaseOnComponentType(component_type,category_id).size() / numberOfRecordsInPage;
        return (kpiCheckPointDAO.getAllDataBaseOnComponentType(component_type,category_id).size() % numberOfRecordsInPage == 0)
                ? totalPage : ++totalPage;
    }

    // array kpi paging
    public ArrayList<KPIDisplay> getListDataPaging(int component_type, String category_id, int numberOfRecordsInPage, int pageCurrent){
        return kpiCheckPointDAO.getListDataPaging(component_type, category_id, numberOfRecordsInPage, pageCurrent);
    }

}
