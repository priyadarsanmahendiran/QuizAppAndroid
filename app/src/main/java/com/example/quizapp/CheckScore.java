package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_check_score);
        getContent();
    }

    public void reload(){

    }

    public void getContent() {
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
                                        final TextView rowTextViewDate = new TextView(CheckScore.this);
                                        rowTextViewDate.setText("Date: " + document.get("date").toString()+"\n");
                                        checkLayout.addView(rowTextViewDate);
                                        final TextView rowTextViewSub = new TextView(CheckScore.this);
                                        rowTextViewSub.setText("Subject: " + document.get("subject").toString()+"\n");
                                        checkLayout.addView(rowTextViewSub);
                                        final TextView rowTextViewScore = new TextView(CheckScore.this);
                                        rowTextViewScore.setText("Score: " + document.get("score").toString()+"\n");
                                        checkLayout.addView(rowTextViewScore);
                                    }
                                }
                            }
                        });
                    }
                } else {

                }
            }
        });
    }
}