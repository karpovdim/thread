package entyti;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;


import static entyti.State.*;

public class Ferry {
    private static final Logger LOGGER = LogManager.getLogger(Ferry.class);
    private static final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private static Ferry instance;
    private final double initSqrt = 1000;
    private double sqrt = initSqrt;
    private final double initWeight = 1500;
    private double weight = initWeight;
    private final ArrayList<Vehicle> vehicles = new ArrayList<>();
    private final ReentrantLock locker = new ReentrantLock();


    private Ferry() {
    }

    public static Ferry getInstance() {
        while (instance == null) {
            if (isInitialized.compareAndSet(false, true)) {
                instance = new Ferry();
            }
        }
        return instance;
    }

    public void onBoarding(Vehicle vehicle) {
        try {
            locker.lock();
            LOGGER.info("vehicle {} is on boarding ", vehicle);
            if (capacityCheck(vehicle)) {
                onboard(vehicle);
            } else {
                LOGGER.warn(" ferry have not empty space  {}", vehicle);
                vehicle.setState(PENDING_ONBOARD);
                moving();
                release();
                onBoarding(vehicle);
            }
        } catch (InterruptedException exception) {
            LOGGER.error("vehicle name - {} is not on boarding {} ", vehicle, exception);
        } finally {
            locker.unlock();
        }
    }

    private void release() {
        vehicles.clear();
        sqrt = initSqrt;
        weight = initWeight;
        LOGGER.info("vehicles is release");
    }

    private void moving() throws InterruptedException {
        vehicles.forEach(vehicle -> {
            vehicle.setState(MOVING);
            LOGGER.info("vehicle {} is moving", vehicle);
        });
        LOGGER.info("vehicles are moving");

        TimeUnit.SECONDS.sleep(5);
    }

    private void onboard(Vehicle vehicle) {
        vehicles.add(vehicle);
        sqrt -= vehicle.getSqrt();
        weight -= vehicle.getWeight();
        vehicle.setState(ONBOARDED);
        LOGGER.info(" vehicle {} is on board", vehicle);
    }

    private boolean capacityCheck(Vehicle vehicle) {
        boolean state = sqrt >= vehicle.getSqrt() && weight >= vehicle.getWeight();
        return state;
    }

}
