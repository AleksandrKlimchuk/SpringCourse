package timetable_generator.Types;
import timetable_generator.serializer.annotations.JsonObject;
import timetable_generator.serializer.annotations.JsonPrimitive;
import timetable_generator.serializer.annotations.JsonSerializable;

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
