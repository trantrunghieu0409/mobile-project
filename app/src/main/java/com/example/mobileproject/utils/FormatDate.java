package com.example.mobileproject.utils;

import com.example.mobileproject.constants.GlobalConstants;

public interface FormatDate {
    long SECONDS_IN_MINUTE = 60;
    long MINUTES_IN_HOUR = 60 * 60;
    long HOURS_IN_DAY = 24 * 60 * 60;

    static String formatDuration(long duration) {
        String result = "";
        if (duration < 0) duration = -duration;
        duration /= 1000;
        if (duration < SECONDS_IN_MINUTE) {
            result = duration + " seconds";
        }
        else if (duration < MINUTES_IN_HOUR) {
            result = duration / SECONDS_IN_MINUTE + " minutes";
        }
        else if (duration < HOURS_IN_DAY){
            result = duration / MINUTES_IN_HOUR + " hours";
        }
        else {
            result = duration / HOURS_IN_DAY + " days";
        }
        return  result;
    }
}
