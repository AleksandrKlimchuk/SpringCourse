package timetable_generator.Types;

import timetable_generator.serializer.annotations.JsonObject;
import timetable_generator.serializer.annotations.JsonPrimitive;
import timetable_generator.serializer.annotations.JsonSerializable;

@JsonSerializable
public class Good {
    public static final int WEIGHT_LOWER_BOUND = 0;

    @JsonObject
    private TypesOfGoods type;
    @JsonPrimitive
    private int weight;

    public Good(TypesOfGoods type, int weight) {
        this.type = type;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public TypesOfGoods getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Type of good: " + getType().toString() +
                " Weight: " + weight +  " " + (type == TypesOfGoods.CONTAINER ? "pieces" : "ton");
    }
}
