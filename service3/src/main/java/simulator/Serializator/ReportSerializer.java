package simulator.Serializator;


import timetable_generator.Types.PortEntity;
import simulator.simulators.Reporter;

import java.util.List;

public class ReportSerializer {
    public static String serialize(Reporter report) {
        List<PortEntity> timetable = report.getReport();
        StringBuilder builder = new StringBuilder();
        try {
            for (PortEntity portEntity : timetable) {
                    builder.append(ToReportSerializer.convertToJson(portEntity) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            System.out.println("Something wrong.\n");
        }
        return builder.toString();
    }
}
