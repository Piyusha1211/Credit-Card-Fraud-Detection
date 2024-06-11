package com.example.creditcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {



        //Declaration
        Button btnRt, btnBk;
        EditText edtEml;
        ProgressBar pgBar;
        FirebaseAuth mAut;
        String stEml;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_forgot_password);

            //Initializaton
            btnBk = findViewById(R.id.bFtPassdB);
            btnRt = findViewById(R.id.bRet);
            edtEml = findViewById(R.id.eFgtPdEml);
            pgBar = findViewById(R.id.ftPassdProgb);

            mAut = FirebaseAuth.getInstance();

            //Reset Button Listener
            btnRt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stEml = edtEml.getText().toString().trim();
                    if (!TextUtils.isEmpty(stEml)) {
                        ResetPassword();
                    } else {
                        edtEml.setError("Email field can't be empty");
                    }
                }
            });


            //Back Button Code
            btnBk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ForgotPasswordActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            });

        }

        private void ResetPassword() {
            pgBar.setVisibility(View.VISIBLE);
            btnRt.setVisibility(View.INVISIBLE);

            mAut.sendPasswordResetEmail(stEml)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ForgotPasswordActivity.this, "Reset Password link has been sent to your registered Email", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgotPasswordActivity.this, "Error :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            pgBar.setVisibility(View.INVISIBLE);
                            btnRt.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

