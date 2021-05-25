package timetable_generator.Types;
import timetable_generator.serializer.annotations.DeserializeConstructor;
import timetable_generator.serializer.annotations.JsonObject;
import timetable_generator.serializer.annotations.JsonPrimitive;
import timetable_generator.serializer.annotations.JsonSerializable;

import java.util.ArrayDeque;
import java.util.Scanner;

@JsonSerializable
public class PortEntity {
    @JsonObject
    private Date date;
    @JsonObject
    private Ship ship;
    @JsonObject
    private Good good;
    @JsonObject
    private Time parkingTime;

    private int arriveDateMinute;
    private int realArriveDateMinute;
    private int unloadingTimeInMinutes;
    private int unloadingStartDateInMinutes = 0;
    private int unloadingEndDateInMinutes = 0;

    public PortEntity(Date date, Ship ship, Good good, int cranePerformance) {
        try {
            this.date = date;
            this.arriveDateMinute = date.getDateInMinutes();

            this.ship = ship;
            this.good = good;

            TypesOfGoods type = good.getType();
            this.parkingTime = computeParkingTime(good.getWeight(), cranePerformance); /*Need service3*/
            this.unloadingTimeInMinutes = parkingTime.getTimeInMinutes();
        }
        catch (Exception error) {
            System.out.println(error.toString() +
                    "\nYou can't create PortEntity with current parameters\n");
        }
    }

    public PortEntity(PortEntity other) {
        this.date = other.date;
        this.ship = other.ship;
        this.good = other.good;
        this.parkingTime = other.parkingTime;
        this.arriveDateMinute = other.arriveDateMinute;
        this.realArriveDateMinute = other.realArriveDateMinute;
        this.unloadingTimeInMinutes = other.unloadingTimeInMinutes;
        this.unloadingStartDateInMinutes = other.unloadingStartDateInMinutes;
        this.unloadingEndDateInMinutes = other.unloadingEndDateInMinutes;
    }

    @DeserializeConstructor
    public PortEntity(String JsonString) {
        Scanner localScanner = new Scanner(JsonString);
        ArrayDeque<String> deque = new ArrayDeque<>(6);
        int day = Integer.parseInt(localScanner.next());
        int hour = Integer.parseInt(localScanner.next());
        int minute = Integer.parseInt(localScanner.next());
        String name = localScanner.next();
        TypesOfGoods type = TypesOfGoods.getTypeByString(localScanner.next());
        int weight = Integer.parseInt(localScanner.next());
        int parkingHour = Integer.parseInt(localScanner.next());
        int parkingMinute = Integer.parseInt(localScanner.next());
        this.date = new Date(day, hour, minute);
        this.ship = new Ship(name);
        this.good = new Good(type, weight);
        this.parkingTime = new Time(parkingHour, parkingMinute);
        this.arriveDateMinute = this.date.getDateInMinutes();
        this.unloadingTimeInMinutes = this.parkingTime.getTimeInMinutes();
    }

    public void increaseArrivalDate(int minutes) {
        arriveDateMinute += minutes;
        date = new Date(arriveDateMinute);
    }

    public Date getDate() {
        return date;
    }

    public Ship getShip() {
        return ship;
    }

    public Good getGood() {
        return good;
    }

    public Time getParkingTime() {
        return parkingTime;
    }

    public int getArriveDateInMinuteByTimetable() {
        return arriveDateMinute;
    }

    public int getRealArriveDateMinute() {
        return realArriveDateMinute;
    }

    public int getUnloadingTimeInMinutes() {
        return unloadingTimeInMinutes;
    }

    public int getUnloadingEndDateInMinutes() {
        return unloadingEndDateInMinutes;
    }

    public int getUnloadingStartDateInMinutes() {
        return unloadingStartDateInMinutes;
    }

    public void setRealArriveDateMinute(int minute) {
        realArriveDateMinute = minute;
    }

    public void setUnloadingEndDateInMinutes(int minutes) {
        unloadingEndDateInMinutes = minutes;
    }

    public void setUnloadingStartDateInMinutes(int minutes) {
        unloadingStartDateInMinutes = minutes;
    }

    private Time computeParkingTime(int weight, int performance) {
        int minutes = (int) Math.floor((double) weight / performance);
        int hours = minutes / (Time.MINUTE_UPPER_BOUND + 1);
        minutes %= Time.MINUTE_UPPER_BOUND + 1;
        return new Time(hours, minutes);
    }

    @Override
    public String toString() {
        return date.toString() + " " + ship.toString() + " " + good.toString() +
                " Parking time: " + parkingTime.toString();
    }

    public String toStringForReport() {
        /*date.toString() + " " + ship.toString() + " " + good.toString() +
                " Parking time: " + parkingTime.toString() + " Arrive date: " + new Date(arriveDateMinute).toString() +
                " Real arrive date: " + new Date(realArriveDateMinute).toString() +
                " Unloading time: " + new Time(unloadingTimeInMinutes).toString() +
                " Unloading start: " + new Date(unloadingStartDateInMinutes).toString() +
                " Unloading end: " + new Date(unloadingEndDateInMinutes);*/
        return ship.toString() + " " + "Arrival time: " + new Date(realArriveDateMinute).toString() +
                " Waiting time: " + new Date(unloadingStartDateInMinutes - realArriveDateMinute).toString() +
                " Unloading start date: " + new Date(unloadingStartDateInMinutes).toString() +
                " Unloading time: " + new Time(unloadingEndDateInMinutes - unloadingStartDateInMinutes).toString();
    }
}
