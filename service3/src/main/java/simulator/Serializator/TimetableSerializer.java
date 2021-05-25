package simulator.Serializator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TimetableSerializer {
    static  public void packJSONToFile(String JSONString, String filename) {
        FileWriter outputJsonFile = null;
        try {
            Files.deleteIfExists(Path.of(filename));
            outputJsonFile = new FileWriter(filename);
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        try {
            outputJsonFile.write(JSONString);
            outputJsonFile.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    /*static public String serialize(PortEntity[] timetable) {
        String result = null;
        try {
            for (PortEntity portEntity : timetable) {
                result.concat(ToJsonSerializer.convertToJson(portEntity) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            System.out.println("Something wrong.\n");
        }
        return result;
    }*/
}
