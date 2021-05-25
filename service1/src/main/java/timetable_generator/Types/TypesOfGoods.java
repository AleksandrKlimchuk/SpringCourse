package timetable_generator.Types;

import timetable_generator.serializer.annotations.JsonObject;
import timetable_generator.serializer.annotations.JsonPrimitive;
import timetable_generator.serializer.annotations.JsonSerializable;

import java.util.Locale;

@JsonSerializable
public enum TypesOfGoods {
    DRY(0, "dry"),
    LIQUID(1, "liquid"),
    CONTAINER(2, "container");

    private final Integer index;
    @JsonPrimitive
    private final String typeDescription;

    TypesOfGoods(Integer value, String type) {
        index = value;
        typeDescription = type;
    }

    public static TypesOfGoods getTypeByString(String type) {
        type.toLowerCase(Locale.ROOT);
        TypesOfGoods result = switch(type) {
            case  "dry" -> TypesOfGoods.DRY;
            case "liquid" -> TypesOfGoods.LIQUID;
            case "container" -> TypesOfGoods.CONTAINER;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        return result;
    }

    @Override
    public String toString() {
        return typeDescription;
    }
}