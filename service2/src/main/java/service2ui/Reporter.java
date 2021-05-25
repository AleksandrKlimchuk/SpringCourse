package service2ui;

import timetable_generator.Types.PortEntity;

import java.util.ArrayList;
import java.util.List;

public class Reporter {
    private static final int ONE_HOUR_BILL = 100;
    private static final int ONE_LOADER_COST = 30000;
    private final int END_TIME = 43200;

    private List<PortEntity> report = new ArrayList<>();
    private int fine = 0;
    private int quantityOfEntities = 0;
    private double averageQueueSize = 0.0;
    private double averageWaitingTime = 0.0;
    private int maxDelay = 0;
    private double averageDelay = 0.0;
    private int requestedQuantityOfCranes;

    public Reporter(List<PortEntity> report) {
        this.report = report;
        /*quantityOfEntities = report.size();
        int sumOfWaitingTime = 0;
        int sumOfDelay = 0;
        for (PortEntity entity : report) {
            sumOfWaitingTime += entity.getRealArriveDateMinute() - entity.getUnloadingStartDateInMinutes();
            sumOfDelay += entity.
        }
        averageWaitingTime = (double) sumOfWaitingTime / quantityOfEntities;*/

    }

    public List<PortEntity> getReport() {
        return report;
    }

    public int getQuantityOfEntities() {
        return quantityOfEntities;
    }

    public int getFine() {
        return fine;
    }

    public double getAverageQueueSize() {
        return averageQueueSize;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public double getAverageDelay() {
        return averageDelay;
    }

    public void setReport(List<PortEntity> report) {
        this.report = report;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public int getRequestedQuantityOfCranes() {
        return requestedQuantityOfCranes;
    }

    public void setQuantityOfEntities(int quantityOfEntities) {
        this.quantityOfEntities = quantityOfEntities;
    }

    public void setAverageQueueSize(double averageQueueSize) {
        this.averageQueueSize = averageQueueSize;
    }

    public void setAverageWaitingTime(double averageWaitingTime) {
        this.averageWaitingTime = averageWaitingTime;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
    }

    public void setAverageDelay(double averageDelay) {
        this.averageDelay = averageDelay;
    }

    public void setRequestedQuantityOfCranes(int requestedQuantityOfCranes) {
        this.requestedQuantityOfCranes = requestedQuantityOfCranes;
    }

    public void addEntity(PortEntity entity) {
        report.add(new PortEntity(entity));
    }

    public void merge(Reporter... reports) {
        for (Reporter reporter : reports) {
            this.report.addAll(reporter.getReport());
            this.quantityOfEntities += reporter.getQuantityOfEntities();
            this.averageQueueSize += reporter.getAverageQueueSize();
            this.averageWaitingTime += reporter.getAverageWaitingTime();
            if (this.maxDelay < reporter.maxDelay) {
                this.maxDelay = reporter.maxDelay;
            }
            this.averageDelay += reporter.averageDelay;
            this.fine += reporter.getFine();
            if (this.requestedQuantityOfCranes < reporter.getRequestedQuantityOfCranes()) {
                this.requestedQuantityOfCranes = reporter.getRequestedQuantityOfCranes();
            }
        }
        this.averageDelay /= 3;
        this.averageDelay = (int)this.averageDelay;
        this.averageQueueSize /= 3;
        this.averageQueueSize = (int)this.averageQueueSize;
        this.averageWaitingTime /= 3;
        this.averageWaitingTime = (int)this.averageWaitingTime;

    }

    public void calculateStats() {
        quantityOfEntities = report.size();
        int maxDelayTemp = 0;
        for (PortEntity entity : report) {
            double delay =  entity.getUnloadingEndDateInMinutes() - entity.getUnloadingStartDateInMinutes() - entity.getUnloadingTimeInMinutes();
            if (delay > maxDelayTemp) {
                maxDelayTemp = this.maxDelay;
                this.maxDelay = entity.getUnloadingEndDateInMinutes() - entity.getUnloadingStartDateInMinutes() - entity.getUnloadingTimeInMinutes();
            }
            averageDelay += (delay > 0) ? delay / quantityOfEntities : 0;
            averageWaitingTime += ((double) entity.getUnloadingStartDateInMinutes() - (double) entity.getRealArriveDateMinute()) / quantityOfEntities;
        }
        calculateFine();
    }


    private int calculateFine() {
        fine = 0;
        for (PortEntity entity : report) {
            int delayInHour = 0;

            if (entity.getUnloadingEndDateInMinutes() < 0) {
                fine += ONE_LOADER_COST;
                continue;
            }

            if (entity.getArriveDateInMinuteByTimetable() <= entity.getRealArriveDateMinute()) {
                //arrived with delay
                delayInHour = (entity.getUnloadingEndDateInMinutes() - entity.getRealArriveDateMinute() - entity.getUnloadingTimeInMinutes()) / 60;
            } else {
                //arrived in time
                delayInHour =
                        (entity.getUnloadingEndDateInMinutes() - entity.getArriveDateInMinuteByTimetable() - entity.getUnloadingTimeInMinutes()) / 60;
            }
            fine += (delayInHour > 0) ? delayInHour * ONE_HOUR_BILL : 0;
        }
        return fine;
    }

    public void printReport() {
        System.out.println("************************************REPORT************************************");
        System.out.println("Quantity of unloading ships: " + quantityOfEntities);
        System.out.println("Average queue size: " + averageQueueSize);
        System.out.println("Average waiting time: " + averageWaitingTime);
        System.out.println("Maximum unloading delay: " + maxDelay);
        System.out.println("Average unloading delay: " + averageDelay);
        System.out.println("Total fine: " + fine);
        System.out.println("Requested quantity of cranes: " + requestedQuantityOfCranes);
        System.out.println("******************************************************************************");
    }
}
