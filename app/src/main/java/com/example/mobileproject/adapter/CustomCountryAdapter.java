package com.example.mobileproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.mobileproject.R;

import java.util.ArrayList;

public class CustomCountryAdapter extends BaseAdapter {
    // This custom adapter populates the GridView with a visual representation of each thumbnail in the input data set. It also implements a method -getView()- to access individual cells in the GridView.
    private final Context context; // main activity’s context
    String[] countryList;; // thumbnail data set

    public CustomCountryAdapter(Context mainActivityContext, String[] list) {
        context = mainActivityContext; countryList = list;
    }
    // how many entries are there in the data set?
    public int getCount() { return countryList.length; }
    // what is in a given 'position' in the data set?
    public Object getItem(int position) { return countryList[position]; }
    // what is the ID of data item in given 'position‘?
    public long getItemId(int position) { return position; }
    // create a view for each thumbnail in the data set, add it to gridview
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            row = View.inflate(parent.getContext(), R.layout.custom_list_country, null);
        }
        else row = convertView;
        TextView name = (TextView) row.findViewById(R.id.countryName);
        String countryName=(String)getItem(position);
        name.setText(countryName);

        return row;

    }//getView

}
