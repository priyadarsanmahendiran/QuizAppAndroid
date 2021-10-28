package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_menu_screen);
    }

    @Override
    protected void onStart(){
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            reload();
        }
    }

    public void onNewGame(View v){
        Intent i = new Intent(this, NewGame.class);
        startActivity(i);
    }

    public void onCheckScore(View v){
        Intent i = new Intent(this, CheckScore.class);
        startActivity(i);
    }

    public void reload(){

    }
}