package com.ryanwahle.projecthours;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class ProjectHoursAddActivity extends Activity {
    EditText editTextProjectName = null;
    NumberPicker numberPickerHoursWorked = null;
    CheckBox checkBoxProjectComplete = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_hours_add);

        editTextProjectName = (EditText) findViewById(R.id.projectHoursAdd_editText_projectName);
        numberPickerHoursWorked = (NumberPicker) findViewById(R.id.projectHoursAdd_numberPicker_hoursWorked);
        checkBoxProjectComplete = (CheckBox) findViewById(R.id.projectHoursAdd_checkBox_projectComplete);

        numberPickerHoursWorked.setMinValue(1);
        numberPickerHoursWorked.setMaxValue(24);
    }

    public void onClick_projectHoursAdd_button_save(View view) {
        ParseObject projectHoursParseObject = new ParseObject("ProjectHours");

        projectHoursParseObject.put("projectName", editTextProjectName.getText().toString());
        projectHoursParseObject.put("hoursWorked", numberPickerHoursWorked.getValue());
        projectHoursParseObject.put("projectComplete", checkBoxProjectComplete.isChecked());
        projectHoursParseObject.setACL(new ParseACL(ParseUser.getCurrentUser()));

        projectHoursParseObject.saveInBackground();

        finish();
    }
}
