package elevator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

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

    public void update(double rate) {
        // Recalculate time
        time = BigDecimal.valueOf(time)
                .add(BigDecimal.valueOf(rate))
                .doubleValue();

        // Recalculate position
        // position += speed * rate + 0.5 * acceleration * Math.pow(rate, 2);
        position = BigDecimal.valueOf(position)
                .add(BigDecimal.valueOf(speed)
                    .multiply(BigDecimal.valueOf(rate))
                .add(BigDecimal.valueOf(0.5)
                    .multiply(BigDecimal.valueOf(acceleration))
                    .multiply(BigDecimal.valueOf(rate).pow(2))))
                .doubleValue();

        // Recalculate speed
        // speed += acceleration * rate;
        speed = BigDecimal.valueOf(speed)
                .add(BigDecimal.valueOf(acceleration)
                    .multiply(BigDecimal.valueOf(rate)))
                .doubleValue();

        // Set speed to maximum allowed speed if speed exceeds maximum allowed
        // Account for negative speeds
        if (Math.abs(speed) >= Math.abs(maxSpeed)) {
            // speed = (speed / Math.abs(speed)) * maxSpeed;
            speed = BigDecimal.valueOf(speed)
                    .divide(BigDecimal.valueOf(speed).abs())
                    .multiply(BigDecimal.valueOf(maxSpeed))
                    .doubleValue();
            setAcceleration(0);
        }
    }

    public void moveToPosition(double finalPosition) {
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

        // Used to calculate loop times (logging purposes only)
        Instant startTime = Instant.now();
        System.out.println("Start: " + startTime);

        // Account for negative distances
        if (totalDistance < 0) {
            setAcceleration(maxAcceleration * -1);
        } else {
            setAcceleration(maxAcceleration);
        }

        // Indicates if the elevator has already started decelerating as it approaches its destination
        boolean slowingDown = false;

        // Indicates if elevator needs to slow down before reaching its max speed
        // (for distances whereby the elevator would not be able to reach its max speed within that time frame)
        boolean earlyDeceleration = false;

        // Checks if the elevator has to begin slowing down before it reaches its max speed
        if (Math.abs(totalDistance) < distanceTravelledUponReachingMaxSpeed * 2) {
            earlyDeceleration = true;
        }

        System.out.println("time to reach max: " + timeTakenToReachMaximumSpeed);
        System.out.println("total d: " + totalDistance);
        System.out.println("distance travelled max speed: " + distanceTravelledUponReachingMaxSpeed);
        System.out.println("early acc: " + earlyDeceleration);

        // Continually recalculate and display elevator details (time, position, speed, acceleration)
        double travelledDistance;
        double remainingDistance;
        while (true) {
            travelledDistance = Math.abs(this.position - initialPosition);
            remainingDistance = Math.abs(totalDistance) - travelledDistance;

            if (remainingDistance <= 0) {
                // Checks if the elevator has completed travelling the whole distance (accounts for negative positions)

                // Stops the elevator
                setSpeed(0);
                setAcceleration(0);
                System.out.println("Arrived.");
                details();
                break;
            } else if (earlyDeceleration) {
                System.out.println("Early deceleration");
                // Checks if the elevator has to slow down despite not having reached max speed
                // If the elevator needs to slow down before it reaches it max speed,
                // set the elevator to start to slow down at the half way point

                // Checks if the elevator has reached the half way point
                if (travelledDistance >= Math.abs(totalDistance) / 2 && !slowingDown) {
                    if (speed > 0) {
                        setAcceleration(maxAcceleration * -1);
                    } else {
                        setAcceleration(maxAcceleration);
                    }
                    System.out.println("Slowing down...\n");
                    slowingDown = true;
                }
            }
            else if (Math.abs(remainingDistance) <= distanceTravelledUponReachingMaxSpeed) {
                // Checks if the elevator has reached the point where it has to
                // begin to slow down to smoothly reach the destination.

                // Checks if the elevator is already slowing down
                if (!slowingDown) {
                    if (speed > 0) {
                        setAcceleration(maxAcceleration * -1);
                    } else {
                        setAcceleration(maxAcceleration);
                    }
                    System.out.println("Slowing down...\n");
                    slowingDown = true;
                }
            }
            // Recalculates elevator details every time interval (s)
            update(0.1);
            // Display new elevator details
            details();
        }

        // Used to calculate loop times (logging purposes only)
        Instant endTime = Instant.now();
        System.out.println("End: " + endTime);
        Duration elapsed = Duration.ofMillis(endTime.toEpochMilli() - startTime.toEpochMilli());
        System.out.println("Elapsed time: " + elapsed);
    }

    public void travelToFloor(int floor) {

    }
}
