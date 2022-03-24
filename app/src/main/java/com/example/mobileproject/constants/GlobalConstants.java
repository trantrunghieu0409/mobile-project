package com.example.mobileproject.constants;

import com.example.mobileproject.R;

public class GlobalConstants {
    /*
        Please declare global constants here.
        Global constants should be considered:
        time for a game, number of players in game, score system, .etc
     */
    public static final int TIME_FOR_A_GAME = 20; // 20 seconds
    public static final Integer[] thumbnails = {R.drawable.avatar_batman,
        R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3};

    public static final String[] topics = {"Animal", "Household", "Transportation"};
    public static final String[] animals = {"cat", "dog", "mouse", "chicken", "lion"};
    public static final String[] households = {"tool box", "box cutter", "duct tape",
            "hammer", "step ladder", "level"};
    public static final String[] transportations = {"car", "bike", "motorbike", "train",
            "plane", "truck"};
    public static final int[] nPlayers = {5, 10, 15, 20};
    public static final int[] nPoints = {120, 200, 300, 400};

    // This only for testing sync drawing
    public static String currentRoomID = "example_room";
}
