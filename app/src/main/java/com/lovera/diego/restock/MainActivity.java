package com.lovera.diego.restock;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (LoginActivity.ACTUAL_USER != null) {
            // Name, email address, and profile photo Url
            String name = LoginActivity.ACTUAL_USER.getDisplayName();
            String email = LoginActivity.ACTUAL_USER.getEmail();
            Uri photoUrl = LoginActivity.ACTUAL_USER.getPhotoUrl();

            TextView textView = findViewById(R.id.textViewTest);
            String text = (name + " " + email + " " + photoUrl);
            textView.setText(text);

            // Check if user's email is verified
            //boolean emailVerified = LoginActivity.ACTUAL_USER.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = LoginActivity.ACTUAL_USER.getUid();
        }
    }
}
