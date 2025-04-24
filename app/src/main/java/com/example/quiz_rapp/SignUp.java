package com.example.quiz_rapp;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText userName, userEmail, userPhone, password;
    Button register;
    TextView signIn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userPhone = findViewById(R.id.usePhone);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        signIn = findViewById(R.id.signIn);
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(SignUp.this, MainActivity.class));
            finishAffinity();
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, SignIn.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mUserName = userName.getText().toString();
                String mEmail = userEmail.getText().toString().trim();
                String mPhone = userPhone.getText().toString().trim();
                String mPassword = password.getText().toString();

                if (TextUtils.isEmpty(mUserName)) {
                    userName.setError("Please enter your name");
                    return;
                }
                if (TextUtils.isEmpty(mEmail)) {
                    userEmail.setError("Please enter your email");
                    return;
                }
                if (TextUtils.isEmpty(mPassword)) {
                    password.setError("Please enter your password");
                    return;
                }
                if (TextUtils.isEmpty(mPhone)) {
                    userPhone.setError("Please enter your phone");
                    return;
                }
                if (mPassword.length() < 8) {
                    password.setError("Password must be greater than 8 characters");
                    return;

                }
                progressBar.setVisibility(view.VISIBLE);
                mAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser mUser = mAuth.getCurrentUser();
                            mUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SignUp.this, "Registration Successful", Toast.LENGTH_LONG).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "On Failure: Email not send" + e.getMessage());

                                }
                            });
                            DocumentReference documentReference = mStore.collection("users").document(mUser.getUid());
                            Map<String, Object> user = new HashMap<>();
                            user.put("Name", mUserName);
                            user.put("Email", mEmail);
                            user.put("PhNumber", mPhone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "User profile created");

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "On faid: " + e.getMessage());
                                }
                            });
                            Toast.makeText(SignUp.this, "User Registered", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                            finishAffinity();
                        }else{
                            Toast.makeText(SignUp.this, "Registered Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }


        });

    }
}
