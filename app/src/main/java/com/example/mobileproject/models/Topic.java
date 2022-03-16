package com.example.mobileproject.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Topic {
    String topicName;
    List<String> vocabs;
    // fromJson
    Topic(HashMap<String, Object> json){
        topicName = json.get("topicName").toString();
        vocabs = Arrays.asList((String[])json.get("vocabs"));
    }
    HashMap<String, Object> toJson(){
        HashMap<String, Object> json = new HashMap<String, Object>();
        json.put("topicName", topicName);
        json.put("vocab", vocabs);
        return json;
    }
}
