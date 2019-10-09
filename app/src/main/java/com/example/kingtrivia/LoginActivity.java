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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity{

    //private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!= null)
        {
            Intent i = new Intent(getApplicationContext(), LevelActiviy.class);
            Bundle bundle = new Bundle();
            bundle.putString(getResources().getString(R.string.email), firebaseAuth.getCurrentUser().getEmail());
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
            mEmailField.setError(getResources().getString(R.string.Required));
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError(getResources().getString(R.string.Required));
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
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.sucessMsg), Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(LoginActivity.this, LevelActiviy.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(getResources().getString(R.string.email), email);
                            //Add the bundle to the intent
                            i.putExtras(bundle);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,  getResources().getString(R.string.connectError), Toast.LENGTH_SHORT).show();
                        }
                }
                });
    }
}
