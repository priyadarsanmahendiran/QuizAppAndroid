package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Subjects extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
    }

    public void onMath(View v){
        Intent i = new Intent(this, Mathematics.class);
        startActivity(i);
    }

    public void onCS(View v){
        Intent i = new Intent(this, ComputerScience.class);
        startActivity(i);
    }

    public void onPhysics(View v){
        Intent i = new Intent(this, Physics.class);
        startActivity(i);
    }

    public void onChem(View v){
        Intent i = new Intent(this, Chemistry.class);
        startActivity(i);
    }

    public void onSub(View v){
        Intent i = new Intent(this, Mathematics.class);
        startActivity(i);
    }
}