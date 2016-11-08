package elevator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.Temporal;

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
        //TODO export data as csv file and make a graph of the data
        // TODO fix time and else loop section
        // TODO use BigDecimal

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
        double timeTakenToReachMaximumSpeed = maxSpeed / maxAcceleration;

        // The distance the elevator has travelled when it reaches max speed
        // let vi = 0, thus:
        // d = 0.5 * vf * t
        double distanceTravelledUponReachingMaxSpeed = 0.5 * maxSpeed * timeTakenToReachMaximumSpeed;

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

        System.out.println("debug:");
        System.out.println(Math.abs(totalDistance));
        System.out.println(distanceTravelledUponReachingMaxSpeed * 2);

        // If the elevator has to begin slowing down before it reaches its max speed
        if (Math.abs(totalDistance) < distanceTravelledUponReachingMaxSpeed * 2) {
            double peakDistance = totalDistance / 2;
            double peakTime = Math.sqrt(2 * peakDistance / maxAcceleration);
            double peakSpeed = maxAcceleration * peakTime;

            // Speeding up until peak time
            setAcceleration(a);
            while (time < peakTime) {
                d = 0.5 * acceleration * Math.pow(time, 2);
                v = acceleration * time;
                setPosition(initialPosition + d);
                setSpeed(v);
                details();
                time += rate;
                setTime(time);
            }

            // Display details at peak
            System.out.println("Peak:");
            setTime(peakTime);
            setPosition(initialPosition + peakDistance);
            setSpeed(peakSpeed);
            setAcceleration(0);
            details();

            // Slowing down after peak time
            setAcceleration(-a);
            while (time > peakTime) {
                d = peakSpeed * (time - peakTime) + 0.5 * acceleration * Math.pow((time - peakTime), 2) + peakDistance;
                v = peakSpeed + acceleration * (time - peakTime);
                setPosition(initialPosition + d);
                setSpeed(v);
                details();
                time += rate;
                setTime(time);

                // Elevator has arrived
                if (time >= peakTime * 2) {
                    System.out.println("Arrived.");
                    setTime(peakTime * 2);
                    setPosition(finalPosition);
                    setSpeed(0);
                    setAcceleration(0);
                    details();
                    break;
                }
            }
        } else {
            System.out.println("loop");
            System.out.println("max v: " + maxSpeed);
            System.out.println("max a: " + maxAcceleration);
            System.out.println("total distance: " + totalDistance);
            System.out.println("max speed distance: " + distanceTravelledUponReachingMaxSpeed * 2);

            double timeToReachMaxSpeed = maxSpeed / a;
            double distanceAtMaxSpeed = 0.5 * maxSpeed * timeToReachMaxSpeed;

            // Accelerate to maxSpeed
            setAcceleration(a);
            while (time < timeToReachMaxSpeed) {
                d = 0.5 * acceleration * Math.pow(time, 2);
                v = acceleration * time;
                setPosition(initialPosition + d);
                setSpeed(v);
                details();
                time += rate;
                setTime(time);
            }

            System.out.println("Peak:");
            setTime(timeToReachMaxSpeed);
            setPosition(initialPosition + distanceAtMaxSpeed);
            setSpeed(maxSpeed);
            setAcceleration(0);
            details();

            double totalCruiseDistance = totalDistance - 2 * distanceAtMaxSpeed;
            double cruiseStartPosition = initialPosition + distanceAtMaxSpeed;
            double cruiseEndPosition = cruiseStartPosition + totalCruiseDistance;
            double totalCruiseTime = totalCruiseDistance / maxSpeed;
            double cruiseStartTime = timeToReachMaxSpeed;
            double cruiseEndTime = cruiseStartTime + totalCruiseDistance / maxSpeed;

            // Cruise at max speed
            setTime(time);
            while (time < cruiseEndTime) {
                d = maxSpeed * (time - cruiseStartTime);
                setPosition(cruiseStartPosition + d);
                details();
                time += rate;
                setTime(time);
            }

            System.out.println("Peak:");
            setTime(cruiseEndTime);
            setPosition(cruiseEndPosition);
            setSpeed(maxSpeed);
            setAcceleration(0);
            details();

            // Slowing down
            setAcceleration(-a);
            while (time > cruiseEndTime) {
                d = maxSpeed * (time - cruiseEndTime) + 0.5 * acceleration * Math.pow((time - cruiseEndTime), 2);
                v = maxSpeed + acceleration * (time - cruiseEndTime);
                setPosition(cruiseEndPosition + d);
                setSpeed(v);
                details();
                time += rate;
                setTime(time);

                // Elevator has arrived
                if (time >= 2 * timeToReachMaxSpeed + totalCruiseTime) {
                    System.out.println("Arrived.");
                    setTime(2 * timeToReachMaxSpeed + totalCruiseTime);
                    setPosition(finalPosition);
                    setSpeed(0);
                    setAcceleration(0);
                    details();
                    break;
                }
            }
        }

        // Reset maximum speeds and acceleration
        setMaxSpeed(Math.abs(maxSpeed));
        setMaxAcceleration(Math.abs(maxAcceleration));
    }

    public void travelToFloor(int floor) {

    }
}
