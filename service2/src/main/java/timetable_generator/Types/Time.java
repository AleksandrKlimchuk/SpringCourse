package timetable_generator.Types;

import service2ui.annotations.JsonPrimitive;
import service2ui.annotations.JsonSerializable;

@JsonSerializable
public final class Time {
    public final static int HOUR_LOWER_BOUND = 0;
    public final static int HOUR_UPPER_BOUND = 23;
    public final static int MINUTE_LOWER_BOUND = 0;
    public final static int MINUTE_UPPER_BOUND = 59;
    public final static int MINUTE_PER_HOUR = 60;

    @JsonPrimitive
    private int hour;
    @JsonPrimitive
    private int minute;

    public Time() {
        this.hour = MINUTE_LOWER_BOUND;
        this.minute = MINUTE_LOWER_BOUND;
    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Time(int minute) {
        this.hour = minute / MINUTE_PER_HOUR;
        this.minute = minute % MINUTE_PER_HOUR;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getTimeInMinutes() {
        return (hour * MINUTE_PER_HOUR) + minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setByTime(Time newTime) {
        hour = newTime.hour;
        minute = newTime.minute;
    }

    public void increase() {
        if (minute == MINUTE_UPPER_BOUND) {
            minute = MINUTE_LOWER_BOUND;
            if (hour == HOUR_UPPER_BOUND) {
                hour = HOUR_LOWER_BOUND;
            } else {
                ++hour;
            }
        } else {
            ++minute;
        }
    }

    public void decrease() {
        if (minute == MINUTE_LOWER_BOUND) {
            minute = MINUTE_UPPER_BOUND;
            if (hour == HOUR_LOWER_BOUND) {
                hour = HOUR_UPPER_BOUND;
            } else {
                --hour;
            }
        } else {
            --minute;
        }
    }

    public boolean isNewDay() {
        return (hour == HOUR_LOWER_BOUND) && (minute == MINUTE_LOWER_BOUND);
    }

    public boolean equals(Time time) {
        return (this.hour == time.hour) && (this.minute == time.minute);
    }

    public boolean lessEquals(Time time){
        if (hour <= time.getHour()) {
            if (minute <= time.getMinute()) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String hour = (getHour() < 10) ? ("0" + getHour()) : String.valueOf((getHour()));
        String minute = (getMinute() < 10) ? ("0" + getMinute()) : String.valueOf(getMinute());
        return hour + ":" + minute;
    }
}
