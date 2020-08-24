package at.adiber.util;

import at.adiber.main.Main;

import java.util.Random;

public class Shared {

    private static Random random = new Random();

    public static String genID() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 3;

        return random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

    }

}
