package com.eason.showearthquake;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.eason.earthquakedatafetcher.EarthquakeDataFetcher;
import com.eason.earthquakedatafetcher.model.Earthquake;

import java.util.List;

/**
 * Entry Activity
 */
public class MainActivity extends AppCompatActivity implements EarthquakeDataFetcher.EarthquakeDataFetcherListener {
    private RecyclerView mEarthquakeRecyclerView;
    private TextView mErrorMsgTextView;
    private EarthquakeAdapter mEarthquakeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //RecyclerView for showing earthquake data as a list
        mEarthquakeRecyclerView = (RecyclerView)findViewById(R.id.earthquake_list);
        //TextView for informing user that network has problem
        mErrorMsgTextView = (TextView)findViewById(R.id.error_msg);

        //configure recycler view
        mEarthquakeRecyclerView
                .setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        mEarthquakeAdapter = new EarthquakeAdapter();
        mEarthquakeRecyclerView.setAdapter(mEarthquakeAdapter);

        updateEarthquakeData();
    }

    private void updateEarthquakeData() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            showEarthquakes();
        } else {
            // display error
            showNetworkProblem();
        }
    }

    private void showEarthquakes() {
        mEarthquakeRecyclerView.setVisibility(View.VISIBLE);
        mErrorMsgTextView.setVisibility(View.GONE);
        new EarthquakeDataFetcher(this).startFetch();
    }

    private void showNetworkProblem() {
        mEarthquakeRecyclerView.setVisibility(View.GONE);
        mErrorMsgTextView.setText(R.string.network_problem);
        mErrorMsgTextView.setVisibility(View.VISIBLE);
    }

    private void showUnknownError() {
        mEarthquakeRecyclerView.setVisibility(View.GONE);
        mErrorMsgTextView.setText(R.string.unkonwn_error);
        mErrorMsgTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFetchFail() {
        Log.d(MainActivity.class.getSimpleName(), "onFetchFail");
        showUnknownError();
    }

    @Override
    public void onFetchSuccess(int totalEarthquakes, List<Earthquake> earthquakes) {
        Log.d(MainActivity.class.getSimpleName(), "onFetchSuccess:" + totalEarthquakes);
        for(Earthquake quake : earthquakes) {
            mEarthquakeAdapter.addEarthquake(quake);
        }
    }
}
