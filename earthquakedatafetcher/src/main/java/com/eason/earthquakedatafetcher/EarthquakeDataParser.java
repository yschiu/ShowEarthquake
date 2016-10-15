package com.eason.earthquakedatafetcher;

import com.eason.earthquakedatafetcher.model.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This is used for converting json string of earthquake data to a list of Earthquake object
 * Data can be found at http://www.seismi.org/api/eqs/
 */

public class EarthquakeDataParser {
    public static final String DATE_FORMAT ="yyyy-MM-DD HH:mm:ss";
    private static final String COUNT = "count";//Amount of earthquakes for request
    private static final String EARTHQUAKE = "earthquakes";//List of earthquakes
    private static final String EARTHQUAKE_SRC = "src";//Source station for earthquake data
    private static final String EARTHQUAKE_ID = "eqid";//ID of earthquake as it is provided by source
    private static final String EARTHQUAKE_DATE = "timedate";//Time of the earthquake. (YYYY-MM-DD HH:MM:SS)
    private static final String EARTHQUAKE_LATITUDE = "lat";//Coordinate of earthquake (23.0000)
    private static final String EARTHQUAKE_LONGITUDE = "lon";//Coordinate of earthquake (176.0000)
    private static final String EARTHQUAKE_MAGNITUDE = "magnitude";//Magnitude of earthquake in Richter scale (6.8)
    private static final String EARTHQUAKE_DEPTH = "depth";//Depth of earthquake in km (315.40)
    private static final String EARTHQUAKE_REGION = "region";//Region info as provided from USGS (eastern Honshu, Japan)

    private String mEarthquakeJsonData;

    private int mTotalCount;
    private List<Earthquake> mEarthquakes;

    public EarthquakeDataParser(String json) {
        mEarthquakeJsonData = json;
    }

    public List<Earthquake> getEarthquakes() {
        return mEarthquakes;
    }

    public int getEarthquakeTotalCount() {
        return mTotalCount;
    }

    public boolean parse() {
        try {
            JSONObject json = new JSONObject(mEarthquakeJsonData);
            mTotalCount = json.getInt(COUNT);
            JSONArray earthquakes = json.getJSONArray(EARTHQUAKE);
            List<Earthquake> earthquakeList = new ArrayList<>();
            for (int i = 0 ; i < earthquakes.length(); i++) {
                JSONObject earthquake = earthquakes.getJSONObject(i);
                String src = earthquake.getString(EARTHQUAKE_SRC);
                String id = earthquake.getString(EARTHQUAKE_ID);
                String timedate = earthquake.getString(EARTHQUAKE_DATE);
                double latitude = earthquake.getDouble(EARTHQUAKE_LATITUDE);
                double longitude = earthquake.getDouble(EARTHQUAKE_LONGITUDE);
                double magnitude = earthquake.getDouble(EARTHQUAKE_MAGNITUDE);
                double depth = earthquake.getDouble(EARTHQUAKE_DEPTH);
                String region = earthquake.getString(EARTHQUAKE_REGION);
                Earthquake quake = new Earthquake(src, id, timedate, latitude, longitude,
                        magnitude, depth, region);
                earthquakeList.add(quake);
            }
            mEarthquakes = earthquakeList;
            if (mEarthquakes.size() > 0)
                return true;
            else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
