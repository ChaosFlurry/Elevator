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

    public void update(double rate) {
        /*
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
        */


    }

    public void moveToPosition(double finalPosition) {
        /*
        TODO export data as csv file and make a graph of the data

        5 m:
        ti = 0
        tf = sqrt(1/2)
        vi = 0
        vf = 7.07106781187   5sqrt(2)
        a = -10
        t = 0.7071067811    sqrt(2)/2
        s = 2.5

        2.5 = 0 * t + 0.5 * 10 * t^2
        2.5 = 5t^2
        0.5 = t^2
        t = sqrt(2)/2
        t = 0.707...

        d = vi * t + 0.5 at^2
        t = 0.8 - sqrt(1/2)
        vi = 5sqrt(2)

        d at t 0.7 = 0 + 5*0.7^2 = 2.4
        d at t sqrt(1/2) = 2.5
        d at 0.8 = 2.5 + (5sqrt(2)*(0.8 - sqrt(0.5))) + (0.5 * -10 * (0.8 - sqrt(0.5))^2)
                 = 2.5 + 0.6137
                 = 3.1137

        calculate every time interval before peak
            check if interval is before or after peak
            if after then calculate peak position/velocity +- new position/velocity
        calculate every time interval after peak (position/velocity at peak + new position/velocity after peak)
        display details

        Stages:
        (assuming d = 100)
        initial acceleration
        t0 = 0
        tf = 1      t = (vf - vi)/a (where a <> 0)  OR  t = sqrt(2d/a) (where vi = 0)  OR  t = 2d/(vi + vf)
        d0 = 0
        df = 5      d = vi*t + 0.5*a*t^2  OR  d = (vi + vf)/2*t
        v0 = 0
        vf = 10     vf = vi + at
        a = 10
        cruising speed
        t0 = 1
        tf = 10
        d0 = 5
        df = 95     (total distance - distance to slow down to 0v)
        v0 = 10
        vf = 10
        a = 0
        final deceleration
        t0 = 10
        tf = 11
        d0 = 95
        df = 100
        vi = 10
        vf = 0
        a = -10

        speed up to 10 ms (set acceleration to 10)
        travel at 10 ms until x - 5 (set acceleration to 0 at 5m)
        slow down to 0 ms (set acceleration -10 at x - 5m)

        calculate position/velocity at time vs sum of positions/velocities
        check which stage interval is at
            if before first turn:
                calculate position/velocity at time
            if after first turn:
                calculate position/velocity at peak + position/velocity after peak with new time interval
            if after 2nd turn:
                calculate position at first peak + position velocity of cruise + position/velocity after 2nd peak
                with new time interval

        d = 5
        acceleration to peak
        ti = 0
        tf = sqrt(0.5)
        si = 0
        sf = 2.5
        vi = 0
        vf = 5sqrt(2)
        a = 10

        d = vi*t + 0.5*a*t^2
        2.5 = 5t^2
        0.5 = t^2
        t = sqrt(0.5)

        vf = vi + at
        vf = 10sqrt(2)

        deceleration from peak
        ti = sqrt(0.5)
        tf = sqrt(2)
        si = 2.5
        sf = 5
        vi = 5sqrt(2)
        vf = 0
        a = -10

        calculate peak time, position, speed

        simulate time events on a scale of something like 0.0000001
        but only display every 0.1 seconds of data
         */

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

        // Indicates if the elevator has already started decelerating as it approaches its destination
        boolean slowingDown = false;

        // Indicates if elevator needs to slow down before reaching its max speed
        // (for distances whereby the elevator would not be able to reach its max speed within that time frame)
        boolean earlyDeceleration = false;

        // Checks if the elevator has to begin slowing down before it reaches its max speed
        if (Math.abs(totalDistance) < distanceTravelledUponReachingMaxSpeed * 2) {
            earlyDeceleration = true;
        }
        /*
        speed up to 10 ms (set acceleration to 10)
        travel at 10 ms until x - 5 (set acceleration to 0 at 5m)
        slow down to 0 ms (set acceleration -10 at x - 5m)

        calculate position/velocity at time vs sum of positions/velocities
        check which stage interval is at
        if before first turn:
        calculate position/velocity at time
        if after first turn:
        calculate position/velocity at peak + position/velocity after peak with new time interval
        if after 2nd turn:
        calculate position at first peak + position velocity of cruise + position/velocity after 2nd peak
        with new time interval
        */

        // TODO fix time and else loop section

        // Reset time
        setTime(0);
        double time = 0;
        double rate = 0.001;

        double d;
        double v;
        if (totalDistance < 0) {
            maxAcceleration = Math.abs(maxAcceleration) * -1;
        } else {
            maxAcceleration = Math.abs(maxAcceleration);
        }
        double a = maxAcceleration;

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
                //time = (Instant.now().toEpochMilli() - startTime.toEpochMilli()) / 1000;
                setTime(time);
            }

            // Display details at peak
            System.out.println("Peak:");
            setTime(peakTime);
            setPosition(peakDistance);
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

                // if the elevator has arrived
                if (time >= peakTime * 2) {
                    System.out.println("Arrived.");
                    setTime(peakTime * 2);
                    setPosition(finalPosition);
                    setSpeed(0);
                    setAcceleration(0);
                    details();
                    setMaxSpeed(Math.abs(maxSpeed));
                    setMaxAcceleration(Math.abs(maxAcceleration));
                    break;
                }
            }
        } else {
            System.out.println("loop");
            System.out.println("max v: " + maxSpeed);
            System.out.println("max a: " + maxAcceleration);
            System.out.println("total distance: " + totalDistance);
            System.out.println("max speed distance: " + distanceTravelledUponReachingMaxSpeed * 2);
            details();
        }

        /*
        while (true) {
            if (earlyDeceleration) {
                double peakTime = Math.sqrt((finalPosition/maxAcceleration));   // t = sqrt(0.5*2d/a)
                //System.out.println("peak time: " + peakTime);
                double peakDisplacement = 0.5 * maxAcceleration * Math.pow(peakTime, 2);  // d = vi*t + 0.5at^2 (vi is 0 in this case)
                double peakVelocity = maxAcceleration * peakTime;     // vf = vi + at
                if (time < peakTime) {
                    d = 0.5 * acceleration * Math.pow(time, 2);     //d = vi*t + 0.5at^2 (vi is 0 in this case)
                    v = a*time;        // vi + at
                    setPosition(position + d);
                    setSpeed(v);
                } else if (time >= peakTime) {
                    a = maxAcceleration * -1;
                    double timeInterval = time - peakTime;
                    d = peakDisplacement + peakVelocity * timeInterval + 0.5 * a * Math.pow(timeInterval, 2);
                    v = peakVelocity + a * timeInterval;
                    setPosition(position + d);
                    setSpeed(v);
                    setAcceleration(a);
                    if (v <= 0) {
                        System.out.println("Arrived");
                        double endTime = peakTime * 2;
                        double endDisplacement = peakDisplacement * 2;
                        double endVelocity = 0;
                        double endAcceleration = 0;
                        setTime(endTime);
                        setPosition(endDisplacement);
                        setSpeed(0);
                        setAcceleration(0);
                        details();
                        break;
                    }
                }
            } else {
                System.out.println("Not early a");
                break;
            }
            details();
            time += rate;   // increment time
            setTime(time);
            */


            /*
            // travelledDistance = Math.abs(this.position - initialPosition)
            // remainingDistance = Math.abs(totalDistance) - travelledDistance
            travelledDistance = BigDecimal.valueOf(this.position)
                    .subtract(BigDecimal.valueOf(initialPosition))
                    .abs()
                    .doubleValue();
            remainingDistance = BigDecimal.valueOf(totalDistance).abs()
                    .subtract(BigDecimal.valueOf(travelledDistance))
                    .doubleValue();

            if (remainingDistance <= 0) {
                // Checks if the elevator has completed travelling the whole distance (accounts for negative positions)

                // Stops the elevator
                //setSpeed(0);
                //setAcceleration(0);
                System.out.println("Arrived.");
                details();
                //break;

                // debug purposes (preventing elevator from suddenly stopping at the end to gather more velocity data)
                if (speed < 0) {
                    break;
                }
            } else if (earlyDeceleration) {
                // Checks if the elevator has to slow down despite not having reached max speed
                // If the elevator needs to slow down before it reaches it max speed,
                // set the elevator to start to slow down at the half way point

                // Checks if the elevator has reached the half way point
                if (travelledDistance >= Math.abs(totalDistance) / 2 && !slowingDown) {
                    // Slow elevator down by setting speed in the opposite direction of motion
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
                    // Slow elevator down by setting speed in the opposite direction of motion
                    if (speed > 0) {
                        setAcceleration(maxAcceleration * -1);
                    } else {
                        setAcceleration(maxAcceleration);
                    }
                    System.out.println("Slowing down...\n");
                    slowingDown = true;
                }
            } */
        // Recalculates elevator details every time interval (s)
        //update(0.01);
        // Display new elevator details
        //details();

        // Used to calculate loop times (logging purposes only)
        //Instant endTime = Instant.now();
        //System.out.println("End: " + endTime);
        //Duration elapsed = Duration.ofMillis(endTime.toEpochMilli() - startTime.toEpochMilli());
        //System.out.println("Elapsed time: " + elapsed);
    }

    public void travelToFloor(int floor) {

    }
}
