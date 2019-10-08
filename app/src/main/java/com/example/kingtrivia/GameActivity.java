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
import java.util.Iterator;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private TextView mQuestionNumber;
    private TextView mLives;
    private TextView mQuestion;
    private Button mBtnAns1;
    private Button mBtnAns2;
    private Button mBtnAns3;

    public static final int QTNS_PER_LEVEL = 10;
    public static final int DELAY = 800; // 1000 means 1 sec
    public static final int QUESTION_SIZE = 25; // 1000 means 1 sec

    private ArrayList<Question> allQuestions;
    private ArrayList<Question> questions;
    private int current_question;
    private String level;
    private int lives;
    private int correctAnswers;
    private int wrongAnswers;
    private int questionCount;
    private  ArrayList<Integer> randomList;
    private Button saveCorrectButton;

    private DatabaseReference reffDbQuestions;

    private String questionSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        questions = new ArrayList<>();
        allQuestions = new ArrayList<>();
        current_question = 0;
        correctAnswers = 0;
        wrongAnswers = 0;

        mQuestionNumber = findViewById(R.id.txt_questionNumber);
        mLives = findViewById(R.id.txt_lives);
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
        switch(level)
        {
            case "1":
                lives=1;
                break;
            case "2":
                lives=2;
                break;
            case "3":
                lives=3;
                break;
            default:
                lives=3;
                break;
        }

        questionSize = bundle.getString("questionSize");
        if (questionSize.equals("1")) {
            mQuestion.setTextSize(QUESTION_SIZE);
            mBtnAns1.setTextSize(QUESTION_SIZE);
            mBtnAns2.setTextSize(QUESTION_SIZE);
            mBtnAns3.setTextSize(QUESTION_SIZE);
            mLives.setTextSize(QUESTION_SIZE);
            mQuestionNumber.setTextSize(QUESTION_SIZE);
        }

        Thread thread = new Thread() {
            public void run() {
                reffDbQuestions = FirebaseDatabase.getInstance().getReference().child("questions").child("level:" + level);
                reffDbQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                            questionCount = (int) dataSnapshot.getChildrenCount();
                        start_game();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        };

        thread.start();
    }

     private void start_game() {
        if (questionCount == 0) {
            Toast.makeText(getApplicationContext(), "No question at this level", Toast.LENGTH_SHORT).show();
            return;
        }

        readLevelQueestions ();
    }

    public void readLevelQueestions ()
    {
        Thread thread = new Thread(){
            public void run(){
                reffDbQuestions = FirebaseDatabase.getInstance().getReference().child("questions").child("level:" + level);
                reffDbQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> dataSnapshotsChat = dataSnapshot.getChildren().iterator();

                        while (dataSnapshotsChat.hasNext()) {
                            DataSnapshot dataSnapshotChild = dataSnapshotsChat.next();
                            Question q1 = new Question();
                            q1.setId(dataSnapshotChild.getValue(Question.class).getId());
                            q1.setLevel(dataSnapshotChild.getValue(Question.class).getLevel());
                            q1.setTheQuestion(dataSnapshotChild.getValue(Question.class).getTheQuestion());
                            q1.setAns1(dataSnapshotChild.getValue(Question.class).getAns1());
                            q1.setAns2(dataSnapshotChild.getValue(Question.class).getAns2());
                            q1.setAns3(dataSnapshotChild.getValue(Question.class).getAns3());
                            q1.setIsActive(dataSnapshotChild.getValue(Question.class).getIsActive());
                            allQuestions.add(q1);
                        }

                        readRandomQuestions();
                        getAndwriteQuestionToScreen(); //Write the first question to the screen
                        current_question++;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        };

        thread.start();
    }

    private void readRandomQuestions() {//Read random questions from db and put it on an array
        Random rand = new Random();
        for (int i = 0; i < QTNS_PER_LEVEL; i++) {
            int r = rand.nextInt(questionCount);
            while (randomList.contains(r)||(allQuestions.get(r).getIsActive()==0))
                r = rand.nextInt(questionCount);
            randomList.add(r);
            questions.add(allQuestions.get(r));
        }
    }

    private void getAndwriteQuestionToScreen()
    {
        mBtnAns1.setVisibility(View.VISIBLE);
        mBtnAns2.setVisibility(View.VISIBLE);
        mBtnAns3.setVisibility(View.VISIBLE);

        mQuestion.setText(questions.get(current_question).getTheQuestion());
        randAnswers ();
        mLives.setText("lives: "+lives);
        mQuestionNumber.setText(String.valueOf(current_question+1)+"/"+ String.valueOf(QTNS_PER_LEVEL));
    }

    private void randAnswers ()
    {
        Random rand = new Random();
        int r = rand.nextInt(6) + 1;

        switch (r)
        {
            case 1:
                mBtnAns1.setText(questions.get(current_question).getAns1());
                mBtnAns2.setText(questions.get(current_question).getAns2());
                mBtnAns3.setText(questions.get(current_question).getAns3());
                saveCorrectButton = mBtnAns1;
                break;
            case 2:
                mBtnAns1.setText(questions.get(current_question).getAns3());
                mBtnAns2.setText(questions.get(current_question).getAns2());
                mBtnAns3.setText(questions.get(current_question).getAns1());
                saveCorrectButton = mBtnAns3;
                break;
            case 3:
                mBtnAns1.setText(questions.get(current_question).getAns1());
                mBtnAns2.setText(questions.get(current_question).getAns3());
                mBtnAns3.setText(questions.get(current_question).getAns2());
                saveCorrectButton = mBtnAns1;
                break;
            case 4:
                mBtnAns1.setText(questions.get(current_question).getAns2());
                mBtnAns2.setText(questions.get(current_question).getAns3());
                mBtnAns3.setText(questions.get(current_question).getAns1());
                saveCorrectButton = mBtnAns3;
                break;
            case 5:
                mBtnAns1.setText(questions.get(current_question).getAns3());
                mBtnAns2.setText(questions.get(current_question).getAns1());
                mBtnAns3.setText(questions.get(current_question).getAns2());
                saveCorrectButton = mBtnAns2;
                break;
            case 6:
                mBtnAns1.setText(questions.get(current_question).getAns2());
                mBtnAns2.setText(questions.get(current_question).getAns1());
                mBtnAns3.setText(questions.get(current_question).getAns3());
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