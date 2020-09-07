package com.alhafeez.assignment;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Hrusikesh swain on 9/6/2020.
 * Be U Salons
 * hrusikeshswain@beusalons.com
 */
public class RandomString {

    /**
     * Generate a random string.
     */
    public static void main(String[] args) {
        System.out.println(generateString());
    }

    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

}
