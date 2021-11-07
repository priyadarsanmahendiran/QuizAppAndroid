package com.example.quizapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface firebaseCallback {
    void onCallback(List<String> l);
    void onMCQ(List<String>a);
    void addButton();
}
