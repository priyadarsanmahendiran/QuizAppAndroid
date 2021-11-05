package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CheckScore extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_check_score);
        getContent(new firebaseCallback() {
            @Override
            public void onCallback(List<String> l) {

            }

            @Override
            public void onMCQ(List<Integer> h, List<String> a) {

            }

            @Override
            public void noTest(boolean flag) {
                if(flag==false){
                    final TextView rowNoText = new TextView(CheckScore.this);
                    rowNoText.setText("No test given until now!");
                    rowNoText.setTextSize(16);
                    rowNoText.setTextColor(Color.BLACK);
                    LinearLayout checkLayout = (LinearLayout) findViewById(R.id.checkLayout);
                    checkLayout.addView(rowNoText);
                }
            }

            @Override
            public void addButton() {

            }
        });
    }

    public void reload(){

    }

    public void getContent(firebaseCallback myCallback) {
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            reload();
        }
        LinearLayout checkLayout = findViewById(R.id.checkLayout);
        String uid = currentUser.getUid();
        CollectionReference scores = db.collection("scores");
        List<String> list = new ArrayList<>();
        scores.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    for (int i = 0; i < list.size(); i++) {
                        DocumentReference doc = scores.document(list.get(i));
                        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document.get("userId").toString().equals(currentUser.getUid())){
                                        flag=true;
                                        CardView card = new CardView(CheckScore.this);
                                        card.setPadding(10,10,10,10);
                                        card.setMaxCardElevation(10);
                                        card.setRadius(10);
                                        LinearLayout scores = new LinearLayout(CheckScore.this);
                                        scores.setOrientation(LinearLayout.VERTICAL);
                                        final TextView rowTextViewDate = new TextView(CheckScore.this);
                                        rowTextViewDate.setTextSize(20);
                                        rowTextViewDate.setTextColor(Color.BLACK);
                                        rowTextViewDate.setText("Date: " + document.get("date").toString());
                                        scores.addView(rowTextViewDate);
                                        final TextView rowTextViewSub = new TextView(CheckScore.this);
                                        rowTextViewSub.setTextSize(16);
                                        rowTextViewSub.setTextColor(Color.BLACK);
                                        rowTextViewSub.setText("Subject: " + document.get("subject").toString());
                                        scores.addView(rowTextViewSub);
                                        final TextView rowTextViewScore = new TextView(CheckScore.this);
                                        rowTextViewScore.setTextSize(16);
                                        rowTextViewScore.setTextColor(Color.BLACK);
                                        rowTextViewScore.setText("Score: " + document.get("score").toString()+"\n");
                                        scores.addView(rowTextViewScore);
                                        card.addView(scores);
                                        checkLayout.addView(card);
                                    }
                                    if(flag==false){
                                        myCallback.noTest(flag);
                                        flag=true;
                                    }

                                }
                            }
                        });
                    }
                }
            }
        });
    }
}