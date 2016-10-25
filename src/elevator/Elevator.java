package elevator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

public class Elevator {
    private int currentFloor;
    private int minFloor;
    private int maxFloor;

    private double time;            // s
    private double position;        // m
    private double speed;           // m/s
    private double acceleration;    // m/s^2

    private double maxSpeed;
    private double maxAcceleration;

    // TODO:
    // Names: Elevator 1L / Elevator 1R
    // Ground-up mode and Top-down for peak times

    public Elevator(int minFloor, int maxFloor) {
        // Elevator starts on ground floor (floor 0)
        this.currentFloor = 0;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.time = 0;
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

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getMaxAcceleration() {
        return maxAcceleration;
    }

    public void setMaxAcceleration(double maxAcceleration) {
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
        System.out.println("Acceleration: " + getAcceleration());
        // Output formatting
        System.out.println("");
    }

    public void update(double rate) {
        // time taken to reach top speed: 1s
        /*
        if total d is less than d/2, accelerate for half the time and slow down for the other half
         */

        time = BigDecimal.valueOf(time)
                .add(BigDecimal.valueOf(rate))
                .doubleValue();

        //position += speed * rate + 0.5 * acceleration * Math.pow(rate, 2);
        position = BigDecimal.valueOf(position)
                .add(BigDecimal.valueOf(speed)
                .multiply(BigDecimal.valueOf(rate))
                .add(BigDecimal.valueOf(0.5)
                .multiply(BigDecimal.valueOf(acceleration))
                .multiply(BigDecimal.valueOf(rate).pow(2))))
                .doubleValue();

        //speed += acceleration * rate;
        speed = BigDecimal.valueOf(speed)
                .add(BigDecimal.valueOf(acceleration)
                .multiply(BigDecimal.valueOf(rate)))
                .doubleValue();

        if (Math.abs(speed) >= Math.abs(maxSpeed)) {
            //accounts for negative speeds
            //speed = (speed / Math.abs(speed)) * maxSpeed;
            speed = BigDecimal.valueOf(speed)
                    .divide(BigDecimal.valueOf(speed).abs())
                    .multiply(BigDecimal.valueOf(maxSpeed))
                    .doubleValue();
            setAcceleration(0);
        }
    }

    public void travelToFloor(int floor) {

    }

    public void moveToPosition(double finalPosition) {
        double initialPosition = this.position;
        double distance = BigDecimal.valueOf(finalPosition)
                .subtract(BigDecimal.valueOf(initialPosition))
                .doubleValue();

        Instant startTime = Instant.now();
        System.out.println("Start: " + startTime);

        if (distance < 0) {
            setAcceleration(maxAcceleration * -1);
        } else {
            setAcceleration(maxAcceleration);
        }
        while (true) {
            Instant loopStart = Instant.now();
            if (Math.abs(this.position - initialPosition) >= Math.abs(distance)) {
                System.out.println("d: " + (finalPosition - initialPosition));
                System.out.println("distance: " + distance);
                setSpeed(0);
                setAcceleration(0);
                System.out.println("Arrived.");
                details();
                break;
            }

            try {
                //Instant sleepStart = Instant.now();
                Thread.sleep(100);
                //Instant sleepEnd = Instant.now();
                //Duration sleepTime = Duration.ofMillis(sleepEnd.toEpochMilli() - sleepStart.toEpochMilli());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            update(0.1);
            System.out.println("Time: " + time);
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
        time = 0;
    }
}
