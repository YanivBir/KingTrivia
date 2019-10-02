package com.example.kingtrivia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        this.userEmail = bundle.getString("email");

        TextView email = (TextView) findViewById(R.id.txt_email);
        email.setText("Welcome "+ this.userEmail.split("@")[0]);

        this.userId = firebaseAuth.getCurrentUser().getUid();

        btnLevl0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("level", "1");
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
                bundle.putString("level", "2");
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
                bundle.putString("level", "3");
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
                    Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();

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
                //finish();
            }
        });

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
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
}
