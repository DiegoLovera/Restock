package com.lovera.diego.restock;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lovera.diego.restock.models.User;

public class ProfileInformationActivity extends AppCompatActivity {

    //region Fields
    private EditText mProfileInfoActivityEditName, mProfileInfoActivityEditPhoneNumber, mProfileInfoActivityEditDateOfBirth;
    private TextInputLayout mProfileInfoActivityLayoutName, mProfileInfoActivityLayoutPhoneNumber, mProfileInfoActivityLayoutDateOfBirth;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRef;
    private FirebaseDatabase mDatabase;
    private CallbackManager mCallbackManager;
    private int mLoginSelected = 0;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        //region UI elements assignation
        mProfileInfoActivityEditName = findViewById(R.id.text_input_edit_layout_profile_info_name);
        mProfileInfoActivityEditPhoneNumber = findViewById(R.id.text_input_edit_layout_profile_info_phoneNumber);
        mProfileInfoActivityEditDateOfBirth = findViewById(R.id.text_input_edit_layout_profile_info_dateOfBirth);
        mProfileInfoActivityLayoutName = findViewById(R.id.text_input_layout_profile_info_name);
        mProfileInfoActivityLayoutPhoneNumber = findViewById(R.id.text_input_layout_profile_info_phoneNumber);
        mProfileInfoActivityLayoutDateOfBirth = findViewById(R.id.text_input_layout_profile_info_dateOfBirth);
        Button ProfileInfoActivityButtonUpdate = findViewById(R.id.button_activity_profile_information_update);
        //endregion

        ProfileInfoActivityButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mProfileInfoActivityEditName.getText().toString();
                String phoneNumber = mProfileInfoActivityEditPhoneNumber.getText().toString();
                String dateOfBirth = mProfileInfoActivityEditDateOfBirth.getText().toString();

                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(phoneNumber) && TextUtils.isEmpty(dateOfBirth)) {
                    Toast.makeText(ProfileInformationActivity.this,
                            "Empty fields",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    //-------------------------------------------------------------------------------//
                    mCurrentUser = mAuth.getCurrentUser();
                    String userId = mCurrentUser.getUid();
                    User user = new User("", dateOfBirth, "", "", name, phoneNumber, "");
                    mRef = mDatabase.getReference().child("User").child(userId);
                    mRef.setValue(user);
                    //------------------------------------------------------------------------------------//
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
