package com.ryanwahle.projecthours;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProjectHoursActivity extends Activity {
    ListView projectHoursListView = null;
    ArrayList<HashMap<String, String>> projectHoursArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "RR1lRRpSMCJU7r4M0WPkpKJ8q9divyVGvc9JquK3", "WLyE29A8QpJqXPIBEYeXz9y9qZ7fUmTI3j8pPbK6");

        setContentView(R.layout.activity_project_hours);

        projectHoursListView = (ListView) findViewById(R.id.projectHours_listView);
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
        projectHoursArrayList = new ArrayList<HashMap<String, String>>();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("ProjectHours");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> projectHoursList, ParseException e) {
                if (e == null) {
                    for (ParseObject projectHoursParseObject : projectHoursList) {
                        //Log.v("Found Object", projectHoursParseObject.toString());

                        //Log.v("Project Name:", projectHoursParseObject.getString("projectName"));
                        //Log.v("Hours Worked:", "" + projectHoursParseObject.getInt("hoursWorked"));
                        //Log.v("Project Completed:", "" + projectHoursParseObject.getBoolean("projectComplete"));

                        String entryProjectName = projectHoursParseObject.getString("projectName");
                        Integer entryHoursWorked = projectHoursParseObject.getInt("hoursWorked");
                        Boolean entryProjectComplete = projectHoursParseObject.getBoolean("projectComplete");

                        HashMap<String, String> projectHoursHashMap = new HashMap<String, String>();
                        projectHoursHashMap.put("projectName", entryProjectName);
                        projectHoursHashMap.put("hoursWorked", entryHoursWorked.toString());
                        projectHoursHashMap.put("projectComplete", entryProjectComplete.toString());

                        Log.v("boolean", entryProjectComplete.toString());

                        projectHoursArrayList.add(projectHoursHashMap);
                    }

                    String[] mapFrom = { "projectName", "hoursWorked", "projectComplete" };
                    int[] mapTo = { R.id.projectHoursListViewRow_textView_projectName, R.id.projectHoursListViewRow_textView_hoursWorked, R.id.projectHoursListViewRow_textView_projectCompleted };

                    SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), projectHoursArrayList, R.layout.layout_project_hours_listview_row, mapFrom, mapTo);
                    projectHoursListView.setAdapter(adapter);
                } else {
                    Log.d("getProjectHourDataFromParse()", "Error: " + e.getMessage());
                }
            }
        });
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
        } else if (id == R.id.projectHours_menu_addProjectHours) {
            Intent projectHoursAddIntent = new Intent(this, ProjectHoursAddActivity.class);
            startActivityForResult(projectHoursAddIntent, 0);
        }

        return super.onOptionsItemSelected(item);
    }
}
