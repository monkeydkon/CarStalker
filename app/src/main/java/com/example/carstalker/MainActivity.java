package com.example.carstalker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
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



public class MainActivity extends AppCompatActivity {

    Location location;
    private int speed;


    TextView welcomeTextView, welcomeTextView2;
    ImageView arrowImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar.setNavigationIcon(R.drawable.back);
        // toolbar.setLogo(R.drawable.ic_launcher);
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
        EditText usernameEditText = dialog.findViewById(R.id.usernameEditText);
        EditText passwordEditText = dialog.findViewById(R.id.passwordEditText);
        Button loginButton = dialog.findViewById(R.id.loginButton);
        Button signupButton = dialog.findViewById(R.id.signupButton);
        Button cancelButton = dialog.findViewById(R.id.backButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
                // TODO: 24/2/2019
                        
            }
        });
        
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final Dialog signupDialog = new Dialog(MainActivity.this);
                signupDialog.setContentView(R.layout.user_sign_up);
                signupDialog.setTitle("sign up");
                signupDialog.setCancelable(false);
                final EditText signupUsernameEditText = signupDialog.findViewById(R.id.signupUsernameEditText);
                final EditText signupFirstnameEditText = signupDialog.findViewById(R.id.signupFirstnameEditText);
                final EditText signupLastnameEditText = signupDialog.findViewById(R.id.signupLastnameEditText);
                final EditText signupEmailEditText = signupDialog.findViewById(R.id.signupEmailEditText);
                final EditText signupPasswordEditText = signupDialog.findViewById(R.id.signupPasswordEditText);
                final EditText signupConfrimPasswordEditText = signupDialog.findViewById(R.id.signupConfirmPasswordEditText);
                final Button signupRegisterButton = signupDialog.findViewById(R.id.signupRegisterButton);
                final ImageView signupBackImageView = signupDialog.findViewById(R.id.signupBackImageView);

                signupBackImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signupDialog.dismiss();
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
                    }
                });

                signupRegisterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (signupUsernameEditText.getText().toString().equals("") || signupFirstnameEditText.getText().toString().equals("") ||
                                signupLastnameEditText.getText().toString().equals("") || signupEmailEditText.getText().toString().equals("") ||
                                signupPasswordEditText.getText().toString().equals("") || signupConfrimPasswordEditText.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),"You left at least one field empty",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                signupDialog.show();
                Window signupDialogWindow = signupDialog.getWindow();
                signupDialogWindow.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);

            }
        });


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
}