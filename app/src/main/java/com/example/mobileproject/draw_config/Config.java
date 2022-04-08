package com.example.mobileproject.draw_config;

import android.app.Activity;

import com.example.mobileproject.R;

public final class Config {

    public enum PenType {
        DRAW,
        ERASE,
        RED,
        ORANGE,
        YELLOW,
        GREEN,
        BLUE,
        PURPLE,
        GRAY,
        BROWN
    }

    public static Integer[] Avatars = {
            R.drawable.avatar_batman, R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3
    };
    public static Integer[] Topics = {
            R.drawable.topic1,R.drawable.topic2,R.drawable.topic3
    };

    public static int height;
    public static int width;
    public static int offset;
}
