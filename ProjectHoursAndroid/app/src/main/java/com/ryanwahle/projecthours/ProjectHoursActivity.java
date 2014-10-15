package com.ryanwahle.projecthours;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
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

    private Handler automaticDataRefreshHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "RR1lRRpSMCJU7r4M0WPkpKJ8q9divyVGvc9JquK3", "WLyE29A8QpJqXPIBEYeXz9y9qZ7fUmTI3j8pPbK6");

        setContentView(R.layout.activity_project_hours);

        projectHoursListView = (ListView) findViewById(R.id.projectHours_listView);

        automaticDataRefreshHandler.postDelayed(automaticDataRefreshRunner, 20000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIfUserLoggedIn();
    }

    private Runnable automaticDataRefreshRunner = new Runnable() {
        @Override
        public void run() {
            getProjectHourDataFromParse();

            automaticDataRefreshHandler.postDelayed(this, 20000);
        }
    };

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
        if (InternetStatus.isInternetAvailable(ProjectHoursActivity.this)) {
            projectHoursArrayList = new ArrayList<HashMap<String, String>>();

            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("ProjectHours");
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> projectHoursList, ParseException e) {
                    if (e == null) {
                        for (ParseObject projectHoursParseObject : projectHoursList) {
                            String entryObjectID = projectHoursParseObject.getObjectId();
                            String entryProjectName = projectHoursParseObject.getString("projectName");
                            Integer entryHoursWorked = projectHoursParseObject.getInt("hoursWorked");
                            Boolean entryProjectComplete = projectHoursParseObject.getBoolean("projectComplete");

                            HashMap<String, String> projectHoursHashMap = new HashMap<String, String>();
                            projectHoursHashMap.put("objectId", entryObjectID);
                            projectHoursHashMap.put("projectName", entryProjectName);
                            projectHoursHashMap.put("hoursWorked", entryHoursWorked.toString() + " hours worked");

                            if (entryProjectComplete) {
                                projectHoursHashMap.put("projectComplete", "Completed");
                            } else {
                                projectHoursHashMap.put("projectComplete", "Not Completed");
                            }

                            projectHoursArrayList.add(projectHoursHashMap);
                        }

                        String[] mapFrom = {"projectName", "hoursWorked", "projectComplete"};
                        int[] mapTo = {R.id.projectHoursListViewRow_textView_projectName, R.id.projectHoursListViewRow_textView_hoursWorked, R.id.projectHoursListViewRow_textView_projectCompleted};

                        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), projectHoursArrayList, R.layout.layout_project_hours_listview_row, mapFrom, mapTo);
                        projectHoursListView.setAdapter(adapter);

                        projectHoursListView.setOnItemClickListener(updateListItem());
                        projectHoursListView.setOnItemLongClickListener(deleteListItem());

                    } else {
                        Log.d("getProjectHourDataFromParse()", "Error: " + e.getMessage());
                    }
                }
            });
        }
    }

    protected AdapterView.OnItemClickListener updateListItem() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (InternetStatus.isInternetAvailable(ProjectHoursActivity.this)) {
                    HashMap<String, String> projectHoursParseObjectToUpdate = projectHoursArrayList.get(position);

                    Intent projectHoursUpdateIntent = new Intent(getApplicationContext(), ProjectHoursAddActivity.class);

                    projectHoursUpdateIntent.putExtra("objectId", projectHoursParseObjectToUpdate.get("objectId"));

                    startActivityForResult(projectHoursUpdateIntent, 0);
                } else {
                    InternetStatus.showNoInternetAlert(ProjectHoursActivity.this);
                }
            }
        };
    }

    protected AdapterView.OnItemLongClickListener deleteListItem() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (InternetStatus.isInternetAvailable(ProjectHoursActivity.this)) {
                    final HashMap<String, String> projectHoursParseObject = projectHoursArrayList.get(position);

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProjectHoursActivity.this);
                    alertDialog.setTitle("Delete Project Hours");
                    alertDialog.setMessage("Are you sure you want to delete this project hour entry?");
                    alertDialog.setNegativeButton("No", null);

                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //projectHoursArrayList.remove(position);

                            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("ProjectHours");
                            parseQuery.getInBackground(projectHoursParseObject.get("objectId"), new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if (e == null) {
                                        parseObject.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                getProjectHourDataFromParse();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });


                    alertDialog.show();
                } else {
                    InternetStatus.showNoInternetAlert(ProjectHoursActivity.this);
                }

                return true;
            }
        };
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
            if (InternetStatus.isInternetAvailable(ProjectHoursActivity.this)) {
                Intent projectHoursAddIntent = new Intent(this, ProjectHoursAddActivity.class);
                startActivityForResult(projectHoursAddIntent, 0);
            } else {
                InternetStatus.showNoInternetAlert(ProjectHoursActivity.this);
            }
        } else if (id == R.id.projectHours_menu_refreshData) {
            if (InternetStatus.isInternetAvailable(ProjectHoursActivity.this)) {
                getProjectHourDataFromParse();
            } else {
                InternetStatus.showNoInternetAlert(ProjectHoursActivity.this);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
