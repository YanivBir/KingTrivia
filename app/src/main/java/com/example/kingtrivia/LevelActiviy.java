package com.example.kingtrivia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LevelActiviy extends AppCompatActivity{
    private String userEmail;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;
    private String userId;

    private Button btnLogout;
    private Button btnAdmin;
    private Button btnLevl0;
    private Button btnLevl1;
    private Button btnLevl2;
    private Switch switchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");

        btnAdmin= findViewById(R.id.btnAdminInter);
        btnLogout = findViewById(R.id.btnLogout);
        btnLevl0 = findViewById(R.id.btnLevel0);
        btnLevl1 = findViewById(R.id.btnLevel1);
        btnLevl2 = findViewById(R.id.btnLevel2);
        switchBtn = findViewById(R.id.switchBtn);

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        this.userEmail = bundle.getString(getResources().getString(R.string.email));

        TextView email = (TextView) findViewById(R.id.txt_email);
        email.setText("Welcome "+ this.userEmail.split("@")[0]);

        this.userId = firebaseAuth.getCurrentUser().getUid();

        btnLevl0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("level", "1");
                if (switchBtn.isChecked())
                    bundle.putString(getResources().getString(R.string.questionSize), "1");
                else
                    bundle.putString(getResources().getString(R.string.questionSize), "0");
                //Add the bundle to the intent
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        btnLevl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(getResources().getString(R.string.level), "2");
                if (switchBtn.isChecked())
                    bundle.putString(getResources().getString(R.string.questionSize), "1");
                else
                    bundle.putString(getResources().getString(R.string.questionSize), "0");
                //Add the bundle to the intent
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        btnLevl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(getResources().getString(R.string.level), "3");
                if (switchBtn.isChecked())
                    bundle.putString(getResources().getString(R.string.questionSize), "1");
                else
                    bundle.putString(getResources().getString(R.string.questionSize), "0");
                //Add the bundle to the intent
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser()!= null) {
                    firebaseAuth.signOut();
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.logoutMsg),
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(i);
            }
        });

        Thread thread = new Thread(){
            public void run(){
        Query userQuery = databaseUsers.child(userId);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    final Button btnAdmin = findViewById(R.id.btnAdminInter);
                    if ((singleSnapshot.getValue().toString()).equals("1"))
                        btnAdmin.setVisibility(View.VISIBLE);
                    else
                        btnAdmin.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
            }
        };

        thread.start();
    }
}
