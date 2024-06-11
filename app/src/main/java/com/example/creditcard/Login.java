package com.example.creditcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.Fragments.ProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth aProf;
    private EditText em;
    private EditText pass;
    private CheckBox rbMCkBx;

    private ImageView gB;

    private static final int RC_SIGN_IN = 9001;
    private SharedPreferences shdPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        aProf = FirebaseAuth.getInstance();

        em = findViewById(R.id.Emal);
        pass = findViewById(R.id.Passd);
        gB = findViewById(R.id.g_bn);
        rbMCkBx = findViewById(R.id.remchkbox);

        shdPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        // Verify that Remember Me is activated and type in the email
        if (shdPref.getBoolean("rememberMe", false)) {
            em.setText(shdPref.getString("email", ""));
            rbMCkBx.setChecked(true);
        }

        // Display Password Hidden with Eye Icon
        ImageView ImageViewShowHidPwd = findViewById(R.id.eye);
        ImageViewShowHidPwd.setImageResource(R.drawable.icons_eye);
        ImageViewShowHidPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    // If the password is apparent conceal it
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // Change Icon
                    ImageViewShowHidPwd.setImageResource(R.drawable.hide_eye);
                } else {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ImageViewShowHidPwd.setImageResource(R.drawable.icons_eye);
                }
            }
        });

        findViewById(R.id.fgtpasswd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Login.this, "You can reset your Password now!", Toast.LENGTH_SHORT).show();
                //Open Password Reset Activity
                 Intent intent = new Intent(Login.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.logBn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eml = em.getText().toString().trim();
                String pass1 = pass.getText().toString();

                if (TextUtils.isEmpty(eml)) {
                    Toast.makeText(Login.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
                    em.setError("Email is required");
                    return;
                }

                aProf.signInWithEmailAndPassword(eml, pass1)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = aProf.getCurrentUser();
                                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();

                                    // Save login state if Remember Me is checked
                                    SharedPreferences.Editor editor = shdPref.edit();
                                    if (rbMCkBx.isChecked()) {
                                        editor.putBoolean("rememberMe", true);
                                        editor.putString("email", eml);
                                    } else {
                                        editor.clear();
                                    }
                                    editor.apply();

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.e("LoginError", "Authentication failed", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.tV_reg_lk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This is the point at which to complete the registration process
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}

