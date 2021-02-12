package com.example.MyApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.MyApp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {



    // private TextView tvShake;
    private CardView btn;
    private CardView btn2;
    private CardView btn3, btn4;
    private ImageButton mbtn2;
    int PERMISSION_ID = 44;
    public double lat, lon;
    public static double latt, lonn;
    private FusedLocationProviderClient client;
    private static final String TAG = "MyActivity";
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private static final String USERS = "User";
    String email;
    public static String eno1, eno2;
    private static final int ERROR_DIALOG_REQUEST = 9001;
////////////////////////////


    /////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String emaill = user.getEmail();
            Log.i(TAG, "" + emaill);
            email = emaill;

        }

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(USERS);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("email").getValue().equals(email)) {
                        String enno1 = ds.child("eno1").getValue(String.class);
                        String enno2 = ds.child("eno2").getValue(String.class);
                        eno1 = enno1;
                        eno2 = enno2;
                        Log.i(TAG, "" + eno1);
                        Log.i(TAG, "" + eno2);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestPermission();
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
           return;
        }

        //getting the current location
        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                Log.i(TAG, "" + lat);
                Log.i(TAG, "" + lon);
                latt = lat;
                lonn = lon;
            }
        });

        //Shake Service
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                startActivity(intent);
            }
        });

        //Maps
        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        //SOS button
        mbtn2 = findViewById(R.id.mbtn2);
        mbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SmsManager sm = SmsManager.getDefault();
                String msg = "Location:"+"https://www.google.com/maps/search/?api=1&query="+MainActivity.latt+","+MainActivity.lonn;
                sm.sendTextMessage(MainActivity.eno1, null, msg, null, null);
                sm.sendTextMessage(MainActivity.eno2, null, msg, null, null);
            }
        });

        //Profile Activity
        btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //LogOut
        btn4 = findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, Login_Form.class);
                startActivity(intent);
            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
    }

}