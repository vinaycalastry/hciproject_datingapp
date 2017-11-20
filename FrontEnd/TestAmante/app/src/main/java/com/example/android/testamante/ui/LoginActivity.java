package com.example.android.testamante.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.android.testamante.MainActivity;
import com.example.android.testamante.R;
import com.example.android.testamante.utils.UtilClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    private AutoCompleteTextView mUserNameView;
    private AutoCompleteTextView mUserPasswordView;
    private CheckBox rememberLoginBox;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // create view references here
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.loginUserName);
        mUserPasswordView=(AutoCompleteTextView) findViewById(R.id.loginUserPassword);
        rememberLoginBox=(CheckBox)findViewById(R.id.loginRememberMeCheckBox) ;

        Button signInUserBtn = (Button) findViewById(R.id.loginSignInBtn);
        signInUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button googleSignInUserBtn = (Button) findViewById(R.id.loginSignInGoogleBtn);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUserNameView.setError(null);
        mUserPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        final String password = mUserPasswordView.getText().toString();

        boolean isInValid = false;
        View focusView = null;

        // check for username validation
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            isInValid = true;
        } else if (!isUserNameValid(userName)) {
            mUserNameView.setError(getString(R.string.error_invalid_username));
            focusView = mUserNameView;
            isInValid = true;
        }

        // Check for a valid password, if the user entered one.
       else if (TextUtils.isEmpty(password))
        {
            mUserPasswordView.setError(getString(R.string.error_field_required));
            focusView = mUserPasswordView;
            isInValid = true;
        }




        if (isInValid) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            // perform the user login attempt.
            boolean rememberLogin = rememberLoginBox.isChecked();

            if (UtilClass.isNetworkAvailable(this)) {
                // check user login

                //authenticate user
                auth.signInWithEmailAndPassword(userName, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                //progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        mUserPasswordView.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(com.example.android.testamante.ui.LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(com.example.android.testamante.ui.LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            } else
                UtilClass.showToast(this, getResources().getString(R.string.network_error));
        }
    }


    public boolean isUserNameValid(String username) {
        //TODO: Replace this with your own logic
        return true;
    }

    public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


}
