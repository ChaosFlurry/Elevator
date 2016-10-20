package elevator;

import java.time.Duration;
import java.time.Instant;

public class Elevator {
    private int currentFloor;
    private int minFloor;
    private int maxFloor;

    private int position;       // m
    private int speed;          // m/s
    private int acceleration;   // m/s^2

    private int maxSpeed;
    private int maxAcceleration;

    // TODO:
    // Names: Elevator 1L / Elevator 1R
    // Ground-up mode and Top-down for peak times

    public Elevator(int minFloor, int maxFloor) {
        // Elevator starts on ground floor (floor 0)
        this.currentFloor = 0;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getMinFloor() {
        return minFloor;
    }

    public void setMinFloor(int minFloor) {
        this.minFloor = minFloor;
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public void setMaxFloor(int maxFloor) {
        this.maxFloor = maxFloor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getMaxAcceleration() {
        return maxAcceleration;
    }

    public void setMaxAcceleration(int maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    public void details() {
        /**
         * Travelling to floor
         * ETA
         * Queue?
         * Direction?
         * Status: Travelling to... Returning to... Unused, Out of Order etc
         */
        System.out.println("Position: " + getPosition());
        if (getSpeed() > 0) {
            System.out.println("Moving: Yes");
        } else {
            System.out.println("Moving: No");
        }
        System.out.println("Speed: " + getSpeed());
        //System.out.print("Acceleration: " + getAcceleration());
        // Output formatting
        System.out.println("");
    }

    public void update(double rate) {
        position += (int) (speed * rate);
    }

    public void travelToFloor(int floor) {

    }

    public void moveToPosition(int position) {
        int distance = position - this.position;
        if (distance > 0) {
            setSpeed(10);
        } else {
            setSpeed(-10);
        }

        Instant startTime = Instant.now();
        System.out.println("Start: " + startTime);
        while (true) {
            Instant loopStart = Instant.now();
            if (this.position == position) {
                setSpeed(0);
                System.out.println("Arrived.");
                details();
                break;
            }

            try {
                Instant sleepStart = Instant.now();
                Thread.sleep(100);
                Instant sleepEnd = Instant.now();
                Duration sleepTime = Duration.ofMillis(sleepEnd.toEpochMilli() - sleepStart.toEpochMilli());
                System.out.println("Sleep duration: " + sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            update(0.1);
            details();
            Instant currentTime = Instant.now();
            Duration elapsed = Duration.ofMillis(
                    currentTime.toEpochMilli() - loopStart.toEpochMilli());
            System.out.println(elapsed);

        }
        Instant endTime = Instant.now();
        System.out.println("End: " + endTime);
        Duration elapsed = Duration.ofMillis(endTime.toEpochMilli() - startTime.toEpochMilli());
        System.out.println("Elapsed time: " + elapsed);
    }
}
