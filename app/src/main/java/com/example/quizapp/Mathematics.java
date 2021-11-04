package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Mathematics extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    List<String > answers = new ArrayList<>();
    List<String> mcq_answers = new ArrayList<>();
    List<Integer> answer_id = new ArrayList<>();
    List<Integer> radioGroupId = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_mathematics);
        getQuestions(new firebaseCallback() {
            @Override
            public void onCallback(List<String>l) {
                answers= l;
            }
            @Override
            public void onMCQ(List<Integer>h, List<String>a){
                radioGroupId = h; mcq_answers=a;
            }
            @Override
            public void noTest(boolean f){

            }
            @Override
            public void addButton(){
                Button submit = new Button(Mathematics.this);
                submit.setText("Submit");
                submit.setBackgroundColor(Color.BLUE);
                submit.setTextColor(Color.WHITE);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getScore(view);
                    }
                });
                LinearLayout compLayout = findViewById(R.id.mathlayout);
                compLayout.addView(submit);
            }
        });
    }

    public void getQuestions(firebaseCallback myCallback){
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            reload();
        }
        LinearLayout compLayout = findViewById(R.id.mathlayout);
        CollectionReference mathematics = db.collection("mathematics");
        List<String> answers = new ArrayList<>();
        List<String> mcq_answers = new ArrayList<>();
        List<Integer>rgId = new ArrayList<>();
        mathematics.limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference doc = mathematics.document(document.getId());
                        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    String mcq = document.get("mcq").toString();
                                    if (mcq.equals("false")) {
                                        final TextView rowTextViewQuestion = new TextView(Mathematics.this);
                                        rowTextViewQuestion.setText(document.get("question").toString());
                                        rowTextViewQuestion.setTextSize(16);
                                        rowTextViewQuestion.setTextColor(Color.BLACK);
                                        rowTextViewQuestion.setTypeface(null, Typeface.BOLD);
                                        compLayout.addView(rowTextViewQuestion);
                                        answers.add(document.get("answer").toString());
                                        final EditText rowAnswer = new EditText(Mathematics.this);
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
        mathematics.limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    DocumentReference doc = mathematics.document(document.getId());
                    doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                String mcq = document.get("mcq").toString();
                                if (mcq.equals("true")) {
                                    final TextView rowTextViewQuestion = new TextView(Mathematics.this);
                                    rowTextViewQuestion.setText(document.get("question").toString());
                                    rowTextViewQuestion.setTextSize(16);
                                    rowTextViewQuestion.setTextColor(Color.BLACK);
                                    rowTextViewQuestion.setTypeface(null, Typeface.BOLD);
                                    compLayout.addView(rowTextViewQuestion);
                                    mcq_answers.add(document.get("answer").toString());
                                    final RadioGroup answerGrp = new RadioGroup(Mathematics.this);
                                    int rg_id = View.generateViewId();
                                    answerGrp.setId(rg_id);
                                    final RadioButton opt1 = new RadioButton(Mathematics.this);
                                    final RadioButton opt2 = new RadioButton(Mathematics.this);
                                    final RadioButton opt3 = new RadioButton(Mathematics.this);
                                    final RadioButton opt4 = new RadioButton(Mathematics.this);
                                    int op1id = View.generateViewId();
                                    int op2id = View.generateViewId();
                                    int op3id = View.generateViewId();
                                    int op4id = View.generateViewId();
                                    opt1.setId(op1id);
                                    opt2.setId(op2id);
                                    opt3.setId(op3id);
                                    opt4.setId(op4id);
                                    opt1.setText(document.get("option1").toString());
                                    opt2.setText(document.get("option2").toString());
                                    opt3.setText(document.get("option3").toString());
                                    opt4.setText(document.get("option4").toString());
                                    answerGrp.addView(opt1);
                                    answerGrp.addView(opt2);
                                    answerGrp.addView(opt3);
                                    answerGrp.addView(opt4);
                                    rgId.add(rg_id);
                                    compLayout.addView(answerGrp);
                                }
                                myCallback.onMCQ(rgId,mcq_answers);
                                if(mcq_answers.size()+answers.size()==queryDocumentSnapshots.size())
                                    myCallback.addButton();
                            }
                        }
                    });
                }
            }

        });
    }

    public void reload(){

    }

    public void getScore(View v){
        int score=0;
        int attempted=0;
        for(int i=0;i< answer_id.size();i++){
            int id = answer_id.get(i);
            EditText ans = (EditText) findViewById(id);
            String ans_val = ans.getText().toString();
            System.out.println(ans_val=="");
            if(ans_val.length()>0)
                attempted++;
            String noSpaceAns = ans_val.replaceAll("\\s", "");
            noSpaceAns= noSpaceAns.toLowerCase(Locale.ROOT);
            String correctAns = answers.get(i);
            correctAns = correctAns.replaceAll("\\s","");
            correctAns = correctAns.toLowerCase(Locale.ROOT);
            if(noSpaceAns.equals(correctAns)){
                score++;
            }
        }
        for(int i=0;i<radioGroupId.size();i++){
            RadioGroup rg = (RadioGroup) findViewById(radioGroupId.get(i));
            int selectedId = rg.getCheckedRadioButtonId();
            RadioButton selectedAns = (RadioButton)findViewById(selectedId);
            if(selectedAns!=null) {
                attempted++;
                String ans_val = selectedAns.getText().toString();
                String noSpaceAns = ans_val.replaceAll("\\s", "");
                noSpaceAns = noSpaceAns.toLowerCase(Locale.ROOT);
                String correctAns = mcq_answers.get(i);
                correctAns = correctAns.replaceAll("\\s", "");
                correctAns = correctAns.toLowerCase(Locale.ROOT);
                System.out.println(noSpaceAns);
                System.out.println(correctAns);
                if (noSpaceAns.equals(correctAns)) {
                    score++;
                }
            }
        }
        Toast.makeText(this, "SCORE: " + String.valueOf(score), Toast.LENGTH_LONG).show();
        String uid = currentUser.getUid();
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Object> score_new = new HashMap<>();
        score_new.put("date",ft.format(d));
        score_new.put("score",score);
        score_new.put("subject","mathematics");
        score_new.put("userId",uid);
        db.collection("scores").document().set(score_new);
        Intent i = new Intent(this, ScoreDisplay.class);
        i.putExtra("attempted",attempted);
        i.putExtra("correct",score);
        i.putExtra("wrong", 10-score);
        startActivity(i);
    }
}