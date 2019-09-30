package com.example.kingtrivia;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity{

    //private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginButton;

   // private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        // Views
        mEmailField = findViewById(R.id.txt_email);
        mPasswordField = findViewById(R.id.txt_password);
        mLoginButton = findViewById(R.id.btnLogin);

       // progressDialog = new ProgressDialog(this);

        final Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
                    userLogin ();
               }
        });
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

        return result;
    }

    public void userLogin ()
    {
        final String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,  new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            //user is succesfully registerd and logged in
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(RegisterActivity.this, LevelActiviy.class));
                            // finish();

                            Intent i = new Intent(LoginActivity.this, LevelActiviy.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            //Add the bundle to the intent
                            i.putExtras(bundle);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Could not Login, please try again.", Toast.LENGTH_SHORT).show();
                        }
                }
                });
    }
}
