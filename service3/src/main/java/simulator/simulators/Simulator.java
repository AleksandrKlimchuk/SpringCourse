package simulator.simulators;


import timetable_generator.Types.PortEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Phaser;

public class Simulator {
    private int quantityOfCranes;
    private final int cranePerformance;
    private final int END_TIME = 43200;
    private final int CRANE_COST = 30000;
    private List<Thread> cranes;
    private List<Worker> workers;
    private Reporter report;
    private Reporter oldReport;
    private ConcurrentLinkedQueue<PortEntity> arrivedEntities;
    private int currentDate = 0;
    private volatile boolean canWork = true;
    private final List<PortEntity> timetable;
    private boolean notAllServed;

    public Simulator(List<PortEntity> timetable, int quantityOfCranes, int cranePerformance) {
        this.timetable = timetable;
        this.quantityOfCranes = quantityOfCranes;
        this.cranePerformance = cranePerformance;
        this.oldReport = new Reporter();
    }

    public Reporter getReport() {
        return oldReport;
    }

    public void setQuantityOfCranes(int quantityOfCranes) {
        this.quantityOfCranes = quantityOfCranes;
    }

    public void run() {
        int difference = 0;
        while (true) {
            currentDate = 0;
            arrivedEntities = new ConcurrentLinkedQueue<>();
            cranes = new ArrayList<>();
            workers = new ArrayList<>();
            report = new Reporter();
            Phaser phaser = new Phaser(quantityOfCranes + 1);

            startCranes(phaser);
            startMainCycle(new ArrayList<>(timetable), phaser);
            interruptThreads();

            report.calculateStats();

            int newFine = report.getFine() + (quantityOfCranes - 1) * CRANE_COST;
            int oldFine = oldReport.getFine() + (quantityOfCranes - 2) * CRANE_COST;
            if ((newFine - oldFine) == difference) {
                if (oldReport.getReport().isEmpty()) {
                    oldReport = report;
                }
                oldReport.setRequestedQuantityOfCranes(quantityOfCranes);
                break;
            }
            if (!notAllServed && (newFine > oldFine)) {
                if (oldReport.getReport().isEmpty()) {
                    oldReport = report;
                }
                oldReport.setRequestedQuantityOfCranes(quantityOfCranes);
                break;
            }
            report.setRequestedQuantityOfCranes(quantityOfCranes);
            difference = newFine - oldFine;
            ++quantityOfCranes;
            System.out.println(newFine + " " + oldFine);
            oldReport = report;
        }
    }


    private int takeWorker() {
        for (int i = 0; i < quantityOfCranes / 2 + 1; ++i) {
            if (workers.get(i).setCrane()) {
                return i;
            }
        }
        return -1;
    }

    private void startCranes(Phaser phaser) {
        for (int i = 0; i < quantityOfCranes; ++i) {
            if (i < (quantityOfCranes / 2 + 1)) {
                workers.add(new Worker(arrivedEntities, cranePerformance, currentDate));
            }
            cranes.add(new Thread(() -> {
                threadCycle(phaser);
            }));
            cranes.get(i).start();;
        }
    }

    private void threadCycle(Phaser phaser) {
        int indexOfWorker = -1;
        while (true) {
            phaser.arriveAndAwaitAdvance();
            if (!canWork) {
                break;
            }
            if ((indexOfWorker >= 0) && (workers.get(indexOfWorker).isUnloaded() || !workers.get(indexOfWorker).isWork())) {
                workers.get(indexOfWorker).releaseCrane();
                indexOfWorker = -1;
            }
            if (indexOfWorker < 0) {
                indexOfWorker = takeWorker();
            }
            if (indexOfWorker >= 0) {
                workers.get(indexOfWorker).work();
            }
            phaser.arriveAndAwaitAdvance();
        }
    }

    private void startMainCycle(List<PortEntity> actualTimetable, Phaser phaser) {
        canWork = true;
        while (currentDate < END_TIME) {
            while (!actualTimetable.isEmpty() && (actualTimetable.get(0).getRealArriveDateMinute() == currentDate)) {
                arrivedEntities.add(new PortEntity(actualTimetable.remove(0)));
            }
            for (int i = 0 ; i < quantityOfCranes / 2 + 1; ++i) {
                PortEntity entityState = workers.get(i).update();
                if (entityState != null) {
                    report.addEntity(entityState);
                }
            }
            report.setAverageQueueSize(report.getAverageQueueSize() + ((double) arrivedEntities.size() / (double) END_TIME));
            phaser.arriveAndAwaitAdvance();
            ++currentDate;
            phaser.arriveAndAwaitAdvance();
        }
        canWork = false;
        phaser.arriveAndAwaitAdvance();
        notAllServed = false;
        for (int i = 0; i < quantityOfCranes / 2 + 1; ++i) {
            PortEntity entity = workers.get(i).getEntity();
            if ((entity != null) && !workers.get(i).isUnloaded()) {
                entity.setUnloadingEndDateInMinutes(-1);
                report.addEntity(entity);
                notAllServed = true;
            }
        }

        for (PortEntity entity : arrivedEntities) {
            entity.setUnloadingEndDateInMinutes(-2);
            report.addEntity(entity);
            notAllServed = true;
        }
    }

    private void interruptThreads() {
        for (int i = 0; i < quantityOfCranes; ++i) {
            cranes.get(i).interrupt();
        }
    }
}