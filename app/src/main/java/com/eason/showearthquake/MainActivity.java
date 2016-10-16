package com.eason.showearthquake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eason.earthquakedatafetcher.EarthquakeDataFetcher;
import com.eason.earthquakedatafetcher.model.Earthquake;

import java.util.List;

/**
 * Entry Activity
 */
public class MainActivity extends AppCompatActivity implements EarthquakeDataFetcher.EarthquakeDataFetcherListener {
    private static final int FILTER_REQ = 10;
    private RecyclerView mEarthquakeRecyclerView;
    private TextView mErrorMsgTextView;
    private EarthquakeAdapter mEarthquakeAdapter;
    private ProgressBar mLoadingProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //for loading more
    private boolean isLoadingMore;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    //Filter for RESTful API
    private EarthquakeDataFetcher.FilterOption mFilter;
    //total earthquake count of last request
    private int mTotalEarthquakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //RecyclerView for showing earthquake data as a list
        mEarthquakeRecyclerView = (RecyclerView)findViewById(R.id.earthquake_list);
        //TextView for informing user that network has problem
        mErrorMsgTextView = (TextView)findViewById(R.id.error_msg);
        //show progress bar when loading data
        mLoadingProgressBar = (ProgressBar)findViewById(R.id.loading_progress_bar);
        //Show refresh progress for update data
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateEarthquakeData();
            }
        });
        //configure recycler view
        mEarthquakeRecyclerView
                .setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        mEarthquakeAdapter = new EarthquakeAdapter(this);
        mEarthquakeRecyclerView.setAdapter(mEarthquakeAdapter);
        //scroll to last item to load more
        mEarthquakeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lm = (LinearLayoutManager) mEarthquakeRecyclerView.getLayoutManager();
                totalItemCount = lm.getItemCount();
                lastVisibleItem = lm.findLastVisibleItemPosition();

                if (!isLoadingMore && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    //Maybe there are no more earthquake data we can load
                    if (mEarthquakeAdapter.getItemCount() < Math.min(EarthquakeDataFetcher.MAX_LIMIT, mTotalEarthquakes)) {
                        //load more data here
                        isLoadingMore = true;
                        mFilter.setLimit(mFilter.getLimit() + EarthquakeDataFetcher.DEFAULT_LIMIT);
                        updateEarthquakeData();
                    }
                }
            }
        });

        //init a filter
        mFilter = new EarthquakeDataFetcher.FilterOption();
        mFilter.setLimit(EarthquakeDataFetcher.DEFAULT_LIMIT);
        updateEarthquakeData();
    }

    private void updateEarthquakeData() {
        showLoading();
        //check network state
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

    //fetch earthquake data from source server
    private void showEarthquakes() {
        mEarthquakeRecyclerView.setVisibility(View.VISIBLE);
        mErrorMsgTextView.setVisibility(View.GONE);
        EarthquakeDataFetcher fetcher = new EarthquakeDataFetcher(this);
        fetcher.setFilterOption(mFilter);
        fetcher.startFetch();
    }

    private void showNetworkProblem() {
        Log.d(MainActivity.class.getSimpleName(),
                "Network Problem!!");
        mEarthquakeAdapter.clear();
        mEarthquakeRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMsgTextView.setText(R.string.network_problem);
        mErrorMsgTextView.setVisibility(View.VISIBLE);
        mLoadingProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showUnknownError() {
        Log.w(MainActivity.class.getSimpleName(),
                "Unknown Error!!");
        mEarthquakeAdapter.clear();
        mEarthquakeRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMsgTextView.setText(R.string.unkonwn_error);
        mErrorMsgTextView.setVisibility(View.VISIBLE);
        mLoadingProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showLoading() {
        //We have two progress bar. Do not show two same time
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mLoadingProgressBar.setVisibility(View.VISIBLE);
            mEarthquakeRecyclerView.setVisibility(isLoadingMore ? View.VISIBLE : View.INVISIBLE);
            mErrorMsgTextView.setVisibility(View.GONE);
        }
    }

    private void hideLoading() {
        mLoadingProgressBar.setVisibility(View.GONE);
        mEarthquakeRecyclerView.setVisibility(View.VISIBLE);
        mErrorMsgTextView.setVisibility(View.GONE);
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
        if (id == R.id.action_filter) {
            Intent filter = new Intent(getApplicationContext(),FilterActivity.class);
            startActivityForResult(filter, FILTER_REQ);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(MainActivity.class.getSimpleName(),
                "[onActivityResult] result:" + resultCode);
        if (requestCode == FILTER_REQ && resultCode == Activity.RESULT_OK) {
            //clear old data
            mEarthquakeAdapter.clear();
            //user set filter
            if (data != null) {
                mFilter.setLimit(EarthquakeDataFetcher.DEFAULT_LIMIT);
                int year = data.getIntExtra(FilterActivity.RESULT_KEY_YEAR, -1);
                if (year > 0){
                    mFilter.setYear(year);
                    int month = data.getIntExtra(FilterActivity.RESULT_KEY_MONTH, -1);
                    if (month > 0) {
                        mFilter.setMonth(month);
                    }
                } else {
                    //do NOT keep filter setting of last time
                    mFilter.setYear(-1);
                    mFilter.setMonth(-1);
                }
                float mag = data.getFloatExtra(FilterActivity.RESULT_KEY_MIN_MAG, -1);
                if (mag > 0) {
                    mFilter.setMinMagnitude(mag);
                } else {
                    //do NOT keep filter setting of last time
                    mFilter.setMinMagnitude(-1);
                }
                updateEarthquakeData();
            }
        }
    }

    @Override
    public void onFetchFail() {
        Log.d(MainActivity.class.getSimpleName(), "onFetchFail");
        showUnknownError();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onFetchSuccess(int totalEarthquakes, List<Earthquake> earthquakes) {
        Log.d(MainActivity.class.getSimpleName(), "onFetchSuccess:" + totalEarthquakes);
        mTotalEarthquakes = totalEarthquakes;
        if (mSwipeRefreshLayout.isRefreshing()) {
            mEarthquakeAdapter.clear();
            mSwipeRefreshLayout.setRefreshing(false);
        }
        int size = earthquakes.size();
        for (int i = mEarthquakeAdapter.getItemCount(); i < size ; i++) {
            Earthquake quake = earthquakes.get(i);
            mEarthquakeAdapter.addEarthquake(quake);
        }
        mFilter.setLimit(mEarthquakeAdapter.getItemCount());
        if (isLoadingMore) isLoadingMore = false;
        hideLoading();
    }
}
