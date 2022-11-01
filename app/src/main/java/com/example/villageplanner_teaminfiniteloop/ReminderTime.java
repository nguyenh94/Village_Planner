package com.example.villageplanner_teaminfiniteloop;

public class ReminderTime {
    private Integer Hours = 0;
    private Integer Minutes = 0;

    public ReminderTime(Integer hours, Integer minutes) {
        this.Hours = hours;
        this.Minutes = minutes;
    }
    public ReminderTime() {
    }

    public Integer getHours() {
        return this.Hours;
    }

    public Integer getMinutes() {
        return this.Minutes;
    }
    public void setHours(Integer hours) {
        this.Hours = hours;
    }

    public void setMinutes(Integer minutes) {
        this.Hours = minutes;
    }
}

