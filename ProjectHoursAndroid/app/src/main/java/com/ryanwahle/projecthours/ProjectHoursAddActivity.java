package com.ryanwahle.projecthours;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class ProjectHoursAddActivity extends Activity {
    EditText editTextProjectName = null;
    NumberPicker numberPickerHoursWorked = null;
    CheckBox checkBoxProjectComplete = null;

    String updateParseObjectId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_hours_add);

        editTextProjectName = (EditText) findViewById(R.id.projectHoursAdd_editText_projectName);
        numberPickerHoursWorked = (NumberPicker) findViewById(R.id.projectHoursAdd_numberPicker_hoursWorked);
        checkBoxProjectComplete = (CheckBox) findViewById(R.id.projectHoursAdd_checkBox_projectComplete);

        numberPickerHoursWorked.setMinValue(1);
        numberPickerHoursWorked.setMaxValue(24);

        Bundle dataPassedBundle = getIntent().getExtras();
        if (dataPassedBundle != null) {
            updateParseObjectId = dataPassedBundle.getString("objectId");

            ParseQuery<ParseObject> updateParseObject = ParseQuery.getQuery("ProjectHours");
            updateParseObject.getInBackground(updateParseObjectId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {

                        getActionBar().setTitle("Update Project Hours");

                        String entryProjectName = parseObject.getString("projectName");
                        Integer entryHoursWorked = parseObject.getInt("hoursWorked");
                        Boolean entryProjectComplete = parseObject.getBoolean("projectComplete");

                        editTextProjectName.setText(entryProjectName);
                        numberPickerHoursWorked.setValue(entryHoursWorked);
                        checkBoxProjectComplete.setChecked(entryProjectComplete);
                    } else {
                        Log.v("Error", " getting object to update!");
                    }
                }
            });

        } else {
            Log.v("ProjectHoursAddActivity", "This is a new request");
        }
    }

    public void onClick_projectHoursAdd_button_save(View view) {
        if ((editTextProjectName.getText().toString().isEmpty() || editTextProjectName.getText().toString().trim().equals(""))) {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(ProjectHoursAddActivity.this);
            errorDialog.setTitle("Error Adding Project Hours");
            errorDialog.setMessage("You must enter a name for your project!");
            errorDialog.setNegativeButton("OK", null);
            errorDialog.setCancelable(false);
            errorDialog.show();
        } else {
            if (InternetStatus.isInternetAvailable(ProjectHoursAddActivity.this)) {
                if (updateParseObjectId == null) {
                    ParseObject projectHoursParseObject = new ParseObject("ProjectHours");

                    projectHoursParseObject.put("projectName", editTextProjectName.getText().toString());
                    projectHoursParseObject.put("hoursWorked", numberPickerHoursWorked.getValue());
                    projectHoursParseObject.put("projectComplete", checkBoxProjectComplete.isChecked());
                    projectHoursParseObject.setACL(new ParseACL(ParseUser.getCurrentUser()));

                    projectHoursParseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            finish();
                        }
                    });
                } else {
                    Log.v("saving object", "now");

                    ParseQuery<ParseObject> updateParseObject = ParseQuery.getQuery("ProjectHours");

                    updateParseObject.getInBackground(updateParseObjectId, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (e == null) {
                                parseObject.put("projectName", editTextProjectName.getText().toString());
                                parseObject.put("hoursWorked", numberPickerHoursWorked.getValue());
                                parseObject.put("projectComplete", checkBoxProjectComplete.isChecked());

                                parseObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        finish();
                                    }
                                });
                            }
                        }
                    });

                }
            } else {
                InternetStatus.showNoInternetAlert(ProjectHoursAddActivity.this);
            }
        }
    }
}
