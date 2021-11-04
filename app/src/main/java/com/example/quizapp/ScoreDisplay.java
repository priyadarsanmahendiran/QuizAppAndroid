package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ScoreDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_display);
        int attempted = getIntent().getExtras().getInt("attempted");
        int correct = getIntent().getExtras().getInt("correct");
        int wrong = getIntent().getExtras().getInt("wrong");
        TextView qAttempt = (TextView) findViewById(R.id.qAttempt);
        String qAttText = qAttempt.getText().toString();
        System.out.println(qAttText);
        qAttempt.setText(qAttText + String.valueOf(attempted));
        TextView cAns = (TextView) findViewById(R.id.cAnswer);
        String cAnsText = cAns.getText().toString();
        cAns.setText(cAnsText + String.valueOf(correct));
        TextView wAns = (TextView) findViewById(R.id.wAnswer);
        String wAnsText = wAns.getText().toString();
        wAns.setText(wAnsText + String.valueOf(wrong));
        Intent i = new Intent(this, Subjects.class);
        startActivity(i);
    }
}