package com.lovera.diego.restock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

public class LoginActivity extends AppCompatActivity {
    //region Constants
    public static int RC_SIGN_IN = 200;
    //endregion
    //region Fields
    private EditText mLoginActivityEditEmail, mLoginActivityEditPassword;
    private TextInputLayout mLoginActivityLayoutEmail, mLoginActivityLayoutPassword;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private int mLoginSelected = 0;
    //endregion

    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        //region UI elements assignation
        mLoginActivityLayoutEmail = findViewById(R.id.text_input_layout_login_activity_email);
        mLoginActivityEditEmail = findViewById(R.id.text_input_edit_login_activity_email);
        mLoginActivityLayoutPassword = findViewById(R.id.text_input_layout_login_activity_password);
        mLoginActivityEditPassword = findViewById(R.id.text_input_edit_login_activity_password);
        Button loginActivityButtonLogin = findViewById(R.id.button_login_activity_login);
        Button loginActivityButtonSignUp = findViewById(R.id.button_login_activity_sign_up);
        //endregion

        //region Google sign in configuration
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn
                .getClient(this, googleSignInOptions);
        GoogleSignInButton loginGoogleButton = findViewById(R.id.login_button_google2);

        loginGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginSelected = 1;
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        //endregion
        //region Facebook sign in configuration
        LoginButton loginFacebookButton = findViewById(R.id.login_button_facebook);
        mCallbackManager = CallbackManager.Factory.create();
        loginFacebookButton.setReadPermissions("email", "public_profile");
        loginFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
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
        loginFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginSelected = 2;
            }
        });
        //endregion
        //region Email sign in configuration
        mLoginActivityEditEmail.addTextChangedListener(new MyTextWatcher(mLoginActivityEditEmail));
        mLoginActivityEditPassword.addTextChangedListener(new MyTextWatcher(mLoginActivityEditPassword));

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
        //endregion
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
    //region onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mLoginSelected == 1){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        } else if (mLoginSelected == 2){
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    //endregion

    //region firebaseAuthWithGoogle
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            RestockApp.ACTUAL_USER = mAuth.getCurrentUser();
                            Intent i = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //endregion
    //region handleFacebookAccessToken
    private void handleFacebookAccessToken(final AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);
        //TODO: Al iniciar sesión con facebook debemos validar que ya haya ingresado toda la información necesaria para conituar en casa de una compra
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            RestockApp.ACTUAL_USER = mAuth.getCurrentUser();
                            LoginManager.getInstance().logOut();
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
    //region Email login
    private void login() {
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(mLoginActivityEditEmail.getText().toString(),
                mLoginActivityEditPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            RestockApp.ACTUAL_USER = mAuth.getCurrentUser();
                            if (RestockApp.ACTUAL_USER != null) {
                                startActivity(new Intent(LoginActivity.this,
                                        MainActivity.class));
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //endregion
    //region validateEmail
    private boolean validateEmail() {
        String email = mLoginActivityEditEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            mLoginActivityLayoutEmail.setError(getString(R.string.activity_login_email_error));
            requestFocus(mLoginActivityEditEmail);
            return false;
        } else {
            mLoginActivityLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }
    //endregion
    //region validatePassword
    private boolean validatePassword() {
        if (mLoginActivityEditPassword.getText().toString().trim().isEmpty()) {
            mLoginActivityLayoutPassword.setError(getString(R.string.activity_login_email_password_error));
            requestFocus(mLoginActivityEditPassword);
            return false;
        } else {
            mLoginActivityLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }
    //endregion
    //region isValidEmail
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    //endregion
    //region requestFocus
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    //endregion

    //region Class MyTextWatcher
    private class MyTextWatcher implements TextWatcher {
        //region Fields
        private View mView;
        //endregion
        //region Constructors
        private MyTextWatcher(View view) {
            this.mView = view;
        }
        //endregion
        //region beforeTextChanged
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        //endregion
        //region onTextChanged
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        //endregion
        //region afterTextChanged
        public void afterTextChanged(Editable editable) {
            switch (mView.getId()) {
                case R.id.text_input_edit_login_activity_email:
                    validateEmail();
                    break;
                case R.id.text_input_edit_login_activity_password:
                    validatePassword();
                    break;
            }
        }
        //endregion
    }
    //endregion
}