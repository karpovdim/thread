package factory;

import entyti.Vehicle;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Runner {
    public static void main(String[] args) {
        List<Vehicle> vehicles = List.of(new Vehicle(100, 150),
                new Vehicle(100, 150),
                new Vehicle(100, 150),
                new Vehicle(100, 150),
                new Vehicle(100, 150),
                new Vehicle(700, 950),
                new Vehicle(100, 150));

        runner(vehicles);
    }

    public static void runner(List<Vehicle> vehicles) {
        ExecutorService service = Executors.newCachedThreadPool();
        for (Vehicle vehicle : vehicles) {
            service.execute(new Thread(vehicle));
        }
        service.shutdown();
    }

}
