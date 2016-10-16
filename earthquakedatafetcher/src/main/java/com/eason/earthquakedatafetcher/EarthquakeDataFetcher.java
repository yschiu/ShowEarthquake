package com.eason.earthquakedatafetcher;

import android.os.AsyncTask;
import android.util.Log;

import com.eason.earthquakedatafetcher.model.Earthquake;

import java.io.IOException;
import java.util.List;

/**
 * This is used for fetching Earthquake data from RESTful data server
 */

public class EarthquakeDataFetcher {
    public static final int DEFAULT_LIMIT = 20;
    public static final int MAX_LIMIT = 900;
    //where the json data come from
    private static final String SERVER_URL = "http://www.seismi.org/api/eqs";
    //parameter name
    private static final String LIMIT = "limit";
    private static final String MIN_MAGNITUDE = "min_magnitude";
    //Used to notify fetch result
    private EarthquakeDataFetcherListener mListener;
    private FilterOption mFilterOption;

    public EarthquakeDataFetcher(EarthquakeDataFetcherListener listener) {
        this.mListener = listener;
    }

    public void setFilterOption(FilterOption filter) {
        mFilterOption = filter;
    }

    public void startFetch() {
        String url = SERVER_URL;
        //Url must add filter option
        if (mFilterOption != null) {
            int year = mFilterOption.getYear() > 0 ? mFilterOption.getYear() : -1;
            int month = (mFilterOption.getMonth() > 1 && mFilterOption.getMonth() < 12) ? mFilterOption.getMonth() : -1;
            int limit = mFilterOption.getLimit() > 0 ? mFilterOption.getLimit() : -1;
            float minMagnitude = mFilterOption.getMinMagnitude() > 0.0f ? mFilterOption.getMinMagnitude() : -1;
            if (year > 0) {
                url += "/" + String.format("%02d", year);
                if (month > 0) {
                    url += "/" + String.format("%02d", month);
                }
            }
            if (limit > 0) {
                url += "?" + LIMIT + "=" + limit;
            }
            if (minMagnitude > 0) {
                url += limit > 0 ? "&" : "?";
                url += MIN_MAGNITUDE + "=" + minMagnitude;
            }
        } else {
            url += "?" + LIMIT + "=" + DEFAULT_LIMIT;
        }
        Log.d(EarthquakeDataFetcher.class.getSimpleName(),
                "The url is " + url);
        new FetchEarthquakeDataTask().execute(url);
    }

    //Asynchronously fetch data to prevent blocking UI thread
    private class FetchEarthquakeDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                return JsonStringFetcher.fetchUrl(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                //convert string to Earthquake Objects
                EarthquakeDataParser earthquakeDataParser = new EarthquakeDataParser(result);
                boolean success = earthquakeDataParser.parse();
                if (success) {
                    mListener.onFetchSuccess(earthquakeDataParser.getEarthquakeTotalCount()
                            , earthquakeDataParser.getEarthquakes());
                } else {
                    mListener.onFetchFail();
                }
            } else {
                //something wrong is happened!
                mListener.onFetchFail();
            }
        }
    }

    public interface EarthquakeDataFetcherListener {
        void onFetchFail();
        void onFetchSuccess(int totalEarthquakes, List<Earthquake> earthquakes);
    }

    public static class FilterOption {
        private int mLimit = DEFAULT_LIMIT;
        private float mMinMagnitude;
        private int mYear;
        private int mMonth;

        public int getMonth() {
            return mMonth;
        }

        //set -1 to ignore this filter option
        public void setMonth(int month) {
            this.mMonth = month;
        }

        public int getLimit() {
            return mLimit;
        }

        //set -1 to ignore this filter option
        public void setLimit(int limit) {
            this.mLimit = limit;
        }

        public float getMinMagnitude() {
            return mMinMagnitude;
        }

        //set -1 to ignore this filter option
        public void setMinMagnitude(float minMagnitude) {
            this.mMinMagnitude = minMagnitude;
        }

        public int getYear() {
            return mYear;
        }

        //set -1 to ignore this filter option
        public void setYear(int year) {
            this.mYear = year;
        }
    }
}
