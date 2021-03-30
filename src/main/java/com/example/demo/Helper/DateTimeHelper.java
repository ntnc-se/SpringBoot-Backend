package com.example.demo.Helper;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeHelper {

    public static DateTimeFormatter monthFormatter = DateTimeFormat.forPattern("MM/yyyy");

    public DateTime firstDayOfWeek(DateTime dateTime){
        return dateTime.weekOfWeekyear().roundFloorCopy().withTimeAtStartOfDay();
    }

    public DateTime lastDayOfWeek(DateTime date)  {
        if(date.isEqual(date.weekOfWeekyear().roundCeilingCopy())) {
            return date.plusDays(6);
        } else {
            return date.weekOfWeekyear().roundCeilingCopy().minusDays(1).withTimeAtStartOfDay();
        }
    }

    public DateTime firstDayOfMonth(DateTime dateTime){
        return dateTime.dayOfMonth().withMinimumValue().withTimeAtStartOfDay();
    }

    public DateTime lastDayOfMonth(DateTime dateTime){
        return dateTime.dayOfMonth().withMaximumValue().withTimeAtStartOfDay();
    }

    public int noDayOfMonth(DateTime dateTime){
        return dateTime.dayOfMonth().getMaximumValue();
    }

    public int getDayBetween(DateTime fromDate, DateTime toDate){
        Interval interval = new Interval(fromDate, toDate);
        return (int)interval.toDuration().getStandardDays();
    }

    public int getQuarterOfYear(DateTime date){
        int month = date.getMonthOfYear();
        if (month % 3 == 0) {
            return month/3;
        } else {
            return month/3 + 1;
        }
    }

    public DateTime firstDayOfQuarter(DateTime date ) {
        int quarter = 0;
        if (date.getMonthOfYear() % 3 == 0) {
            quarter = date.getMonthOfYear() / 3;
        }
        else {
            quarter = date.getMonthOfYear() / 3 + 1;
        }
        return date.withMonthOfYear(quarter * 3 - 2)
                .dayOfMonth().withMinimumValue()
                .withTimeAtStartOfDay();
    }

    public DateTime lastDayOfQuarter(DateTime date) {
        int quarter = 0;
        if (date.getMonthOfYear() % 3 == 0) {
            quarter = date.getMonthOfYear() / 3;
        }
        else {
            quarter = date.getMonthOfYear() / 3 + 1;
        }
        return date.withMonthOfYear(quarter * 3)
                .dayOfMonth().withMaximumValue()
                .withTimeAtStartOfDay();
    }

    public int dayOfQuarter( DateTime date)  {
       return getDayBetween(firstDayOfQuarter(date), date) + 1;
    }

    public int dayOfYear( DateTime date) {
        return date.dayOfYear().get();
    }

    public int dayOfMonth( DateTime date) {
        return date.dayOfMonth().get();
    }

    public DateTime firstDayOfQuater(DateTime dateTime){
        int quarter = 0;
        if(dateTime.getMonthOfYear() % 3 == 0){
            quarter = dateTime.getMonthOfYear() /3 ;
        }else{
            quarter = dateTime.getMonthOfYear() / 3 + 1;
        }
        return dateTime.withMonthOfYear(quarter * 3 - 2).dayOfMonth().withMinimumValue().withTimeAtStartOfDay();
    }

    public DateTime firstDayOfYear(DateTime dateTime){
        return dateTime.dayOfYear().withMinimumValue().withTimeAtStartOfDay();
    }
}
