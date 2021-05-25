package timetable_generator.serializer.Serializator;

import timetable_generator.Types.PortEntity;

import java.io.FileWriter;
import java.io.IOException;

public class TimetableSerializer {
    static  public void serialize(PortEntity[] timetable, String filename) {
        FileWriter outputJsonFile = null;
        try {
            outputJsonFile = new FileWriter(filename);
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        /* try {
            for (PortEntity portEntity : timetable) {
                if (outputJsonFile != null) {
                    outputJsonFile.write(ToJsonSerializer.convertToJson(portEntity));
                    outputJsonFile.write("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            System.out.println("Something wrong.\n");
        }*/

        String JSONResult = serialize(timetable);
        try {
            outputJsonFile.write(JSONResult);
            outputJsonFile.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    static public String serialize(PortEntity[] timetable) {
        StringBuilder builder = new StringBuilder();
        try {
            for (PortEntity portEntity : timetable) {
                builder.append(ToJsonSerializer.convertToJson(portEntity) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            System.out.println("Something wrong.\n");
        }
        return builder.toString();
    }
}
