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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lovera.diego.restock.models.User;

public class SignUpActivity extends AppCompatActivity {

    //TODO: Falta capturar todos los datos que sean necesarios para crear una cuenta y todas las validaciones necesarias

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseUser mCurrentUser;
    private EditText mSignUpActivityEditEmail, mSignUpActivityEditPassword;
    private CallbackManager mCallbackManager;
    private ProgressBar mProgressBar;
    private LinearLayout mLinearLayout;

    //Activity lifecycle
    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        //region ProgressBar setup
        mLinearLayout = findViewById(R.id.layout_signup_progress);
        mLinearLayout.setVisibility(View.INVISIBLE);
        mProgressBar = findViewById(R.id.progressBar_signup);
        mProgressBar.setVisibility(View.INVISIBLE);
        mProgressBar.setMax(100);
        mProgressBar.setIndeterminate(true);
        //endregion

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
                    mProgressBar.setVisibility(View.VISIBLE);
                    mLinearLayout.setVisibility(View.VISIBLE);
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
    }
    //endregion
    //Facebook SignUp
    //region handleFacebookAccessToken
    private void handleFacebookAccessToken(AccessToken token) {
        mProgressBar.setVisibility(View.VISIBLE);
        mLinearLayout.setVisibility(View.VISIBLE);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mCurrentUser = mAuth.getCurrentUser();
                            final String userId = mCurrentUser.getUid();

                            Query query = mRef.child("User")
                                    .orderByChild(mCurrentUser.getUid());

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                    } else {
                                        if (mCurrentUser.getEmail() != null) {

                                            User cUser = new User(mCurrentUser.getEmail(), "", "", "", mCurrentUser.getDisplayName(), "", mCurrentUser.getPhotoUrl().toString(),  FirebaseInstanceId.getInstance().getToken());
                                            mRef = mDatabase.getReference().child("User").child(userId);
                                            mRef.setValue(cUser);
                                        }
                                        else {

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            RestockApp.ACTUAL_USER = mAuth.getCurrentUser();
                            LoginManager.getInstance().logOut();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(SignUpActivity.this, "Email already in use or check your connection.",
                                    Toast.LENGTH_SHORT).show();
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
                            User user = new User(mSignUpActivityEditEmail.getText().toString(), "", "", "", "", "", "",  FirebaseInstanceId.getInstance().getToken());
                            //Se especifica que se va a insertar en el nodo "User" bajo el userId del usuario actual
                            mRef = mDatabase.getReference().child("User").child(userId);
                            //Se inserta el objeto user de tipo User
                            mRef.setValue(user);
                            //------------------------------------------------------------------------------------//
                            RestockApp.ACTUAL_USER = mAuth.getCurrentUser();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mLinearLayout.setVisibility(View.INVISIBLE);
                            //updateUI(user);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Email already in use or check your connection.",
                                    Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mLinearLayout.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    //endregion
}
