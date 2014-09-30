package com.ryanwahle.projecthours;

import android.app.Activity;
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

        ParseUser.logInInBackground(editTextUserName.getText().toString(), editTextPassword.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser == null) {
                    Log.v("Login Failed!", e.toString());
                } else {
                    Log.v("Login Succeeded!", "YES!");
                    Intent projectHoursIntent = new Intent(getApplicationContext(), ProjectHoursActivity.class);
                    startActivity(projectHoursIntent);
                    finish();
                }
            }
        });
    }

    public void onClick_login_button_register(View view) {
        Intent registerAccountIntent = new Intent(this, RegisterAccountActivity.class);
        startActivityForResult(registerAccountIntent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Log.v("LoginActivity", "register account reporting user canceled");
        } else if (resultCode == RESULT_OK) {
            Log.v("LoginActivity", "register account reporting user created login account");
        }
    }
}
