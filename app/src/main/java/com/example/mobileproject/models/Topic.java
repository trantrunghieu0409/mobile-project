package com.example.mobileproject.models;

import android.graphics.RenderNode;

import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class Topic {
    String topicName;
    List<String> vocabs;
    // fromJson
    Topic(HashMap<String, Object> json){
        topicName = json.get("topicName").toString();
        vocabs = Arrays.asList((String[])json.get("vocabs"));
    }

    public Topic(String topicName) {
        this.topicName = topicName;
    }

    public void addVocab(String vocab) {
        vocabs.add(vocab);
    }

    Topic(String topicName, List<String> vocabs)
    {
        this.topicName=topicName;
        this.vocabs=vocabs;
    }
    static public String[] getAllTopics() {
        return GlobalConstants.topics;
    }

    static public String generateVocab(String topicName) {
        Random rnd = new Random();
        String vocab = "";

        if (topicName.equals("Animal")) {
            vocab = GlobalConstants.animals[rnd.nextInt(GlobalConstants.animals.length)];
        }
        if (topicName.equals("Household")) {
            vocab = GlobalConstants.households[rnd.nextInt(GlobalConstants.households.length)];
        }
        if (topicName.equals("Transportation")) {
            vocab = GlobalConstants.transportations[rnd.nextInt(GlobalConstants.transportations.length)];
        }

        return vocab;
    }
}
