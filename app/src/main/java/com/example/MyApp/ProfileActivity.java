package com.example.MyApp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class ProfileActivity  extends AppCompatActivity {

    TextView txt_firstname,txt_lastname,txt_email,txt_eno1,txt_eno2;
    String email;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txt_firstname = (TextView) findViewById(R.id.firstname);
        txt_lastname = findViewById(R.id.lastname);
        txt_email = findViewById(R.id.email);
        txt_eno1 = findViewById(R.id.en1);
        txt_eno2 = findViewById(R.id.en2);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), Login_Form.class));
        }

        final FirebaseUser muser = firebaseAuth.getCurrentUser();

        if (muser != null) {

            String emaill = muser.getEmail();
            email =emaill;
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("email").getValue().equals(email)) {
                        txt_firstname.setText("First Name-  "+ds.child("firstname").getValue(String.class));
                        txt_lastname.setText( "Last Name-   "+ds.child("lastname").getValue(String.class));
                        txt_email.setText("Email-   "+muser.getEmail());
                        txt_eno1.setText("Emergency Contact 1-  "+ds.child("eno1").getValue(String.class));
                        txt_eno2.setText("Emergency Contact 2-  "+ds.child("eno2").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}