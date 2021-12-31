package kth.jjve.xfran.weeklycalendar;

/*
Function: useful methods for the calendar
Activity: CalendarViewActivity
Jitse van Esch, Elisa Perini & Mariah Sabioni
based on: https://github.com/codeWithCal/CalendarTutorialAndroidStudio/tree/WeeklyCalendar
 */

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = mondayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate))
        {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate mondayForDate(LocalDate current) {
        // also sets monday as 1st day of the week
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo)) {
            if(current.getDayOfWeek() == DayOfWeek.MONDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }

    /* ------------ METHODS FOR EXPORTING EVENTS ------------ */

    public static int exportMinutes(String time){
        return Integer.parseInt(time.substring(time.length() - 2));
    }

    public static int exportHours(String time){
        return Integer.parseInt(time.substring(0,2));
    }


}