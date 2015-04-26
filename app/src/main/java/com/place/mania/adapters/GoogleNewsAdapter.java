package com.place.mania.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.place.mania.DataObjects.InstagramObject;
import com.place.mania.DataObjects.TwitterObject;
import com.place.mania.GoogleNewsGSON;
import com.place.mania.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chirag on 3/17/15.
 */
public class GoogleNewsAdapter extends BaseAdapter {


    private Context context;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private ArrayList<GoogleNewsGSON> datalist;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    Activity activity;
    public GoogleNewsAdapter(Context context, ArrayList<GoogleNewsGSON> datalist, Activity activity) {
        this.activity = activity;
        this.context = context;
        this.datalist = datalist;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public GoogleNewsGSON getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final GoogleNewsGSON childParent = (GoogleNewsGSON) datalist.get(position);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_tweet, null);
        }

        ((TextView)convertView.findViewById(R.id.tweet)).setText(datalist.get(position).getContent());
        ((TextView)convertView.findViewById(R.id.name)).setText(datalist.get(position).getTitle());
        return convertView;
    }

}
