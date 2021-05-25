package service2ui.Serializator;

import service2ui.annotations.JsonObject;
import service2ui.annotations.JsonPrimitive;
import service2ui.annotations.JsonSerializable;

import java.lang.reflect.Field;
import java.util.Objects;

public interface ToJsonSerializer {

    static private String convertPrimitiveToJson(Field field, Object object, int nesting) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(addWhitespaces(nesting));
        builder.append("\"");
        builder.append(field.getName());
        builder.append("\"");
        builder.append(":");
        builder.append("\"");
        builder.append(field.get(object));
        builder.append("\"");
        builder.append(",");
        return builder.toString();
    }

    static private String convertObjectToJson(Object object, int nesting) throws Exception {
        Class c = object.getClass();
        StringBuilder builder = new StringBuilder();
        builder.append(addWhitespaces(nesting)).append("\"").append(c.getName()).append("\"")
                .append(":").append("\n")
                .append(addWhitespaces(nesting)).append("{\n");

        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(JsonObject.class)) {
                builder.append(convertObjectToJson(field.get(object), nesting + 1));
            }
            else if (field.isAnnotationPresent(JsonPrimitive.class)) {
                builder.append(convertPrimitiveToJson(field, object, nesting + 1))
                        .append("\n");
            }
        }
        builder.append(addWhitespaces(nesting)).append("}");
        /*In addWhitespaces method we add 4 whitespaces by 1 nesting. And we need remember about '\n'*/
        if (builder.lastIndexOf(",") == builder.lastIndexOf("}") - (nesting * 4) - 2) {
            builder.deleteCharAt(builder.lastIndexOf(","));
        }
        builder.append(",\n");
        return builder.toString();
    }

    static public String convertToJson(Object object) throws Exception {
        checkIfSerializable(object);
        StringBuilder builder = new StringBuilder();
        builder.append(convertObjectToJson(object, 0));

        builder.deleteCharAt(builder.indexOf(":"));
        builder.deleteCharAt(builder.lastIndexOf("\n"));
        builder.deleteCharAt(builder.lastIndexOf(","));
        return builder.toString();
    }

    private static void checkIfSerializable(Object object) throws Exception {
        if (Objects.isNull(object)) {
            throw new Exception("Object is null");
        }

        Class c = object.getClass();
        if (!c.isAnnotationPresent(JsonSerializable.class)) {
            throw new Exception(c.getName() + "is not JsonSerializable");
        }
    }

    private static String addWhitespaces(int quantity) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < quantity; ++i) {
            builder.append("    ");
        }
        return builder.toString();
    }
}
