package service2ui;

import org.springframework.stereotype.Component;
import timetable_generator.Types.PortEntity;

import java.util.List;

@Component
public interface Repository {
    String getTimetable();

    public void writeReportToJSON(String report);

    String notify(String filename);

    public List<PortEntity> getTimetableByReport(String reportName);

}
