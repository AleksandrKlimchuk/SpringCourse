package service2ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import service2ui.Serializator.TimetableSerializer;
import timetable_generator.Types.PortEntity;
import service2ui.deserializer.ReportDeserializer;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class RepositoryImplementation implements Repository{
    @Autowired
    RestTemplate template;

    private static final String SERVICE_ONE_URL = "http://localhost:8081/timetable";
    private static final String SERVICE_THREE_URL = "http://localhost:8083/report?filename=%s";
    private static final String TIMETABLE_FILE_NAME = "timetable.json";
    private static final String REPORT_FILE_NAME = "report.json";

    @Override
    public String getTimetable() {
        String timetableInJSONStr = template.getForObject(SERVICE_ONE_URL, String.class);
        TimetableSerializer.packJSONToFile(timetableInJSONStr, TIMETABLE_FILE_NAME);
        return TIMETABLE_FILE_NAME;
    }

    @Override
    public void writeReportToJSON(String report) {
        TimetableSerializer.packJSONToFile(report, REPORT_FILE_NAME);
    }

    @Override
    public String notify(String filename) {
        template.getForObject(String.format(SERVICE_THREE_URL, TIMETABLE_FILE_NAME), String.class);
        return "ok";
    }
//
    @Override
    public List<PortEntity> getTimetableByReport(String fileName) {
        if (!Files.exists(Path.of(fileName))) {
            throw new NullPointerException();
        }
        List<PortEntity> reportTimetable = null;
        try {
            reportTimetable = ReportDeserializer.deserialize(fileName);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return reportTimetable;
    }

    public List<PortEntity> getTimetableByReport() {
        return getTimetableByReport(REPORT_FILE_NAME);
    }
}
