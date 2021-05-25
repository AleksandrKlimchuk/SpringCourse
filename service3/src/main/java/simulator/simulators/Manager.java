package simulator.simulators;

import timetable_generator.Types.PortEntity;
import simulator.deserializer.JsonDeserializer;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class Manager {
    private static final int cranePerformance = 100;
    private int quantityOfDryCranes = 5;
    private int quantityOfLiquidCranes = 5;
    private int quantityOfContainerCranes = 5;
    private static Random random = new Random();
    private Reporter report = new Reporter();
    private static final int MAX_ARRIVE_DELAY = 10080; // in minutes
    private static final int MAX_UNLOAD_DELAY = 1440; // in minutes
    private int END_TIME = 43200; // in minutes

    private List<PortEntity> timetable;

    private List<PortEntity> timetableForDry = new ArrayList<>();
    private List<PortEntity> timetableForLiquid = new ArrayList<>();
    private List<PortEntity> timetableForContainer = new ArrayList<>();

    private Phaser phaser = new Phaser(3);
    private ExecutorService simulators = Executors.newFixedThreadPool(3);

    public Manager(List<PortEntity> timetable) {
        this.timetable = timetable;
    }

    public Reporter getReport() {
        return report;
    }

    public void run() throws InterruptedException {

        setDelays(timetable);
        timetable.sort(Comparator.comparingInt(PortEntity::getRealArriveDateMinute));
        System.out.println("Before simulating: ");
        for (PortEntity entity : timetable) {
            System.out.println(entity.toStringForReport());
        }
        for (PortEntity entity : timetable) {
            switch (entity.getGood().getType()) {
                case DRY -> timetableForDry.add(entity);
                case LIQUID -> timetableForLiquid.add(entity);
                case CONTAINER -> timetableForContainer.add(entity);
            }
        }
        Simulator drySimulator = new Simulator(timetableForDry, quantityOfDryCranes, cranePerformance);
        Simulator liquidSimulator = new Simulator(timetableForLiquid, quantityOfLiquidCranes, cranePerformance);
        Simulator containerSimulator = new Simulator(timetableForContainer, quantityOfContainerCranes, cranePerformance);

        CountDownLatch countDownLatch = new CountDownLatch(3);

        startSimulator(drySimulator, quantityOfDryCranes, countDownLatch);
        startSimulator(liquidSimulator, quantityOfLiquidCranes, countDownLatch);
        startSimulator(containerSimulator, quantityOfContainerCranes, countDownLatch);

        countDownLatch.await();

        Reporter dryReport = drySimulator.getReport();
        Reporter liquidReport = liquidSimulator.getReport();
        Reporter containerReport = containerSimulator.getReport();

        /*dryReport.settingReportForJSON();
        liquidReport.settingReportForJSON();
        containerReport.settingReportForJSON();*/

        report.merge(dryReport, liquidReport, containerReport);
        report.settingReportForJSON();

        System.out.println("REPORT");
        for (PortEntity entity : report.getReport()) {
            System.out.println(entity.toStringForReport());
        }
        System.out.println();
        report.printReport();

        simulators.shutdown();
    }

    private static List<PortEntity> getTimetableFromJSON(String jsonFilename) throws IllegalAccessException, InstantiationException,
    InvocationTargetException {
        List<PortEntity> jsonTimetable = JsonDeserializer.deserialize(jsonFilename);
        return jsonTimetable;
    }

    private void setDelays(List<PortEntity> timetableForDelay) {
        for (PortEntity entity : timetableForDelay) {
            int randomFlag = random.nextInt(100);
            if (randomFlag % 5 == 0) {
                boolean isCorrectDate = false;
                while (!isCorrectDate) {
                    int arriveDelay = random.nextInt(MAX_ARRIVE_DELAY * 2) - MAX_ARRIVE_DELAY;
                    int endOfUnloadingDateWithDelay = entity.getArriveDateInMinuteByTimetable() + arriveDelay + entity.getUnloadingTimeInMinutes();
                    int arriveDateInMinuteWithDelay = entity.getArriveDateInMinuteByTimetable() + arriveDelay;
                    if ((arriveDateInMinuteWithDelay >= 0) && (arriveDateInMinuteWithDelay < END_TIME)
                            && (endOfUnloadingDateWithDelay > 0) && (endOfUnloadingDateWithDelay < END_TIME)) {
                        entity.setRealArriveDateMinute(entity.getArriveDateInMinuteByTimetable() + arriveDelay);
                        isCorrectDate = true;
                    }
                    System.out.println("a");
                }
            } else {
                entity.setRealArriveDateMinute(entity.getArriveDateInMinuteByTimetable());
            }
            randomFlag = random.nextInt();
            if (randomFlag % 5 == 0) {
                int unloadingDelay = random.nextInt(MAX_UNLOAD_DELAY);
                int unloadingEndDateWithDelay = entity.getRealArriveDateMinute() + entity.getUnloadingTimeInMinutes() + unloadingDelay;
                if (unloadingEndDateWithDelay < END_TIME) {
                    entity.setUnloadingEndDateInMinutes(unloadingDelay);
                }
            } else {
                entity.setUnloadingEndDateInMinutes(0);
            }
        }
    }

    private void startSimulator(Simulator simulator, int quantityOfCranes, CountDownLatch countDownLatch) {
        simulator.setQuantityOfCranes(quantityOfCranes);
        simulators.execute(() -> {
            phaser.arriveAndAwaitAdvance();
            simulator.run();
            countDownLatch.countDown();
        });
    }
}
