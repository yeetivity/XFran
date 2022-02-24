package kth.jjve.xfran.models;
/*
Function: defines event (event == logged workout)
Activity: EventActivity
Jitse van Esch, Elisa Perini & Mariah Sabioni
 */

import java.time.LocalDate;
import java.time.LocalTime;

public class EventInApp {

    private String name;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public EventInApp(String name, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public EventInApp() {
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }

    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }

    public void setEndTime(LocalTime time) { this.endTime = time; }


}
