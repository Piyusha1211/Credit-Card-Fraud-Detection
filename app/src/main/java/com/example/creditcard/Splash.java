package com.example.creditcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(3000);
                    startActivity(new Intent(Splash.this, Login.class));
                    finish();

                }catch (Exception e){

                }
            }
        }; thread.start();

    }
}
