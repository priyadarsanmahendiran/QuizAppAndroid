package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Chemistry extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    List<String > answers = new ArrayList<>();
    List<Integer> answer_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_chemistry);
        getQuestions(new MyCallback() {
            @Override
            public void onCallback(List<String>l) {
                answers= l;
            }
        });
    }

    public void getQuestions(MyCallback myCallback){
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            reload();
        }
        LinearLayout compLayout = findViewById(R.id.chemlayout);
        CollectionReference chemistry = db.collection("chemistry");
        List<String> answers = new ArrayList<>();
        chemistry.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference doc = chemistry.document(document.getId());
                        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    String mcq = document.get("mcq").toString();
                                    final TextView rowTextViewQuestion = new TextView(Chemistry.this);
                                    rowTextViewQuestion.setText("Question: " + document.get("question").toString());
                                    compLayout.addView(rowTextViewQuestion);
                                    answers.add(document.get("answer").toString());
                                    if (mcq.equals("false")) {
                                        final EditText rowAnswer = new EditText(Chemistry.this);
                                        int id = View.generateViewId();
                                        rowAnswer.setId(id);
                                        answer_id.add(id);
                                        compLayout.addView(rowAnswer);
                                    }
                                    myCallback.onCallback(answers);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void reload(){

    }

    public void getScore(View v){
        int score=0;
        for(int i=0;i< answer_id.size();i++){
            int id = answer_id.get(i);
            EditText ans = (EditText) findViewById(id);
            String ans_val = ans.getText().toString();
            String noSpaceAns = ans_val.replaceAll("\\s", "");
            noSpaceAns= noSpaceAns.toLowerCase(Locale.ROOT);
            String correctAns = answers.get(i);
            correctAns = correctAns.replaceAll("\\s","");
            correctAns = correctAns.toLowerCase(Locale.ROOT);
            if(noSpaceAns.equals(correctAns)){
                score++;
            }
        }
        Toast.makeText(this, "SCORE: " + String.valueOf(score), Toast.LENGTH_LONG).show();
        String uid = currentUser.getUid();
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Object> score_new = new HashMap<>();
        score_new.put("date",ft.format(d));
        score_new.put("score",score);
        score_new.put("subject","chemistry");
        score_new.put("userId",uid);
        db.collection("scores").document().set(score_new);
    }
}