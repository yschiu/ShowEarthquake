package com.eason.earthquakedatafetcher.model;

import com.eason.earthquakedatafetcher.EarthquakeDataParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * present earthquake data as a Object
 */

public class Earthquake {
    private String mSource;
    private String mId;
    private Date mDate;
    private double mLatitude;
    private double mLongitude;
    private double mMagnitude;
    private double mDepth;
    private String mRegionName;

    public Earthquake(String source, String id, String date, double latitude,
                      double longitude, double magnitude, double depth, String regionName) throws IllegalArgumentException{
        this.mSource = source;
        this.mId = id;
        SimpleDateFormat format = new SimpleDateFormat(EarthquakeDataParser.DATE_FORMAT);
        try {
            mDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("date format is wrong");
        }
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mMagnitude = magnitude;
        this.mDepth = depth;
        this.mRegionName = regionName;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getSource() {
        return mSource;
    }

    public String getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public double getDepth() {
        return mDepth;
    }

    public String getRegionName() {
        return mRegionName;
    }
}
