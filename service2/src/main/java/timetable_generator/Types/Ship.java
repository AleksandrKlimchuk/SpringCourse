package timetable_generator.Types;

import service2ui.annotations.JsonPrimitive;
import service2ui.annotations.JsonSerializable;
@JsonSerializable
public class Ship {
    @JsonPrimitive
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
