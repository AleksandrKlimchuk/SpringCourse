package timetable_generator.serializer.Serializator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportSerializer {
    /*public static void serialize(Reporter report, String filename) {
        List<PortEntity> timetable = report.getReport();
        FileWriter outputJsonFile = null;
        try {
            outputJsonFile = new FileWriter(filename);
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        try {
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
        }
        try {
            outputJsonFile.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }*/
}
