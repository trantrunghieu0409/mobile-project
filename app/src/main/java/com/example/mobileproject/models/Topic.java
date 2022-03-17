package com.example.mobileproject.models;

import com.google.gson.Gson;

import java.security.PublicKey;
import java.util.ArrayList;
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

    Topic(String topicName, List<String> vocabs)
    {
        this.topicName=topicName;
        this.vocabs=vocabs;
    }
    public String toJson(){
        Gson gson=new Gson();
        String json= gson.toJson(new Topic(topicName,vocabs));
        return json;

    }
    public Topic fromJson(String json){
        Gson gson=new Gson();
        Topic topic= gson.fromJson(json,Topic.class);
        return topic;
    }
}
