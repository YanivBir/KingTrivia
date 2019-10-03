package com.example.kingtrivia;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EndGameActivity extends AppCompatActivity {
    private TextView mCorrect;
    private TextView mWrong;
    private TextView mLife;
    private TextView mAllGrades;

    private Button returnLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        mCorrect = findViewById(R.id.txtCorrect);
        mWrong = findViewById(R.id.txtUnCorrect);
        mLife =  findViewById(R.id.txtLife);
        mAllGrades =  findViewById(R.id.txtAllGrades);
        returnLevel =  findViewById(R.id.btnHome);

        Bundle bundle = getIntent().getExtras();
        String correct = bundle.getString("correct");
        String wrong = bundle.getString("wrong");
        String life = bundle.getString("life");

        int total = 10*Integer.parseInt(correct) + (-5)*Integer.parseInt(wrong) + (15)*Integer.parseInt(life);
        if (total < 0)
            total = 0;

        mCorrect.setText(correct);
        mWrong.setText(wrong);
        mLife.setText(life);
        mAllGrades.setText(String.valueOf(total));

        returnLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();;
            }
        });
    }
}
