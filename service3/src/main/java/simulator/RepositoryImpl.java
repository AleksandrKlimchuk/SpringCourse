package simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import simulator.Serializator.ReportSerializer;
import timetable_generator.Types.PortEntity;
import simulator.deserializer.JsonDeserializer;
import simulator.simulators.Reporter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@org.springframework.stereotype.Repository
public class RepositoryImpl implements Repository {

    private static final String SERVICE_TWO_SCHEDULE_URL = "http://localhost:8082/timetable?filename=json.json";
    private static final String SERVICE_TWO_RESULT_URL = "http://localhost:8082/results?filename=report.json";

    @Autowired
    RestTemplate templates;

    @Override
    public List<PortEntity> getTimetable() {
        String JSONString = templates.getForObject(SERVICE_TWO_SCHEDULE_URL, String.class);
        System.out.println(JSONString);
        List<PortEntity> timetable = null;
        try {
             timetable = JsonDeserializer.deserialize(JSONString);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return timetable;
    }

    public void sendReport(Reporter report) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String JSONString = ReportSerializer.serialize(report);
        HttpEntity<String> httpEntity = new HttpEntity<>(JSONString, headers);
        templates.postForObject(SERVICE_TWO_RESULT_URL, httpEntity, String.class);
    }
}
