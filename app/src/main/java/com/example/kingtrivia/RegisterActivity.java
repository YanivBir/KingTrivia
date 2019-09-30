package com.example.kingtrivia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity{
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mPasswordAgainField;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseUsers = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        // Views
        mEmailField = findViewById(R.id.txt_email);
        mPasswordField = findViewById(R.id.txt_password);
        mPasswordAgainField = findViewById(R.id.txt_passwordAgain);

        final Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }

                registerUser();
               }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordAgainField.getText().toString())) {
            mPasswordAgainField.setError("Required");
            result = false;
        } else {
            mPasswordAgainField.setError(null);
        }

        String password = mPasswordField.getText().toString().trim();
        String passwordAgain = mPasswordAgainField.getText().toString().trim();
        if (!password.equals(passwordAgain)) {
            mPasswordAgainField.setError("Not match");
            result = false;
        } else {
            mPasswordAgainField.setError(null);
        }

        return result;
    }

    private void registerUser() {
        final String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        Toast.makeText(RegisterActivity.this, "Registering user...", Toast.LENGTH_SHORT).show();


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            //user is succesfully registerd and logged in
                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(RegisterActivity.this, LevelActiviy.class));
                           // finish();

                            User u1 = new User(0);
                            databaseUsers.child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(u1);
                            Toast.makeText(RegisterActivity.this, "User saved to DB.", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(RegisterActivity.this, LevelActiviy.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            //Add the bundle to the intent
                            i.putExtras(bundle);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Could not register, please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}