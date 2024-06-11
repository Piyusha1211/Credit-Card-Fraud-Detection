package com.example.creditcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    private EditText eTFulNam, eTEml, eTDoB, eTMob, eTPwd, eTConPwd;
    private ProgressBar progBr;
    private RadioGroup rdoGrpGd;
    private RadioButton rdoBtnGdSel;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Register");
        }
        Toast.makeText(Register.this, "You can register now", Toast.LENGTH_LONG).show();

        auth = FirebaseAuth.getInstance();

        progBr = findViewById(R.id.progbar);
        eTFulNam = findViewById(R.id.fnam_reg);
        eTEml = findViewById(R.id.emil_reg);
        eTDoB = findViewById(R.id.dob_reg);
        eTMob = findViewById(R.id.mobno_reg);
        eTPwd = findViewById(R.id.Passd_reg);
        eTConPwd = findViewById(R.id.confPassd_reg);
        rdoGrpGd = findViewById(R.id.gend_reg);

        eTDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        Button buttonRegister = findViewById(R.id.btn_reg);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog picker = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                eTDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        picker.show();
    }

    private void registerNewUser() {
        String textFullName = eTFulNam.getText().toString().trim();
        String textEmail = eTEml.getText().toString().trim();
        String textDoB = eTDoB.getText().toString().trim();
        String textMobile = eTMob.getText().toString().trim();
        String textPwd = eTPwd.getText().toString().trim();
        String textConfirmPwd = eTConPwd.getText().toString().trim();

        if (!validateInput(textFullName, textEmail, textDoB, textMobile, textPwd, textConfirmPwd)) {
            return;
        }

        int selectedGenderId = rdoGrpGd.getCheckedRadioButtonId();
        rdoBtnGdSel = findViewById(selectedGenderId);
        String textGender = rdoBtnGdSel.getText().toString();

        progBr.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progBr.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    if (firebaseUser != null) {
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                        firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Register", "User profile updated.");
                                } else {
                                    Log.e("RegisterError", "Profile update failed", task.getException());
                                }
                            }
                        });

                        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textDoB, textGender, textMobile);
                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                        referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Register.this, "User registered successfully. Verification email sent.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(Register.this, Login.class); // Open Login activity
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Log.e("RegisterError", "Email verification failed", task.getException());
                                                Toast.makeText(Register.this, "Email could not be sent for verification.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    Log.e("RegisterError", "Saving user details failed", task.getException());
                                    Toast.makeText(Register.this, "Registration failed. Please try again.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                } else {
                    Log.e("RegisterError", "User registration failed", task.getException());
                    Toast.makeText(Register.this, "Registration failed. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validateInput(String fullName, String email, String dob, String mobile, String password, String confirmPassword) {
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(dob) ||
                TextUtils.isEmpty(mobile) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
            return false;
        } else if (rdoGrpGd.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_LONG).show();
            return false;
        } else if (mobile.length() != 8) {
            Toast.makeText(this, "Mobile number should be 8 digits", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(this, "Password should be at least 6 characters long", Toast.LENGTH_LONG).show();
            return false;
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
