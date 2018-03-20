package com.lovera.diego.restock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //region fields
    private EditText loginActivityEditEmail, loginActivityEditPassword;
    private TextInputLayout loginActivityLayoutEmail, loginActivityLayoutPassword;
    private Button loginActivityButtonLogin, loginActivityButtonSignUp;
    private LoginButton login_button_facebook;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    public static FirebaseUser ACTUAL_USER;
    //endregion

    //Activity life cycle
    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button_facebook = findViewById(R.id.login_button_facebook);

        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        login_button_facebook.setReadPermissions("email", "public_profile");
        login_button_facebook.registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                //Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                //Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        loginActivityEditEmail = findViewById(R.id.loginActivityTextInputEditEmail);
        loginActivityEditPassword = findViewById(R.id.loginActivityTextInputEditPassword);
        loginActivityLayoutEmail = findViewById(R.id.loginActivityTextInputLayoutEmail);
        loginActivityLayoutPassword = findViewById(R.id.loginActivityTextInputLayoutPassword);

        loginActivityButtonLogin = findViewById(R.id.loginActivityLoginButton);
        loginActivityButtonSignUp = findViewById(R.id.loginActivitySignUpButton);

        loginActivityEditEmail.addTextChangedListener(new MyTextWatcher(loginActivityEditEmail));
        loginActivityEditPassword.addTextChangedListener(new MyTextWatcher(loginActivityEditPassword));

        loginActivityButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        loginActivityButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
    //endregion
    //region onStart
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    //endregion

    //Facebook login
    //region onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    //endregion
    //region handleFacebookAccessToken
    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            ACTUAL_USER = mAuth.getCurrentUser();
                            Intent i = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(i);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
    //endregion

    //Email login
    //region login
    private void login() {
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(loginActivityEditEmail.getText().toString(),
                loginActivityEditPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            ACTUAL_USER = mAuth.getCurrentUser();
                            if (ACTUAL_USER != null) {
                                startActivity(new Intent(LoginActivity.this,
                                        MainActivity.class));
                            }
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                        // ...
                    }
                });
    }
    //endregion
    //region validateEmail
    private boolean validateEmail() {
        String email = loginActivityEditEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            loginActivityLayoutEmail.setError(getString(R.string.activity_login_email_error));
            //requestFocus(loginActivityEditEmail);
            return false;
        } else {
            loginActivityLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }
    //endregion
    //region validatePassword
    private boolean validatePassword() {
        if (loginActivityEditPassword.getText().toString().trim().isEmpty()) {
            loginActivityLayoutPassword.setError(getString(R.string.activity_login_email_password_error));
            //requestFocus(loginActivityEditPassword);
            return false;
        } else {
            loginActivityLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }
    //endregion
    //region isValidEmail
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    //endregion
    //region requestFocus
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    //endregion
    //region MyTextWatcher
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.loginActivityTextInputEditEmail:
                    validateEmail();
                    break;
                case R.id.loginActivityTextInputEditPassword:
                    validatePassword();
                    break;
            }
        }
    }
    //endregion
}