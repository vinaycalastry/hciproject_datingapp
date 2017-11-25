package com.example.android.testamante.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.android.testamante.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button signInUserBtn = (Button) findViewById(R.id.splashSignInBtn);
        signInUserBtn.setOnClickListener(this);
        Button registerUserBtn = (Button) findViewById(R.id.splashRegisterBtn);
        registerUserBtn.setOnClickListener(this);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(com.example.android.testamante.ui.SplashActivity.this, ProfileActivity.class)); // Just for Testing Profile
            finish();
        }
        else{
            progressBar.setVisibility(View.GONE);
            signInUserBtn.setVisibility(View.VISIBLE);
            registerUserBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.splashRegisterBtn:
                startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                break;
            case R.id.splashSignInBtn:
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                break;
        }
    }
}
