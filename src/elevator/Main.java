package elevator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        Elevator e = new Elevator(0, 10);
        e.setMaxSpeed(10);
        e.setMaxAcceleration(10);
        e.details();
        while (true) {
            String input = input();
            e.moveToPosition(Double.parseDouble(input));
        }
    }

    public static String input() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        try {
            input = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }
}
