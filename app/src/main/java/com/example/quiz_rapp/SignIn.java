package com.example.quiz_rapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    EditText userEmail, password;
    Button signInBtn;
    TextView signUp;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userEmail = findViewById(R.id.userEmail);
        password = findViewById(R.id.password);
        signInBtn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signIn);
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, SignUp.class));

            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = userEmail.getText().toString().trim();
                String mPassword = password.getText().toString();

                if (TextUtils.isEmpty(mEmail)) {
                    userEmail.setError("Please enter your email");
                    return;
                }
                if (TextUtils.isEmpty(mPassword)) {
                    password.setError("Please enter your password");
                    return;
                }
                progressBar.setVisibility(view.VISIBLE);
                mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignIn.this, "User Logged in successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignIn.this, MainActivity.class));
                            finishAffinity();
                        }else{
                            Toast.makeText(SignIn.this, "Please check your User Name and Password or Create a new Account", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }


                });
            }

            });
        }
    }