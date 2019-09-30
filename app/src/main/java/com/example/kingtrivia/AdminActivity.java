package com.example.kingtrivia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        final Button btnNewQuestion= findViewById(R.id.btnNewQuestion);
        btnNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NewQuestionActivity.class);
                startActivity(i);
                finish();
            }
        });

        final Button btnUpdateQuestion= findViewById(R.id.btnEditQuestions);
        btnUpdateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UpdateQuestionActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
