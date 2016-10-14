package com.eason.earthquakedatafetcher;

import android.os.AsyncTask;

import com.eason.earthquakedatafetcher.model.Earthquake;

import java.io.IOException;
import java.util.List;

/**
 * This is used for fetching Earthquake data from RESTful data server
 */

public class EarthquakeDataFetcher {
    //where the json data come from
    private static final String SERVER_URL = "http://www.seismi.org/api/eqs?limit=20";
    //Used to notify fetch result
    private EarthquakeDataFetcherListener mListener;

    public EarthquakeDataFetcher(EarthquakeDataFetcherListener listener) {
        this.mListener = listener;
    }

    public void startFetch() {
        new FetchEarthquakeDataTask().execute(SERVER_URL);
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
}
