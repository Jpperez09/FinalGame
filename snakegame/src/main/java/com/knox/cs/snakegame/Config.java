package com.knox.cs.snakegame;

public class Config {
    private static String speed = "Normal"; // Default speed

    public static String getSpeed() {
        return speed;
    }

    public static void setSpeed(String speed) {
        Config.speed = speed;
    }
}
