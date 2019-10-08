package com.example.kingtrivia;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateQuestionActivity extends AppCompatActivity{
    private EditText questionNumField;
    private EditText questionLevelField;
    private EditText questionField;
    private EditText ans1Field;
    private EditText ans2Field;
    private EditText ans3Field;
    private Button btnUpdate;
    private Button btnDel;
    private Button btnShow;
    private Button btnPrev;
    private Button btnNext;

    private Question question;

    private DatabaseReference reffDbQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatequestion);

        question = new Question();

        questionNumField = findViewById(R.id.txt_questionNumber);
        questionLevelField = findViewById(R.id.txt_questionLevel);
        questionField = findViewById(R.id.txt_question);
        ans1Field = findViewById(R.id.txt_ans1);
        ans2Field = findViewById(R.id.txt_ans2);
        ans3Field = findViewById(R.id.txt_ans3);
        btnUpdate =  findViewById(R.id.btnUpdate);
        btnDel = findViewById(R.id.btnDelete);
        btnShow = findViewById(R.id.btnShow);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(questionNumField.getText().toString())) {
                    questionNumField.setError("Required");
                }
                else if (TextUtils.isEmpty(questionLevelField.getText().toString()))
                {
                    questionLevelField.setError("Required");
                }
                else
                    showQuestion(questionNumField.getText().toString(), questionLevelField.getText().toString());
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(questionNumField.getText().toString())) {
                    questionNumField.setError("Required");
                }
                else if (TextUtils.isEmpty(questionLevelField.getText().toString()))
                {
                    questionLevelField.setError("Required");
                }
                else{
                    int questionId = Integer.parseInt(questionNumField.getText().toString());
                    if (questionId >1)
                        questionId--;
                    showQuestion(String.valueOf(questionId), String.valueOf(question.getLevel()));
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(questionNumField.getText().toString())) {
                    questionNumField.setError("Required");
                }
                else if (TextUtils.isEmpty(questionLevelField.getText().toString()))
                {
                    questionLevelField.setError("Required");
                }
                else{
                    int questionId = Integer.parseInt(questionNumField.getText().toString());
                    questionId++;
                    showQuestion(String.valueOf(questionId), String.valueOf(question.getLevel()));
                }
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (question.getIsActive() == 1) {
                    btnDel.setText("שחזר");
                    question.setIsActive(0);
                }
                else {
                    btnDel.setText("מחק");
                    question.setIsActive(1);
                }

                reffDbQuestions = FirebaseDatabase.getInstance().getReference().child("questions").child("level:"+String.valueOf(question.getLevel()));
                reffDbQuestions.child(String.valueOf(question.getId())).child("isActive").setValue(question.getIsActive());
                Toast.makeText(getApplicationContext(), "Question active status updated successfully", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm())
                    return;

                question.setTheQuestion(questionField.getText().toString().trim());
                question.setAns1(ans1Field.getText().toString().trim());
                question.setAns2(ans2Field.getText().toString().trim());
                question.setAns3(ans3Field.getText().toString().trim());

                reffDbQuestions = FirebaseDatabase.getInstance().getReference().child("questions").child("level:"+String.valueOf(question.getLevel()));
                reffDbQuestions.child(String.valueOf(question.getId())).setValue(question);
                Toast.makeText(getApplicationContext(), "Question updated successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQuestion (final String questionId, final String questionLevel) {
        Thread thread = new Thread() {
            public void run() {
                reffDbQuestions = FirebaseDatabase.getInstance().getReference().child("questions").
                        child("level:" + questionLevel).child(questionId);

                reffDbQuestions.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            question.setId(dataSnapshot.getValue(Question.class).getId());
                            question.setLevel(dataSnapshot.getValue(Question.class).getLevel());
                            question.setTheQuestion(dataSnapshot.getValue(Question.class).getTheQuestion());
                            question.setAns1(dataSnapshot.getValue(Question.class).getAns1());
                            question.setAns2(dataSnapshot.getValue(Question.class).getAns2());
                            question.setAns3(dataSnapshot.getValue(Question.class).getAns3());
                            question.setIsActive(dataSnapshot.getValue(Question.class).getIsActive());

                            questionNumField.setText(Integer.toString(question.getId()));
                            questionLevelField.setText(Integer.toString(question.getLevel()));
                            questionField.setText(question.getTheQuestion());
                            ans1Field.setText(question.getAns1());
                            ans2Field.setText(question.getAns2());
                            ans3Field.setText(question.getAns3());

                            if (question.getIsActive() == 1)
                                btnDel.setText("מחק");
                            else
                                btnDel.setText("שחזר");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };

        thread.start();
    }

    private boolean validateForm() {
        boolean result = true;

        if (TextUtils.isEmpty(questionNumField.getText().toString())) {
            questionNumField.setError("Required");
            result = false;
        } else {
            questionNumField.setError(null);
        }

        if (TextUtils.isEmpty(questionLevelField.getText().toString())) {
            questionLevelField.setError("Required");
            result = false;
        } else {
            questionLevelField.setError(null);
        }

        if (TextUtils.isEmpty(questionField.getText().toString())) {
            questionField.setError("Required");
            result = false;
        } else {
            questionField.setError(null);
        }

        if (TextUtils.isEmpty(ans1Field.getText().toString())) {
            ans1Field.setError("Required");
            result = false;
        } else {
            ans1Field.setError(null);
        }


        if (TextUtils.isEmpty(ans2Field.getText().toString())) {
            ans2Field.setError("Required");
            result = false;
        } else {
            ans2Field.setError(null);
        }

        if (TextUtils.isEmpty(ans3Field.getText().toString())) {
            ans3Field.setError("Required");
            result = false;
        } else {
            ans3Field.setError(null);
        }

        return result;
    }
}
