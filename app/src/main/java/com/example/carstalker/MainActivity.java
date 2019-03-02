package com.example.carstalker;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.se.omapi.Session;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

//    Location location;
//    private int speed;


    TextView welcomeTextView, welcomeTextView2;
    ImageView arrowImageView;

    private DatabaseReference mDatabase;

    private String encryptedPassword;
    private String decryptedPassword;

    private SaveSharedPreferences saveSharedPreferences;

    public String logedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Here, thisActivity is the current activity
        // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }


        saveSharedPreferences = new SaveSharedPreferences(getApplicationContext());

        logedUser = saveSharedPreferences.getusename();

        // check if user has allready logged in
        if (!logedUser.equals("")) {
            Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
            //intent.putExtra("username", logedUser);
            startActivity(intent);
            finish();
            return;
        }
//        }else{
//          //  toolbar.setVisibility(View.VISIBLE);
//        }

        setContentView(R.layout.activity_main);


        //   FirebaseApp.initializeApp(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar.setNavigationIcon(R.drawable.back);
        // toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Car Stalker");
        toolbar.setSubtitle("Keeping you safe");

        welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView2 = findViewById(R.id.welcomeTextView2);

        arrowImageView = findViewById(R.id.arrowImageView);

        welcomeTextView.animate().alpha(1f).setDuration(1000);
        welcomeTextView2.animate().alpha(1f).setDuration(2000);
        arrowImageView.animate().alpha(1f).setDuration(3000);

//        while(location.getSpeed() < 50){
//            speed = (int)location.getSpeed();
//
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_settings:
//                // User chose the "Settings" item, show the app settings UI...
//                return true;

            case R.id.email_in_button:
                signIn();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    private void signIn() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.user_log_in);
        dialog.setTitle("Log In");
        dialog.setCancelable(false);
        final EditText usernameEditText = dialog.findViewById(R.id.usernameEditText);
        final EditText passwordEditText = dialog.findViewById(R.id.passwordEditText);
        Button loginButton = dialog.findViewById(R.id.loginButton);
        Button signupButton = dialog.findViewById(R.id.signupButton);
        Button cancelButton = dialog.findViewById(R.id.backButton);

        //login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "You left at least a field empty, try again", Toast.LENGTH_SHORT).show();
                } else {
                    mDatabase.child("users").child(usernameEditText.getText().toString().toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                encryptedPassword = dataSnapshot.child("password").getValue().toString();
                                AESCrypt aesCrypt = new AESCrypt();
                                try {
                                    decryptedPassword = aesCrypt.decrypt(encryptedPassword);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (passwordEditText.getText().toString().equals(decryptedPassword)) {
                                    Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_SHORT).show();

                                    //save to shared preferances for REMEMBER ME FUNCTIONALITY
                                    // SaveSharedPreferences saveSharedPreferences = SaveSharedPreferences.getSharedPreferances();
                                    // TODO: 25/2/2019 shared preferances keep logged user
                                    saveSharedPreferences.setusename(usernameEditText.getText().toString().toLowerCase());


                                    //succes->open new activity
                                    Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                                    // intent.putExtra("username", saveSharedPreferences.getusename());
                                    //   Toast.makeText(getApplicationContext(),usernameEditText.getText().toString().toLowerCase(), Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    //kill this activity so user cant press back button
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                                    passwordEditText.setText("");
                                }
                            } else {
                                usernameEditText.setText("");
                                Toast.makeText(getApplicationContext(), "This username does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        //signup
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close previous dialog
                dialog.dismiss();

                //make a new dialog
                final Dialog signupDialog = new Dialog(MainActivity.this);
                signupDialog.setContentView(R.layout.user_sign_up);
                signupDialog.setTitle("sign up");
                signupDialog.setCancelable(false);

                //initialize
                final EditText signupUsernameEditText = signupDialog.findViewById(R.id.signupUsernameEditText);
                final EditText signupFirstnameEditText = signupDialog.findViewById(R.id.signupFirstnameEditText);
                final EditText signupLastnameEditText = signupDialog.findViewById(R.id.signupLastnameEditText);
                final EditText signupEmailEditText = signupDialog.findViewById(R.id.signupEmailEditText);
                final EditText signupPasswordEditText = signupDialog.findViewById(R.id.signupPasswordEditText);
                final EditText signupConfrimPasswordEditText = signupDialog.findViewById(R.id.signupConfirmPasswordEditText);

                final Button signupRegisterButton = signupDialog.findViewById(R.id.signupRegisterButton);
                final ImageView signupBackImageView = signupDialog.findViewById(R.id.signupBackImageView);

                //go to signup dialog
                signupBackImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signupDialog.dismiss();
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
                    }
                });

                //sign up button
                signupRegisterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check edittexts
                        if (signupUsernameEditText.getText().toString().equals("") || signupFirstnameEditText.getText().toString().equals("") ||
                                signupLastnameEditText.getText().toString().equals("") || signupEmailEditText.getText().toString().equals("") ||
                                signupPasswordEditText.getText().toString().equals("") || signupConfrimPasswordEditText.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "You left at least one field empty", Toast.LENGTH_SHORT).show();
                        } else if (!signupPasswordEditText.getText().toString().equals(signupConfrimPasswordEditText.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Your passwords don't match. Try again.", Toast.LENGTH_SHORT).show();
                            signupConfrimPasswordEditText.setText("");
                            signupPasswordEditText.setText("");
                        } else {
                            mDatabase.child("users").child(signupUsernameEditText.getText().toString().toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Toast.makeText(getApplicationContext(), "This username is already taken. Try another one", Toast.LENGTH_SHORT).show();
                                        signupUsernameEditText.setText("");
                                    } else {
                                        mDatabase.child("users").child(signupUsernameEditText.getText().toString().toLowerCase()).child("firstname").setValue(signupFirstnameEditText.getText().toString());
                                        mDatabase.child("users").child(signupUsernameEditText.getText().toString().toLowerCase()).child("lastname").setValue(signupLastnameEditText.getText().toString());
                                        mDatabase.child("users").child(signupUsernameEditText.getText().toString().toLowerCase()).child("email").setValue(signupEmailEditText.getText().toString());

                                        //encrypt password
                                        AESCrypt aesCrypt = new AESCrypt();
                                        try {
                                            encryptedPassword = aesCrypt.encrypt(signupUsernameEditText.getText().toString().toLowerCase());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        mDatabase.child("users").child(signupUsernameEditText.getText().toString().toLowerCase()).child("password").setValue(encryptedPassword);

                                        signupDialog.dismiss();
                                        dialog.show();
                                        Window window = dialog.getWindow();
                                        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);

                                        Toast.makeText(getApplicationContext(), "sign up complete!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

                signupDialog.show();
                Window signupDialogWindow = signupDialog.getWindow();
                signupDialogWindow.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);

            }
        });


        //go back to main activity without login or signup
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // findTheFuckingLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //todo disable functionality

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }




}