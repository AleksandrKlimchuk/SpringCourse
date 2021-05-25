package timetable_generator.Types;


import simulator.annotations.JsonPrimitive;
import simulator.annotations.JsonPrimitiveToReport;
import simulator.annotations.JsonSerializable;
import simulator.annotations.ReportSerializable;

@JsonSerializable
@ReportSerializable
public class Ship {
    @JsonPrimitive
    @JsonPrimitiveToReport
    private String name;

    public Ship(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
