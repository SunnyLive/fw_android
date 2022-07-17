package com.fengwo.module_chat.utils.chat_new;

import java.util.Random;

public class FingerprintUtils {

    private static final char[] RANDOM_CHAR = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    public static String getRandomFingerprint(String userId, String toId) {
        StringBuilder append = new StringBuilder().append(userId).append('_').append(toId).append('_');
        for (int i = 0; i < 10; i++) {
            int index = new Random().nextInt(RANDOM_CHAR.length);
            append.append(RANDOM_CHAR[index]);
        }
        return append.toString();
    }
}