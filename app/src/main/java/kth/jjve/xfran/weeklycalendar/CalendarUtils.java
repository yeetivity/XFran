package kth.jjve.xfran.weeklycalendar;

/*
Function: useful methods for the calendar
Activity: CalendarViewActivity & EditEventActivity
Jitse van Esch, Elisa Perini & Mariah Sabioni
 */

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class CalendarUtils {
    public static LocalDate selectedDate;

    public static String cleanDate(LocalDate date) {
        // changes date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }

    public static String cleanTime(LocalTime time) {
        // changes time to ie. 13:00
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date) {
        // changes format of date from 2021-12-23 to December 2021
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        //based on: https://github.com/codeWithCal/CalendarTutorialAndroidStudio/tree/WeeklyCalendar
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = mondayForDate(selectedDate);
        LocalDate endDate = Objects.requireNonNull(current).plusWeeks(1);

        while (current.isBefore(endDate)) {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate mondayForDate(LocalDate current) {
        // also sets monday as 1st day of the week
        //based on: https://github.com/codeWithCal/CalendarTutorialAndroidStudio/tree/WeeklyCalendar
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo)) {
            if(current.getDayOfWeek() == DayOfWeek.MONDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }

    /* ----------- METHODS FOR REPO -------------- */
    // methods used to change date and time format for storing in firbase

    public static LocalDate dateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    public static LocalTime timeFromString(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, formatter);
    }

    /* ------------ METHODS FOR EXPORTING EVENTS ------------ */
    //methods to change the date and time into a format accepted by the calendar provider API

    public static int exportMinutes(String time){
        return Integer.parseInt(time.substring(time.length() - 2));
    }

    public static int exportHours(String time){
        return Integer.parseInt(time.substring(0,2));
    }

    public static int exportYear(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return Integer.parseInt(date.format(formatter));
    }

    public static int exportMonth(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        int month = Integer.parseInt(date.format(formatter));
        return month-1; // /!\ 0 = january!!!!!
    }

    public static int exportDay(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        return Integer.parseInt(date.format(formatter));
    }

}