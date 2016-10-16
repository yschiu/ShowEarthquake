package com.eason.showearthquake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eason.earthquakedatafetcher.model.Earthquake;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for presenting data as a list
 */

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.ViewHolder> {
    private List<Earthquake> mEarthquakes = new ArrayList<>();
    private WeakReference<Activity> mActivity;

    public EarthquakeAdapter(Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public int getItemCount() {
        return mEarthquakes.size();
    }

    public void addEarthquake(Earthquake earthquake) {
        mEarthquakes.add(earthquake);
        this.notifyDataSetChanged();
    }

    public void clear() {
        mEarthquakes.clear();
    }

    private void startMapIntent(double latitude, double longitude) {
        if (mActivity.get() != null) {
            Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude
                            + "?q=" + latitude + "," + longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            mActivity.get().startActivity(mapIntent);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Earthquake earthquake = mEarthquakes.get(position);
        holder.mRegionNameTextView.setText(earthquake.getRegionName());
        holder.mMagnitude.setText(Double.toString(earthquake.getMagnitude()));
        holder.mDepth.setText(Double.toString(earthquake.getDepth()));
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        holder.mDate.setText(dateFormat.format(earthquake.getDate()));
        holder.mGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMapIntent(earthquake.getLatitude(), earthquake.getLongitude());
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View earthquakeItem = LayoutInflater.from(context).inflate(R.layout.earthquake_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(earthquakeItem);
        return viewHolder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mMagnitude;
        public TextView mDepth;
        public TextView mDate;
        public TextView mRegionNameTextView;
        public ImageView mGoToMap;
        public ViewHolder(View itemView){
            super(itemView);

            this.mMagnitude = (TextView)itemView.findViewById(R.id.magnitude);
            this.mDepth = (TextView)itemView.findViewById(R.id.depth);
            this.mDate = (TextView)itemView.findViewById(R.id.timedate);
            this.mRegionNameTextView = (TextView)itemView.findViewById(R.id.region_name);
            this.mGoToMap = (ImageView)itemView.findViewById(R.id.go_to_map);
        }
    }
}
