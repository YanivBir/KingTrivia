package com.example.kingtrivia;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private TextView mQuestionNumber;
    private TextView mLives;
    private TextView mlLevel;
    private TextView mQuestion;
    private Button mBtnAns1;
    private Button mBtnAns2;
    private Button mBtnAns3;

    public static final int QTNS_PER_LEVEL = 3; // change it to 10!
    public static final int DELAY = 800; // 5000 means 5 sec

    private Question[] questions;
    private int current_question;
    private String level;
    private int lives;
    private int correctAnswers;
    private int wrongAnswers;
    private int questionCount;
    private  ArrayList<Integer> randomList;

    private Button saveCorrectButton;

    private DatabaseReference reffDbQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        questions = new Question[QTNS_PER_LEVEL];
        current_question = 0;
        lives = 3;
        correctAnswers = 0;
        wrongAnswers = 0;

        mQuestionNumber = findViewById(R.id.txt_questionNumber);
        mLives = findViewById(R.id.txt_lives);
        mlLevel = findViewById(R.id.txt_level);
        mQuestion = findViewById(R.id.txt_question);
        mBtnAns1 = findViewById(R.id.btnAns1);
        mBtnAns2 = findViewById(R.id.btnAns2);
        mBtnAns3 = findViewById(R.id.btnAns3);
        randomList = new ArrayList<Integer>();

        mBtnAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Choose ans1
                try {
                    chooseAnswer(mBtnAns1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mBtnAns2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Choose ans2
                try {
                    chooseAnswer(mBtnAns2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mBtnAns3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Choose ans3
                try {
                    chooseAnswer(mBtnAns3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        level = bundle.getString("level");
        setTextViewLabel(level);

        reffDbQuestions = FirebaseDatabase.getInstance().getReference().child("questions").child("level:"+level);
        reffDbQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    questionCount= (int) dataSnapshot.getChildrenCount();
                start_game();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setTextViewLabel (String Level)
    {
        if (Level.equals("1"))
            mlLevel.setText("שאלות ברמה קלה");
        else if (Level.equals("2"))
            mlLevel.setText("שאלות ברמה בינונית");
        else if (Level.equals("3"))
            mlLevel.setText("שאלות ברמה קשה");
        else
            mlLevel.setText("Error level");
    }

    private void start_game() {
        if (questionCount == 0) {
            Toast.makeText(getApplicationContext(), "No question at this level", Toast.LENGTH_SHORT).show();
            return;
        }
        readRandomQuestions();
    }

    private void readRandomQuestions() {//Read random questions from db and put it on an array
        Random rand = new Random();
       // ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < QTNS_PER_LEVEL; i++) {
            int r = rand.nextInt(questionCount) + 1;
            while (randomList.contains(r))
                r = rand.nextInt(questionCount) + 1;
            randomList.add(r);
        }

        getAndwriteQuestionToScreen();
    }

    private void getAndwriteQuestionToScreen()
    {
        reffDbQuestions = FirebaseDatabase.getInstance().getReference().child("questions").child("level:" + level).child(String.valueOf(randomList.get(current_question)));
        reffDbQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questions[current_question] = new Question();
                questions[current_question].setId(dataSnapshot.getValue(Question.class).getId());
                questions[current_question].setLevel(dataSnapshot.getValue(Question.class).getLevel());
                questions[current_question].setTheQuestion(dataSnapshot.getValue(Question.class).getTheQuestion());
                questions[current_question].setAns1(dataSnapshot.getValue(Question.class).getAns1());
                questions[current_question].setAns2(dataSnapshot.getValue(Question.class).getAns2());
                questions[current_question].setAns3(dataSnapshot.getValue(Question.class).getAns3());
                questions[current_question].setIsActive(dataSnapshot.getValue(Question.class).getIsActive());

                mQuestion.setText(questions[current_question].getTheQuestion());
                randAnswers ();

                mLives.setText("lives: "+lives);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void randAnswers ()
    {// TODO:make it random
        Random rand = new Random();
        int r = rand.nextInt(6) + 1;

        switch (r)
        {
            case 1:
                mBtnAns1.setText(questions[current_question].getAns1());
                mBtnAns2.setText(questions[current_question].getAns2());
                mBtnAns3.setText(questions[current_question].getAns3());
                saveCorrectButton = mBtnAns1;
                break;
            case 2:
                mBtnAns1.setText(questions[current_question].getAns3());
                mBtnAns2.setText(questions[current_question].getAns2());
                mBtnAns3.setText(questions[current_question].getAns1());
                saveCorrectButton = mBtnAns3;
                break;
            case 3:
                mBtnAns1.setText(questions[current_question].getAns1());
                mBtnAns2.setText(questions[current_question].getAns3());
                mBtnAns3.setText(questions[current_question].getAns2());
                saveCorrectButton = mBtnAns1;
                break;
            case 4:
                mBtnAns1.setText(questions[current_question].getAns2());
                mBtnAns2.setText(questions[current_question].getAns3());
                mBtnAns3.setText(questions[current_question].getAns1());
                saveCorrectButton = mBtnAns3;
                break;
            case 5:
                mBtnAns1.setText(questions[current_question].getAns3());
                mBtnAns2.setText(questions[current_question].getAns1());
                mBtnAns3.setText(questions[current_question].getAns2());
                saveCorrectButton = mBtnAns2;
                break;
            case 6:
                mBtnAns1.setText(questions[current_question].getAns2());
                mBtnAns2.setText(questions[current_question].getAns1());
                mBtnAns3.setText(questions[current_question].getAns3());
                saveCorrectButton = mBtnAns2;
                break;
        }

    }

    private void chooseAnswer (final Button btnChoose) throws InterruptedException {
        if (btnChoose.getText().toString().equals(saveCorrectButton.getText().toString()))
        {
            correctAnswers++;
            btnChoose.setBackgroundColor(Color.parseColor("#228B22"));//green
        }
        else
        {
            wrongAnswers++;
            lives--;
            btnChoose.setBackgroundColor(Color.parseColor("#FF6347")); //red
            saveCorrectButton.setBackgroundColor(Color.parseColor("#228B22"));//green
        }

        Handler handler=new Handler();
        Runnable r=new Runnable() {
            public void run() {
                btnChoose.setBackgroundColor(Color.parseColor("#C0C0C0"));
                saveCorrectButton.setBackgroundColor(Color.parseColor("#C0C0C0"));
                current_question++;

                if ((current_question < QTNS_PER_LEVEL)&&(lives>0))
                    getAndwriteQuestionToScreen();
                else
                {
                    Intent i = new Intent(getApplicationContext(), EndGameActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("correct", String.valueOf(correctAnswers));
                    bundle.putString("wrong", String.valueOf(wrongAnswers));
                    bundle.putString("life", String.valueOf(lives));
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }
            }
        };
        handler.postDelayed(r, DELAY);
    }
}
