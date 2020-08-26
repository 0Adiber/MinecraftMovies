package at.adiber.util;

import at.adiber.config.Config;
import at.adiber.main.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Shared {
    private static Random random = new Random();

    public static Config Config;

    public static Map<String, UUID> verifyMap = new HashMap<>();
    public static RandomID verifyIds = new RandomID(5, random);

    public static String genID() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int len = 3;

        return random.ints(leftLimit, rightLimit + 1)
                    .limit(len)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

    }

}
