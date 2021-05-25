package simulator;


import timetable_generator.Types.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public interface ConsoleReader {
    public static PortEntity read() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Arrive day[1-30]: ");
        int day = Integer.parseInt(reader.readLine());
        System.out.print("Arrive hour[0-23]: ");
        int hour = Integer.parseInt(reader.readLine());
        System.out.print("Arrive minute[0-59]: ");
        int minute = Integer.parseInt(reader.readLine());
        System.out.print("Name of ship: ");
        String shipName = reader.readLine();
        System.out.print("Enter type of good{dry, liquid, container}: ");
        TypesOfGoods type = TypesOfGoods.getTypeByString(reader.readLine());
        System.out.print("Enter weight of good: ");
        int weight = Integer.parseInt(reader.readLine());
        return new PortEntity(new Date(day, hour, minute), new Ship(shipName), new Good(type, weight), 10);
    }
}