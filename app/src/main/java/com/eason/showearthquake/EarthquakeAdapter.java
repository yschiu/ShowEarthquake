package com.eason.showearthquake;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eason.earthquakedatafetcher.model.Earthquake;

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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Earthquake earthquake = mEarthquakes.get(position);
        holder.mRegionNameTextView.setText(earthquake.getRegionName());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View earthquakeItem = LayoutInflater.from(context).inflate(R.layout.earthquake_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(earthquakeItem);
        return viewHolder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mRegionNameTextView;
        public ViewHolder(View itemView){
            super(itemView);

            this.mRegionNameTextView = (TextView) itemView.findViewById(R.id.region_name);
        }
    }
}