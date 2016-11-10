package elevator;

import java.math.BigDecimal;

/**
 * Creates an Elevator Object.
 *
 * TODO finish javadoc
 */
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

    public void setTime(double time) {this.time = time;}

    public double getTime() {return time;}

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
        /*
         * TODO all this stuff
         * Travelling to floor
         * ETA
         * Queue?
         * Direction?
         * Status: Travelling to... Returning to... Unused, Out of Order etc
         */

        // Print elevator details
        System.out.println("Time: " + getTime());
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

    public void moveToPosition(double finalPosition) {
        // TODO export data as csv file and make a graph of the data

        // Store current position as starting point of the elevator
        double initialPosition = this.position;

        // Calculate distance to be travelled
        double totalDistance = BigDecimal.valueOf(finalPosition)
                .subtract(BigDecimal.valueOf(initialPosition))
                .doubleValue();

        // The time it takes for the elevator to reach max speed
        // let vi = 0, thus:
        // vf = a * t
        // t = vf / a
        double timeTakenToReachMaximumSpeed = BigDecimal.valueOf(maxSpeed)
                .divide(BigDecimal.valueOf(maxAcceleration))
                .doubleValue();

        // The distance the elevator has travelled when it reaches max speed
        // let vi = 0, thus:
        // d = 0.5 * vf * t
        double distanceTravelledUponReachingMaxSpeed = BigDecimal.valueOf(maxSpeed)
                .divide(BigDecimal.valueOf(2))
                .multiply(BigDecimal.valueOf(timeTakenToReachMaximumSpeed))
                .doubleValue();

        // Reset time
        setTime(0);
        double time = 0;
        double rate = 0.1;

        double d;
        double v;
        if (totalDistance < 0) {
            maxAcceleration = Math.abs(maxAcceleration) * -1;
            maxSpeed = Math.abs(maxSpeed) * -1;
        } else {
            maxAcceleration = Math.abs(maxAcceleration);
            maxSpeed = Math.abs(maxSpeed);
        }
        double a = maxAcceleration;


        boolean doesNotReachMaxSpeed = false;
        if (Math.abs(totalDistance) < BigDecimal.valueOf(distanceTravelledUponReachingMaxSpeed)
                .multiply(BigDecimal.valueOf(2))
                .doubleValue()) {
            doesNotReachMaxSpeed = true;
        }

        // If the elevator has to begin slowing down before it reaches its max speed
        if (doesNotReachMaxSpeed) {
            double peakDistance = BigDecimal.valueOf(totalDistance)
                    .divide(BigDecimal.valueOf(2))
                    .doubleValue();
            double peakTime = Math.sqrt(BigDecimal.valueOf(peakDistance)
                    .multiply(BigDecimal.valueOf(2))
                    .divide(BigDecimal.valueOf(maxAcceleration))
                    .doubleValue());
            double peakSpeed = BigDecimal.valueOf(maxAcceleration)
                    .multiply(BigDecimal.valueOf(peakTime))
                    .doubleValue();

            // Speeding up until peak time
            setAcceleration(a);
            while (time < peakTime) {
                // d = 0.5 * acceleration * t^2
                d = BigDecimal.valueOf(0.5)
                        .multiply(BigDecimal.valueOf(acceleration))
                        .multiply(BigDecimal.valueOf(time).pow(2))
                        .doubleValue();

                // v = acceleration * time
                v = BigDecimal.valueOf(acceleration)
                        .multiply(BigDecimal.valueOf(time))
                        .doubleValue();

                setPosition(BigDecimal.valueOf(initialPosition)
                        .add(BigDecimal.valueOf(d))
                        .doubleValue());
                setSpeed(v);
                details();
                time = BigDecimal.valueOf(time)
                        .add(BigDecimal.valueOf(rate))
                        .doubleValue();
                setTime(time);
            }

            // Display details at peak
            System.out.println("Peak:");
            setTime(peakTime);
            setPosition(BigDecimal.valueOf(initialPosition)
                    .add(BigDecimal.valueOf(peakDistance))
                    .doubleValue());
            setSpeed(peakSpeed);
            setAcceleration(0);
            details();

            // reset time
            setTime(time);

            // Slowing down after peak time
            setAcceleration(-a);
            while (time > peakTime) {
                // d = peakSpeed * (time - peakTime) + 0.5 * acceleration * Math.pow((time - peakTime), 2) + peakDistance;
                d = BigDecimal.valueOf(peakSpeed)
                        .multiply(BigDecimal.valueOf(time).subtract(BigDecimal.valueOf(peakTime)))
                        .add(BigDecimal.valueOf(0.5)
                            .multiply(BigDecimal.valueOf(acceleration))
                            .multiply((BigDecimal.valueOf(time).subtract(BigDecimal.valueOf(peakTime))).pow(2)))
                        .add(BigDecimal.valueOf(peakDistance))
                        .doubleValue();

                // v = peakSpeed + acceleration * (time - peakTime);
                v = BigDecimal.valueOf(peakSpeed)
                        .add(BigDecimal.valueOf(acceleration)
                            .multiply(BigDecimal.valueOf(time).subtract(BigDecimal.valueOf(peakTime))))
                        .doubleValue();

                setPosition(initialPosition + d);
                setSpeed(v);
                details();
                time = BigDecimal.valueOf(time)
                        .add(BigDecimal.valueOf(rate))
                        .doubleValue();
                setTime(time);

                // Elevator has arrived
                if (time >= BigDecimal.valueOf(peakTime).multiply(BigDecimal.valueOf(2)).doubleValue()) {
                    System.out.println("Arrived.");
                    setTime(BigDecimal.valueOf(peakTime)
                            .multiply(BigDecimal.valueOf(2))
                            .doubleValue());
                    setPosition(finalPosition);
                    setSpeed(0);
                    setAcceleration(0);
                    details();
                    break;
                }
            }
        } else {
            double timeToReachMaxSpeed = BigDecimal.valueOf(maxSpeed)
                    .divide(BigDecimal.valueOf(a))
                    .doubleValue();
            double distanceAtMaxSpeed = BigDecimal.valueOf(0.5)
                    .multiply(BigDecimal.valueOf(maxSpeed))
                    .multiply(BigDecimal.valueOf(timeToReachMaxSpeed))
                    .doubleValue();

            // Accelerate to maxSpeed
            setAcceleration(a);
            while (time < timeToReachMaxSpeed) {
                // d = 0.5 * acceleration * Math.pow(time, 2);
                d = BigDecimal.valueOf(0.5)
                        .multiply(BigDecimal.valueOf(acceleration))
                        .multiply(BigDecimal.valueOf(time).pow(2))
                        .doubleValue();

                // v = acceleration * time;
                v = BigDecimal.valueOf(acceleration)
                        .multiply(BigDecimal.valueOf(time))
                        .doubleValue();

                setPosition(BigDecimal.valueOf(initialPosition)
                        .add(BigDecimal.valueOf(d))
                        .doubleValue());
                setSpeed(v);
                details();
                time = BigDecimal.valueOf(time)
                        .add(BigDecimal.valueOf(rate))
                        .doubleValue();
                setTime(time);
            }

            setTime(timeToReachMaxSpeed);
            setPosition(BigDecimal.valueOf(initialPosition)
                    .add(BigDecimal.valueOf(distanceAtMaxSpeed))
                    .doubleValue());
            setSpeed(maxSpeed);
            setAcceleration(0);
            System.out.println("Peak:");
            details();

            double totalCruiseDistance = BigDecimal.valueOf(totalDistance)
                    .subtract(BigDecimal.valueOf(distanceAtMaxSpeed)
                        .multiply(BigDecimal.valueOf(2)))
                    .doubleValue();
            double cruiseStartPosition = BigDecimal.valueOf(initialPosition)
                    .add(BigDecimal.valueOf(distanceAtMaxSpeed))
                    .doubleValue();
            double cruiseEndPosition = BigDecimal.valueOf(cruiseStartPosition)
                    .add(BigDecimal.valueOf(totalCruiseDistance))
                    .doubleValue();
            double totalCruiseTime = BigDecimal.valueOf(totalCruiseDistance)
                    .divide(BigDecimal.valueOf(maxSpeed))
                    .doubleValue();
            double cruiseStartTime = timeToReachMaxSpeed;
            double cruiseEndTime = BigDecimal.valueOf(cruiseStartTime)
                    .add(BigDecimal.valueOf(totalCruiseDistance)
                        .divide(BigDecimal.valueOf(maxSpeed)))
                    .doubleValue();

            // reset time
            if (time == getTime()) {
                time = BigDecimal.valueOf(time)
                        .add(BigDecimal.valueOf(rate))
                        .doubleValue();
            }
            setTime(time);

            // Cruise at max speed
            while (time < cruiseEndTime) {
                // d = maxSpeed * (time - cruiseStartTime)
                d = BigDecimal.valueOf(maxSpeed)
                        .multiply(BigDecimal.valueOf(time)
                            .subtract(BigDecimal.valueOf(cruiseStartTime)))
                        .doubleValue();

                setPosition(BigDecimal.valueOf(cruiseStartPosition)
                        .add(BigDecimal.valueOf(d))
                        .doubleValue());
                details();
                time = BigDecimal.valueOf(time)
                        .add(BigDecimal.valueOf(rate))
                        .doubleValue();
                setTime(time);
            }

            setTime(cruiseEndTime);
            setPosition(cruiseEndPosition);
            setSpeed(maxSpeed);
            setAcceleration(0);
            if (cruiseEndTime != cruiseStartTime) {
                // does not print if no time (and therefore distance) has passed since the start of the cruising time
                System.out.println("Peak:");
                details();
            }

            // reset time
            if (time == getTime()) {
                time = BigDecimal.valueOf(time)
                        .add(BigDecimal.valueOf(rate))
                        .doubleValue();
            }
            setTime(time);

            // Slowing down
            setAcceleration(-a);
            while (time >= cruiseEndTime) {
                // d = maxSpeed * (time - cruiseEndTime) + 0.5 * acceleration * Math.pow((time - cruiseEndTime), 2);
                d = BigDecimal.valueOf(maxSpeed)
                        .multiply(BigDecimal.valueOf(time)
                            .subtract(BigDecimal.valueOf(cruiseEndTime)))
                        .add(BigDecimal.valueOf(0.5)
                            .multiply(BigDecimal.valueOf(acceleration))
                            .multiply((BigDecimal.valueOf(time).subtract(BigDecimal.valueOf(cruiseEndTime))).pow(2)))
                        .doubleValue();

                // v = maxSpeed + acceleration * (time - cruiseEndTime);
                v = BigDecimal.valueOf(maxSpeed)
                        .add(BigDecimal.valueOf(acceleration)
                            .multiply(BigDecimal.valueOf(time).subtract(BigDecimal.valueOf(cruiseEndTime))))
                        .doubleValue();

                setPosition(BigDecimal.valueOf(cruiseEndPosition)
                        .add(BigDecimal.valueOf(d))
                        .doubleValue());
                setSpeed(v);
                details();
                time = BigDecimal.valueOf(time)
                        .add(BigDecimal.valueOf(rate))
                        .doubleValue();
                setTime(time);

                // Elevator has arrived
                if (time >= BigDecimal.valueOf(2)
                        .multiply(BigDecimal.valueOf(timeToReachMaxSpeed))
                        .add(BigDecimal.valueOf(totalCruiseTime))
                        .doubleValue()) {
                    System.out.println("Arrived.");
                    setTime(BigDecimal.valueOf(2)
                            .multiply(BigDecimal.valueOf(timeToReachMaxSpeed))
                            .add(BigDecimal.valueOf(totalCruiseTime))
                            .doubleValue());
                    setPosition(finalPosition);
                    setSpeed(0);
                    setAcceleration(0);
                    details();
                    break;
                }
            }
        }
    }

    public void travelToFloor(int floor) {

    }
}
