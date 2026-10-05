package com.sunoza.utils;

public class YouTubeUtils {

    private YouTubeUtils() {
    }

    public static String convertDuration(String duration) {

        if (duration == null || duration.isBlank()) {
            return "00:00";
        }

        try {
            String value = duration.replace("PT", "");

            int hours = 0;
            int minutes = 0;
            int seconds = 0;

            if (value.contains("H")) {
                String[] hourParts = value.split("H");
                hours = Integer.parseInt(hourParts[0]);
                value = hourParts.length > 1 ? hourParts[1] : "";
            }

            if (value.contains("M")) {
                String[] minuteParts = value.split("M");
                minutes = Integer.parseInt(minuteParts[0]);
                value = minuteParts.length > 1 ? minuteParts[1] : "";
            }

            if (value.contains("S")) {
                seconds = Integer.parseInt(
                        value.replace("S", "")
                );
            }

            if (hours > 0) {
                return String.format(
                        "%02d:%02d:%02d",
                        hours,
                        minutes,
                        seconds
                );
            }

            return String.format(
                    "%02d:%02d",
                    minutes,
                    seconds
            );

        } catch (Exception e) {
            return "00:00";
        }
    }
}