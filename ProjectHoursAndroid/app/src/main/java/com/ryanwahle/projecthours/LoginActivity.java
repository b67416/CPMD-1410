package com.ryanwahle.projecthours;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parse.Parse;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "RR1lRRpSMCJU7r4M0WPkpKJ8q9divyVGvc9JquK3", "WLyE29A8QpJqXPIBEYeXz9y9qZ7fUmTI3j8pPbK6");

        setContentView(R.layout.activity_login);
    }

    public void onClick_login_button_signIn(View view) {
        Log.v("login_button_signIn", "clicked");
    }

    public void onClick_login_button_register(View view) {
        Log.v("login_button_register", "clicked");

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
