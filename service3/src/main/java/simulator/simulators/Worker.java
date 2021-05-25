package simulator.simulators;


import timetable_generator.Types.PortEntity;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker {
    private PortEntity entity = null;
    private final ConcurrentLinkedQueue<PortEntity> arrivedEntities;
    private volatile int quantityOfCranes = 0;
    private final int cranePerformance;
    private final AtomicInteger currentWeight = new AtomicInteger(0);
    private int currentDate;
    private int unloadingDelay = 0;

    public Worker(ConcurrentLinkedQueue<PortEntity> arrivedEntities, int cranePerformance, int currentDate) {
        this.arrivedEntities = arrivedEntities;
        this.cranePerformance = cranePerformance;
        this.currentDate = currentDate;
    }

    public PortEntity getEntity() {
        return entity;
    }

    public boolean isWork() {
        return entity != null;
    }

    public synchronized boolean setCrane() {
        if ((quantityOfCranes < 2) && isWork()) {
            ++quantityOfCranes;
            return true;
        }
        return false;
    }

    public void work() {
        int weight = -1;
        int result = -1;
        do {
            weight = currentWeight.get();
            result = weight - cranePerformance;
        } while (!currentWeight.compareAndSet(weight, result)); // Remove 2nd condition
    }

    public boolean isUnloaded() {
        return (currentWeight.get() <= 0) && (unloadingDelay <= 0);
    }

    synchronized public void releaseCrane() {
        --quantityOfCranes;
    }

    public PortEntity update() {
        PortEntity result = null;
        if ((currentWeight.get() <= 0) && (unloadingDelay > 0)) {
            --unloadingDelay;
        }
        if (isWork() && isUnloaded()) {
            entity.setUnloadingEndDateInMinutes(currentDate);
            result = this.entity;
            this.entity = null;
        }
        if (!isWork()) {
            this.entity = arrivedEntities.poll();
            if (this.entity != null) {
                this.entity.setUnloadingStartDateInMinutes(currentDate);
            }
            unloadingDelay = (this.entity != null) ? this.entity.getUnloadingEndDateInMinutes() : 0;
            currentWeight.set((this.entity != null) ? (this.entity.getGood().getWeight()) : 0);
        }
        ++currentDate;
        return result;
    }
}