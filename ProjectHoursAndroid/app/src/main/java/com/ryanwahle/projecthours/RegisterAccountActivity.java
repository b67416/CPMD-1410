package com.ryanwahle.projecthours;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterAccountActivity extends Activity {
    EditText editTextUserName = null;
    EditText editTextPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        editTextUserName = (EditText) findViewById(R.id.registerAccount_editText_userName);
        editTextPassword = (EditText) findViewById(R.id.registerAccount_editText_password);
    }

    public void onClick_registerAccount_button_createAccount(View view) {
        if (editTextPassword.getText().toString().isEmpty() || editTextUserName.getText().toString().isEmpty()) {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(RegisterAccountActivity.this);
            errorDialog.setTitle("Error Creating Account");
            errorDialog.setMessage("You must enter both a username and password!");
            errorDialog.setNegativeButton("OK", null);
            errorDialog.setCancelable(false);
            errorDialog.show();
        } else {
            ParseUser registerAccountParseUser = new ParseUser();
            registerAccountParseUser.setUsername(editTextUserName.getText().toString());
            registerAccountParseUser.setPassword(editTextPassword.getText().toString());

            registerAccountParseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        AlertDialog.Builder errorDialog = new AlertDialog.Builder(RegisterAccountActivity.this);
                        errorDialog.setTitle("Error Creating Account");
                        errorDialog.setMessage(e.toString());
                        errorDialog.setNegativeButton("OK", null);
                        errorDialog.setCancelable(false);
                        errorDialog.show();
                    }
                }
            });
        }
    }

    public void onClick_registerAccount_button_cancel(View view) {
        finish();
    }
}
