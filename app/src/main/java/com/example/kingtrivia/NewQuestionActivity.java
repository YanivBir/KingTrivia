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

public class NewQuestionActivity extends AppCompatActivity {
    private EditText levelQuesField;
    private EditText questionField;
    private EditText ans1Field;
    private EditText ans2Field;
    private EditText ans3Field;
    private int maxid1 =1;
    private int maxid2 =1;
    private int maxid3 =1;
    private DatabaseReference reffDbQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newquestion);

        levelQuesField = findViewById(R.id.txt_level);
        questionField = findViewById(R.id.txt_question);
        ans1Field = findViewById(R.id.txt_ans1);
        ans2Field = findViewById(R.id.txt_ans2);
        ans3Field = findViewById(R.id.txt_ans3);

        reffDbQuestions = FirebaseDatabase.getInstance().getReference().child("questions").child("level:1");
        reffDbQuestions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxid1= (int) dataSnapshot.getChildrenCount() +1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reffDbQuestions = FirebaseDatabase.getInstance().getReference().child("questions").child("level:2");
        reffDbQuestions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxid2= (int) dataSnapshot.getChildrenCount() +1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reffDbQuestions = FirebaseDatabase.getInstance().getReference().child("questions").child("level:3");
        reffDbQuestions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxid3= (int) dataSnapshot.getChildrenCount() +1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
                registerQuestion();
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(levelQuesField.getText().toString())) {
            levelQuesField.setError("Required");
            result = false;
        } else {
            levelQuesField.setError(null);
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

    private void registerQuestion() {
        Question q1 = new Question(0,  Integer.parseInt(levelQuesField.getText().toString().trim()),
        questionField.getText().toString().trim(),
        ans1Field.getText().toString().trim(), ans2Field.getText().toString().trim(),
        ans3Field.getText().toString().trim(), 1);
        reffDbQuestions = FirebaseDatabase.getInstance().getReference().child("questions");

        if (levelQuesField.getText().toString().equals("1")) {
            q1.setId(maxid1);
            reffDbQuestions.child("level:" + levelQuesField.getText()).child(String.valueOf(maxid1)).setValue(q1);
        }
        else if (levelQuesField.getText().toString().equals("2")) {
            q1.setId(maxid2);
            reffDbQuestions.child("level:" + levelQuesField.getText()).child(String.valueOf(maxid2)).setValue(q1);
        }
        else {
            q1.setId(maxid3);
            reffDbQuestions.child("level:" + levelQuesField.getText()).child(String.valueOf(maxid3)).setValue(q1);
        }

        Toast.makeText(getApplicationContext(), "Question added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


}
