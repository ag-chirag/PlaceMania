package com.place.mania;

/**
 * Created by chirag on 4/25/15.
 */
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.place.mania.Authentication.InstagramApp;
import com.place.mania.Authentication.InstagramSession;
import com.place.mania.DataObjects.InstagramObject;
import com.place.mania.R;
import com.place.mania.Utils.Utils;
import com.place.mania.adapters.ImageListAdapter;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Instagram extends Fragment implements View.OnClickListener{

    Activity activity;
    String CLIENT_ID	= "77d7811aeafe47048c5ce654088354b8";
    String CLIENT_SECRET	= "3df8c75d9268460587dc942b9e21bacf";
    String REDIRECT_URI	= "http://www.belogical.in";
    public static String ACCESS_TOKEN;
    String TAG_NAME = "";

    Button login;
    ListView listView;
    InstagramApp insta;
    Fragment fragment;
    ArrayList<InstagramObject> objectList;
    ImageListAdapter adapter;
    ArrayList<String> tags;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.instagram,container,false);
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public void initialize()
    {
        fragment = this;
        TAG_NAME = getActivity().getIntent().getStringExtra("query");
        String[] tags = TAG_NAME.split("\\s+");
        TAG_NAME = "";
        for (String tag : tags)
        TAG_NAME += tag;
        objectList = new ArrayList<InstagramObject>();
        login = (Button)getView().findViewById(R.id.login);
         insta = new InstagramApp(activity,CLIENT_ID,CLIENT_SECRET,REDIRECT_URI);
        listView = (ListView)getView().findViewById(R.id.list);
        adapter = new ImageListAdapter(activity,objectList, activity);
        listView.setAdapter(adapter);
        if(!insta.hasAccessToken())
        {
            Log.e("inside if","here");
            login.setOnClickListener(this);
        }
        else
        {
            Log.e("inside else","here");
            login.setVisibility(View.GONE);
            fetchImage();
        }
        insta.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                if(insta.hasAccessToken())
                {
                    login.setVisibility(View.GONE);
                    ACCESS_TOKEN = new InstagramSession(activity.getApplicationContext()).getAccessToken();
                    fetchImage();
                }
            }

            @Override
            public void onFail(String error) {

            }
        });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.login:
                insta.authorize();
                break;
        }
    }

    private void fetchImage()
    {
        String DATA_URL = "https://api.instagram.com/v1/tags/" + Uri.encode(TAG_NAME) +"/media/recent?access_token=";
        Utils.fetchDataFromUrl(DATA_URL+ACCESS_TOKEN, new Utils.HttpListener() {
            @Override
            public void success(String data) {
                try {
                    Gson gson = new Gson();

                    InstagramGSON obj = gson.fromJson(data, InstagramGSON.class);
                    ArrayList<InstagramGSON.InstagramItem> list;
                    list = obj.getInstagramItems();

                    for (InstagramGSON.InstagramItem item : list)
                    {
                        objectList.add(new InstagramObject(item.getImageUrl(),item.getCaption()));
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String reason) {

            }
        });
    }
}