package com.ryanwahle.projecthours;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {
    EditText editTextUserName = null;
    EditText editTextPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserName = (EditText) findViewById(R.id.login_editText_userName);
        editTextPassword = (EditText) findViewById(R.id.login_editText_password);
    }

    public void onClick_login_button_signIn(View view) {
        Log.v("login_button_signIn", "clicked");

        if ((editTextUserName.getText().toString().isEmpty() || editTextUserName.getText().toString().trim().equals("") || editTextPassword.getText().toString().isEmpty() || editTextPassword.getText().toString().trim().equals(""))) {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(LoginActivity.this);
            errorDialog.setTitle("Error Logging In");
            errorDialog.setMessage("You must enter a username and password!");
            errorDialog.setNegativeButton("OK", null);
            errorDialog.setCancelable(false);
            errorDialog.show();

            return;
        }

        if (InternetStatus.isInternetAvailable(this)) {
            ParseUser.logInInBackground(editTextUserName.getText().toString(), editTextPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (parseUser == null) {
                        Log.v("Login Failed!", e.toString());
                        AlertDialog.Builder errorDialog = new AlertDialog.Builder(LoginActivity.this);
                        errorDialog.setTitle("Error Logging In");
                        errorDialog.setMessage("Invalid username and/or password!");
                        errorDialog.setNegativeButton("OK", null);
                        errorDialog.setCancelable(false);
                        errorDialog.show();
                    } else {
                        Log.v("Login Succeeded!", "YES!");
                        proceedAfterLogin();
                    }
                }
            });
        } else {
            InternetStatus.showNoInternetAlert(LoginActivity.this);
        }
    }

    public void onClick_login_button_register(View view) {
        Intent registerAccountIntent = new Intent(this, RegisterAccountActivity.class);
        startActivityForResult(registerAccountIntent, 0);
    }

    public void proceedAfterLogin() {
        Intent projectHoursIntent = new Intent(getApplicationContext(), ProjectHoursActivity.class);
        startActivity(projectHoursIntent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Log.v("LoginActivity", "register account reporting user canceled");
        } else if (resultCode == RESULT_OK) {
            Log.v("LoginActivity", "register account reporting user created login account");
            proceedAfterLogin();
        }
    }
}
