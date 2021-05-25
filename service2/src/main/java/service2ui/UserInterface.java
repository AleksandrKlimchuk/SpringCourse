package service2ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import timetable_generator.Types.PortEntity;

import java.util.List;

@Controller
public class UserInterface {
    @Autowired
    RepositoryImplementation repository;

    @GetMapping("/start")
    public String startWork() {
        String filename = repository.getTimetable();
        repository.notify(filename);
        return "redirect:/reports";
    }

    @PostMapping("/results")
    @ResponseBody
    public String getResult(@RequestBody String report, @RequestParam(name = "filename") String filename) {
        repository.writeReportToJSON(report);
        System.out.println(report);
        return "ok";
    }

    @GetMapping("/reports")
    @ResponseBody
    public String showResults() {
        List<PortEntity> report = repository.getTimetableByReport();
        System.out.println(report);
        return "ok";
    }

    @GetMapping("/timetable")
    @ResponseBody
    public String getData(@RequestParam(value = "filename") String filename) {
        return repository.getTimetable();
    }
}
