package com.example.kingtrivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!= null)
        {
            Intent i = new Intent(getApplicationContext(), LevelActiviy.class);
            Bundle bundle = new Bundle();
            bundle.putString("email", firebaseAuth.getCurrentUser().getEmail());
            //Add the bundle to the intent
            i.putExtras(bundle);
            startActivity(i);
            finish();
        }

       final Button btnLogin = findViewById(R.id.btnLoginAct);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        final Button btnRegister = findViewById(R.id.btnRegisterAct);
            btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}

