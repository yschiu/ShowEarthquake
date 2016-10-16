package com.eason.showearthquake;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FilterActivity extends AppCompatActivity{
    public static final String VALUE_NOT_SPECIFIC = "Not Specific";
    public static final String RESULT_KEY_YEAR = "year";
    public static final String RESULT_KEY_MONTH = "month";
    public static final String RESULT_KEY_MIN_MAG = "min_mag";

    private static final int SPINNER_YEAR_START = 2008;

    private Spinner mYearSpinner;
    private Spinner mMonthSpinner;
    private Spinner mMagnitudeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //spinners
        mYearSpinner = (Spinner)findViewById(R.id.spinner_year);
        mMonthSpinner = (Spinner)findViewById(R.id.spinner_month);
        mMagnitudeSpinner = (Spinner)findViewById(R.id.spinner_magnitude);
        //setting values to Year spinner
        List<String> yearsOptions = new ArrayList<>();
        yearsOptions.add(VALUE_NOT_SPECIFIC);
        Calendar now= Calendar.getInstance();
        for (int i = SPINNER_YEAR_START ; i < now.get(Calendar.YEAR); i++) {
            yearsOptions.add(String.valueOf(i));
        }
        mYearSpinner.setAdapter(new ArrayAdapter<String>(this
                , R.layout.spinner_item, R.id.value, yearsOptions));
        //setting values to Month spinner
        List<String> monthOptions = new ArrayList<>();
        monthOptions.add(VALUE_NOT_SPECIFIC);
        for (int i = 1 ; i <= 12; i++) {
            monthOptions.add(String.valueOf(i));
        }
        mMonthSpinner.setAdapter(new ArrayAdapter<String>(this
                , R.layout.spinner_item, R.id.value, monthOptions));
        //setting values to Magnitude spinner
        List<String> magOptions = new ArrayList<>();
        magOptions.add(VALUE_NOT_SPECIFIC);
        for (float i = 1.0f ; i <= 10.0f; i+= 0.5f) {
            magOptions.add(String.valueOf(i));
        }
        mMagnitudeSpinner.setAdapter(new ArrayAdapter<String>(this
                , R.layout.spinner_item, R.id.value, magOptions));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_done:
                //return value to our list activity
                Intent result = new Intent();
                String year = (String)mYearSpinner.getSelectedItem();
                String month = (String)mMonthSpinner.getSelectedItem();
                String magnitude = (String)mMagnitudeSpinner.getSelectedItem();
                if (year != VALUE_NOT_SPECIFIC) {
                    result.putExtra(RESULT_KEY_YEAR, Integer.parseInt(year));
                    if(month != VALUE_NOT_SPECIFIC) {
                        result.putExtra(RESULT_KEY_MONTH, Integer.parseInt(month));
                    }
                }
                if (magnitude != VALUE_NOT_SPECIFIC) {
                    result.putExtra(RESULT_KEY_MIN_MAG, Float.parseFloat(magnitude));
                }
                setResult(Activity.RESULT_OK, result);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
