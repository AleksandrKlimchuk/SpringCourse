package service2ui.deserializer;

import timetable_generator.Types.PortEntity;
import service2ui.annotations.ReportContructor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public interface ReportDeserializer {
    public static List<PortEntity> deserialize(String fileName) throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
        FileReader inputJsonFile = null;
        try {
            inputJsonFile = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(inputJsonFile);
        List<PortEntity> list = new ArrayList<>();
        while (scanner.hasNext()) {
            String className = scanner.next();
            try {
                className = getClassNameCorrectness(className);
            } catch (Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
            String JsonArgument = convertJsonToString(scanner);
            Object object = null;
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                constructor.setAccessible(true);
                if (constructor.isAnnotationPresent(ReportContructor.class)) {
                    object = constructor.newInstance(JsonArgument, true);
                }
            }
            PortEntity result  = (PortEntity) object;
            list.add(result);
        }
        return list;
    }

    private static String convertJsonToString(Scanner scanner) {
        StringBuilder builder = new StringBuilder();
        int braces = 0;
        if (scanner.hasNext()) {
            String temp = scanner.next();
            if (temp.equals("{")) {
                ++braces;
            }
        }
        while(scanner.hasNext() && braces != 0) {
            String temp = scanner.next();
            if (temp.equals("{")) {
                ++braces;
            }
            if (temp.indexOf("}") != -1) {
                --braces;
            }
            StringBuilder tempBuilder = new StringBuilder(temp);
            if (tempBuilder.indexOf("\"") != -1) {
                tempBuilder.deleteCharAt(tempBuilder.indexOf("\""));
            }
            if (tempBuilder.indexOf("\"") != -1) {
                tempBuilder.deleteCharAt(tempBuilder.indexOf("\""));
            }
            if (tempBuilder.indexOf("\"") != -1 && tempBuilder.lastIndexOf("\"") != -1) {
                temp = tempBuilder.substring(tempBuilder.indexOf("\"") + 1, tempBuilder.lastIndexOf("\""));
                builder.append(temp + " ");
            }
        }
        return builder.toString();
    }

    private static String getClassNameCorrectness(String className) throws Exception {
        int start = className.indexOf("\"") + 1;
        int end = className.lastIndexOf("\"");
        if (start < 0 || end < 0)
            throw new Exception("Invalid Json format");
        return className.substring(start,end);
    }
}
