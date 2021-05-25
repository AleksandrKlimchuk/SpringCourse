package timetable_generator.Types;

import service2ui.annotations.JsonObject;
import service2ui.annotations.JsonPrimitive;
import service2ui.annotations.JsonSerializable;

@JsonSerializable
public final class Date {
    public static final int DAY_LOWER_BOUND = 1;
    public static final int DAY_UPPER_BOUND = 30;
    public static final int MINUTES_PER_DAY = 1440;

    @JsonPrimitive
    private int day;
    @JsonObject
    private Time time;

    public Date() {
        this.day = 0;
        this.time = new Time();
    }

    public Date(int day, Time time) {
        this.day = day;
        this.time = time;
    }

    public Date(int day, int hour, int minute) {
        this(day, new Time(hour, minute));
    }

    public Date(int minutes) {
        this.day = minutes / MINUTES_PER_DAY;
        minutes %= MINUTES_PER_DAY;
        this.time = new Time(minutes);
    }


    public int getDay() {
        return day;
    }

    public void setDay(int day) throws Exception {
        this.day = day;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setTime(int hour, int minute) throws Exception {
        this.time = new Time(hour, minute);
    }

    public int getDateInMinutes() {
        return (day * MINUTES_PER_DAY) + time.getTimeInMinutes();
    }



    @Override
    public String toString() {
        String day = (getDay() < 10) ? ("0" + getDay()) : String.valueOf(getDay());
        return day + " day " + getTime().toString();
    }
}
