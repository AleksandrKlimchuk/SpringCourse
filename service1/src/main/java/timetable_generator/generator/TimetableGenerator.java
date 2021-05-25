package timetable_generator.generator;

import org.springframework.stereotype.Repository;
import timetable_generator.Types.*;

import java.util.Random;

@Repository
public class TimetableGenerator {

    public static final int CRANE_PERFORMANCE = 10;
    public static final int SHIPS_PER_DAY = 10;

    private PortEntity[] timetable;
    private static final Random random  = new Random(System.currentTimeMillis());
    private int cranePerformance;
    private int shipsPerDay;

    public TimetableGenerator() {
        this.cranePerformance = CRANE_PERFORMANCE;
        this.shipsPerDay = SHIPS_PER_DAY;
        generate();
    }

    private void generate() {
        int quantityOfShips = 0;
        int[] days = new int[Date.DAY_UPPER_BOUND];
        for (int i = 0; i < 30; ++i) {
            days[i] = random.nextInt(shipsPerDay + 1);
            quantityOfShips += days[i];
        }

        int indexOfShip = 1;
        int indexInTimeTable = 0;

        timetable = new PortEntity[quantityOfShips];
        for (int day = 0; day < Date.DAY_UPPER_BOUND; ++day) {
            if (days[day] != 0) {
                for (int i = 0; i < days[day]; i++) {
                    Date date;
                    try {
                        date = new Date(day + 1, new Time(random.nextInt(Time.HOUR_UPPER_BOUND),
                                random.nextInt(Time.MINUTE_UPPER_BOUND)));
                    } catch (Exception error) {
                        System.out.println(error.toString() + "\nInvalid Date\n");
                        continue;
                    }
                    Ship ship = new Ship("Ship" + indexOfShip++);
                    int randomForType = random.nextInt(3);
                    TypesOfGoods type;
                    try {
                        type = switch(randomForType) {
                            case 0 -> TypesOfGoods.DRY;
                            case 1 -> TypesOfGoods.LIQUID;
                            case 2 -> TypesOfGoods.CONTAINER;
                            default -> throw new IllegalStateException("Unexpected value: " + randomForType);
                        };
                    } catch (Exception error) {
                        System.out.println(error.toString() + "\nYou can't create good with current parameters\n");
                        continue;
                    }
                    int weight = switch (type) {
                        case DRY -> randomDryWeight();
                        case LIQUID -> randomLiquidWeight();
                        case CONTAINER -> randomContainerWeight();
                    };
                    Good good;
                    try {
                        good = new Good(type, weight);
                    } catch (Exception error) {
                        System.out.println(error.toString() + "Invalid parameters for good");
                        continue;
                    }
                    timetable[indexInTimeTable++] = new PortEntity(date, ship, good, cranePerformance);
                }
            }
        }
        sort();
    }

    private int randomContainerWeight() {
        final int lowerBound = 4000;
        final int upperBound = 18000;
        return lowerBound + random.nextInt(upperBound - lowerBound + 1);
    }

    private int randomLiquidWeight() {
        final int lowerBound = 15000;
        final int upperBound = 30000;
        return lowerBound + random.nextInt(upperBound - lowerBound + 1);
    }

    private int randomDryWeight() {
        final int lowerBound = 10000;
        final int upperBound = 20000;
        return lowerBound + random.nextInt(upperBound - lowerBound + 1);
    }

    public void print() {
        for (PortEntity entity : timetable) {
            System.out.println(entity.toString());
        }
    }

    public PortEntity[] getTimetable() {
        return timetable;
    }

    private void sort() {
        boolean needIteration = true;
        while (needIteration) {
            needIteration = false;
            for (int i = 1; i < timetable.length; ++i) {
                Date currentDate = timetable[i].getDate();
                Date previousDate = timetable[i - 1].getDate();
                if (currentDate.getDay() ==  previousDate.getDay()) {
                    Time currentTime = currentDate.getTime();
                    Time previousTime = previousDate.getTime();
                    if (currentTime.getHour() < previousTime.getHour()) {
                        swap(i, i - 1);
                        needIteration = true;
                    }
                    else if (currentTime.getHour() == previousTime.getHour()) {
                        if (currentTime.getMinute() < previousTime.getMinute()) {
                            swap(i, i - 1);
                            needIteration = true;
                        }
                    }
                }
            }
        }
    }

    private void swap(int index1, int index2) {
        PortEntity tmp = timetable[index1];
        timetable[index1] = timetable[index2];
        timetable[index2] = tmp;
    }

}
