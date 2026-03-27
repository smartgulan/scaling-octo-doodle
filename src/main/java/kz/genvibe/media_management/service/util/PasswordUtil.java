package kz.genvibe.media_management.service.util;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PasswordUtil {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
        "abcdefghijklmnopqrstuvwxyz" +
        "0123456789" +
        "!@#$%^&*()-_=+";

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomPassword(int length) {
        return IntStream.range(0, length)
            .map(i -> CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())))
            .mapToObj(c -> String.valueOf((char) c))
            .collect(Collectors.joining());
    }

}
