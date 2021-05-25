package timetable_generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import timetable_generator.generator.TimetableGenerator;
import timetable_generator.serializer.Serializator.TimetableSerializer;

@Controller
public class GeneratorGet {
    private TimetableGenerator timetableGenerator = new TimetableGenerator();

    @GetMapping("/timetable")
    @ResponseBody
    public String getTimetable() {

        String serializedString = TimetableSerializer.serialize(timetableGenerator.getTimetable());
        System.out.println(serializedString);
        return serializedString;
    }
}
