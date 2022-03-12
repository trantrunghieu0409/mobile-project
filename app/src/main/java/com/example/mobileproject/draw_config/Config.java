package com.example.mobileproject.draw_config;

import android.app.Activity;

public final class Config extends Activity {

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

    public static int height;
    public static int width;
    public static int offset;
}
