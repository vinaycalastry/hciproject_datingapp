package com.example.android.testamante.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.testamante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {


    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneEditText;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        init();
    }

    private void init() {
        usernameEditText = (EditText) findViewById(R.id.registerUserName);
        emailEditText = (EditText) findViewById(R.id.registerUserEmail);
        passwordEditText = (EditText) findViewById(R.id.registerUserPassword);
        phoneEditText = (EditText) findViewById(R.id.registerUserPhone);

        Button signUpBtn = (Button) findViewById(R.id.registerSignUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {
        // Reset errors.
        usernameEditText.setError(null);
        emailEditText.setError(null);
        passwordEditText.setError(null);
        phoneEditText.setError(null);

        // Store values at the time of the register attempt.
        String userName = usernameEditText.getText().toString();
        String userEmail = emailEditText.getText().toString();
        String userPassword = passwordEditText.getText().toString();
        String userPhone = phoneEditText.getText().toString();

        boolean isInValid = false;
        View focusView = null;

        // check for username validation
        if (TextUtils.isEmpty(userName)) {
            usernameEditText.setError(getString(R.string.error_field_required));
            focusView = usernameEditText;
            isInValid = true;
        } else if (!isUserNameValid(userName)) {
            usernameEditText.setError(getString(R.string.error_invalid_username));
            focusView = usernameEditText;
            isInValid = true;
        }

        // check for userEmail validation
        else if (TextUtils.isEmpty(userEmail)) {
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            isInValid = true;
        } else if (!isEmailValid(userEmail)) {
            emailEditText.setError(getString(R.string.error_invalid_email));
            focusView = emailEditText;
            isInValid = true;
        }

        // Check for a valid password, if the user entered one.
        else if (TextUtils.isEmpty(userPassword)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            isInValid = true;
        }

        // check for userPhone validation
        else if (TextUtils.isEmpty(userPhone)) {
            phoneEditText.setError(getString(R.string.error_field_required));
            focusView = phoneEditText;
            isInValid = true;
        } else if (!isPhoneValid(userPhone)) {
            phoneEditText.setError(getString(R.string.error_invalid_phone));
            focusView = phoneEditText;
            isInValid = true;
        }

        if (isInValid) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //Register
            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                Intent intent = new Intent(com.example.android.testamante.ui.RegisterActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(com.example.android.testamante.ui.RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // [START_EXCLUDE]
                            //hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
        }
    }

    public boolean isUserNameValid(String username) {
        //TODO: Replace this with your own logic
        return true;
    }

    public boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() == 10;
    }

    public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

}
