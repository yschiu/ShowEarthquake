package com.eason.showearthquake;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eason.earthquakedatafetcher.model.Earthquake;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for presenting data as a list
 */

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.ViewHolder> {
    private List<Earthquake> mEarthquakes = new ArrayList<>();

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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Earthquake earthquake = mEarthquakes.get(position);
        holder.mRegionNameTextView.setText(earthquake.getRegionName());
        holder.mMagnitude.setText(Double.toString(earthquake.getMagnitude()));
        holder.mDepth.setText(Double.toString(earthquake.getDepth()));
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        holder.mDate.setText(dateFormat.format(earthquake.getDate()));
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
        public ViewHolder(View itemView){
            super(itemView);

            this.mMagnitude = (TextView)itemView.findViewById(R.id.magnitude);
            this.mDepth = (TextView)itemView.findViewById(R.id.depth);
            this.mDate = (TextView)itemView.findViewById(R.id.timedate);
            this.mRegionNameTextView = (TextView)itemView.findViewById(R.id.region_name);
        }
    }
}
