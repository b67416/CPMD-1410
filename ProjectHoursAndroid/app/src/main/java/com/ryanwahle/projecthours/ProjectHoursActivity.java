package com.ryanwahle.projecthours;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class ProjectHoursActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "RR1lRRpSMCJU7r4M0WPkpKJ8q9divyVGvc9JquK3", "WLyE29A8QpJqXPIBEYeXz9y9qZ7fUmTI3j8pPbK6");

        setContentView(R.layout.activity_project_hours);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIfUserLoggedIn();
    }

    public void checkIfUserLoggedIn() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {
            Log.v("current_user", currentUser.getUsername());
            getProjectHourDataFromParse();
        }
    }

    public void getProjectHourDataFromParse() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.project_hours, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.projectHours_menu_logOut) {
            ParseUser.logOut();
            checkIfUserLoggedIn();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
