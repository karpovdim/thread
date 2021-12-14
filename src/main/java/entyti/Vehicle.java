package entyti;

import static entyti.State.CREATED;

public class Vehicle implements Runnable {
    private final double sqrt;
    private final double weight;
    private State state;

    public Vehicle(double sqrt, double weight) {
        this.sqrt = sqrt;
        this.weight = weight;
        this.state = CREATED;
    }

    public double getSqrt() {
        return sqrt;
    }

    public double getWeight() {
        return weight;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void run() {
        Ferry.getInstance().onBoarding(this);
    }
}

enum State {
    CREATED,
    PENDING_ONBOARD,
    ONBOARDED,
    MOVING
}