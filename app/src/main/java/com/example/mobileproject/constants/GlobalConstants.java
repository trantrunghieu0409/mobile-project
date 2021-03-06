package com.example.mobileproject.constants;

import com.example.mobileproject.R;

public class GlobalConstants {
    /*
        Please declare global constants here.
     */
    public static final int TIME_FOR_A_GAME = 20; // 20 seconds
    public static final int TIME_FOR_A_WAITING = 5; // 5 seconds
    public static final int MAX_HINT = 2;

    public static final Integer[] thumbnails = {R.drawable.avatar_batman,
        R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3};

    public static final String[] topics = {"Animal", "Household", "Transportation"};
    public static final String[] animals = {"cat", "dog", "mouse", "chicken", "lion", "tiger", "elephant", "monkey", "fox", "bird", "horse", "fish"};
    public static final String[] households = {"tool box", "box cutter", "duct tape",
            "hammer", "step ladder", "level", "table", "chair", "pan", "fan", "bookshelf", "cupboard", "television", "refrigerator"};
    public static final String[] transportations = {"car", "bike", "motorbike", "train", "subway", "road", "sign", "stop", "traffic light", "container",
            "plane", "truck"};

    public static final int[] nPlayers = {5, 10, 15, 20};
    public static final int[] nPoints = {20, 120, 200, 300, 400};




    // This only for testing sync drawing
    public static String currentRoomID = "example_room";
}
