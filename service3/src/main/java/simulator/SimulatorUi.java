package simulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import simulator.simulators.Manager;
import simulator.simulators.Reporter;


@Controller
public class SimulatorUi {
    @Autowired
    private RepositoryImpl repository;

    @GetMapping("/report")
    @ResponseBody
    public Reporter getReport(@RequestParam(value = "filename") String filename) {
        System.out.println(filename);
        Manager manager = new Manager(repository.getTimetable());
        try {
            manager.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repository.sendReport(manager.getReport());
        return manager.getReport();
    }
}
