package simulator;

import timetable_generator.Types.PortEntity;

import java.util.List;

@org.springframework.stereotype.Repository
public interface Repository {
    List<PortEntity> getTimetable();
}
