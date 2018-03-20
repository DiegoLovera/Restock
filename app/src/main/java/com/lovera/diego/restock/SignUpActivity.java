package com.lovera.diego.restock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    //TODO: Falta capturar todos los datos que sean necesarios para crear una cuenta y todas las validaciones necesarias

    private FirebaseAuth mAuth;
    private EditText signUpActivityEditEmail, signUpActivityEditPassword;
    private TextInputLayout signUpActivityLayoutEmail, signUpActivityLayoutPassword;
    private Button signUpActivitySignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        signUpActivityEditEmail = findViewById(R.id.signUpActivityTextInputEditEmail);
        signUpActivityEditPassword = findViewById(R.id.signUpActivityTextInputEditPassword);
        signUpActivityLayoutEmail = findViewById(R.id.signUpActivityTextInputLayoutEmail);
        signUpActivityLayoutPassword = findViewById(R.id.signUpActivityTextInputLayoutPassword);

        signUpActivitySignUpButton = findViewById(R.id.signUpActivityLoginButton);

        signUpActivitySignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(signUpActivityEditEmail.getText().toString(),
                        signUpActivityEditPassword.getText().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            LoginActivity.ACTUAL_USER = mAuth.getCurrentUser();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}
