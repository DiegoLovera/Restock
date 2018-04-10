package com.lovera.diego.restock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lovera.diego.restock.models.User;

public class SignUpActivity extends AppCompatActivity {

    //TODO: Falta capturar todos los datos que sean necesarios para crear una cuenta y todas las validaciones necesarias

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseUser mCurrentUser;
    private EditText mSignUpActivityEditEmail, mSignUpActivityEditPassword;
    private CallbackManager mCallbackManager;

    //Activity lifecycle
    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        mSignUpActivityEditEmail = findViewById(R.id.signUpActivityTextInputEditEmail);
        mSignUpActivityEditPassword = findViewById(R.id.signUpActivityTextInputEditPassword);
        TextInputLayout signUpActivityLayoutEmail = findViewById(R.id.signUpActivityTextInputLayoutEmail);
        TextInputLayout signUpActivityLayoutPassword = findViewById(R.id.signUpActivityTextInputLayoutPassword);

        Button signUpActivitySignUpButton = findViewById(R.id.signUpActivityLoginButton);

        LoginButton signUpFacebookButton = findViewById(R.id.sign_up_button_facebook);
        mCallbackManager = CallbackManager.Factory.create();
        signUpFacebookButton.setReadPermissions("email", "public_profile");
        signUpFacebookButton.registerCallback(mCallbackManager,new FacebookCallback<LoginResult>() {
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

        signUpActivitySignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mSignUpActivityEditEmail.getText().toString();
                String password = mSignUpActivityEditPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this,
                            "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this,
                            "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 8) {
                    Toast.makeText(SignUpActivity.this,
                            "Password too short, enter minimum 8 characters!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    createAccount(email, password);
                }

            }
        });
    }
    //endregion
    //region onStart
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        //updateUI(mCurrentUser);
    }
    //endregion
    //Facebook SignUp
    //region handleFacebookAccessToken
    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);
        //TODO: Al iniciar sesión con facebook debemos validar que ya haya ingresado toda la información necesaria para conituar en casa de una compra
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //-------------------------------------------------------------------------------//
                            // Insertar el correo del usuario en la base de datos
                            // Se obtiene el codigo de usario actual que se acaba de crear y se asigna a mCurrentUser
                            mCurrentUser = mAuth.getCurrentUser();
                            //Se almacena el Uid del usuario en la variable userId
                            String userId = mCurrentUser.getUid();
                            User user = new User(mSignUpActivityEditEmail.getText().toString());
                            //Se especifica que se va a insertar en el nodo "User" bajo el userId del usuario actual
                            mRef = mDatabase.getReference().child("User").child(userId);
                            //Se inserta el objeto user de tipo User
                            mRef.setValue(user);
                            //------------------------------------------------------------------------------------//
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            RestockApp.ACTUAL_USER = mAuth.getCurrentUser();
                            LoginManager.getInstance().logOut();
                            Intent i = new Intent(SignUpActivity.this,
                                    MainActivity.class);
                            startActivity(i);
                            //updateUI(user);
                        } else {
                                Toast.makeText(SignUpActivity.this, "Email already in use or check your connection.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
    //endregion
    //region onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    //endregion
    //SignUp
    //region createAccount
    public void createAccount(String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //-------------------------------------------------------------------------------//
                            // Insertar el correo del usuario en la base de datos
                            // Se obtiene el codigo de usuario actual que se acaba de crear y se asigna a mCurrentUser
                            mCurrentUser = mAuth.getCurrentUser();
                            //Se almacena el Uid del usuario en la variable userId
                            String userId = mCurrentUser.getUid();
                            User user = new User(mSignUpActivityEditEmail.getText().toString());
                            //Se especifica que se va a insertar en el nodo "User" bajo el userId del usuario actual
                            mRef = mDatabase.getReference().child("User").child(userId);
                            //Se inserta el objeto user de tipo User
                            mRef.setValue(user);
                            //------------------------------------------------------------------------------------//
                            RestockApp.ACTUAL_USER = mAuth.getCurrentUser();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                            //updateUI(user);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Email already in use or check your connection.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //endregion
}
