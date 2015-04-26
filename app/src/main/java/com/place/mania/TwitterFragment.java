package com.place.mania;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.place.mania.Authentication.TwitterLogin;
import com.place.mania.DataObjects.Const;
import com.place.mania.DataObjects.TwitterObject;
import com.place.mania.adapters.TweetListAdapter;

import java.net.Authenticator;
import java.util.ArrayList;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
/**
 * Created by hp1 on 21-01-2015.
 */
public class TwitterFragment extends Fragment {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Button mLoginButton;
    private Twitter mTwitter;
    private RequestToken mRequestToken;
    TwitterFragment fragment;
    TweetListAdapter adapter;
    ArrayList<TwitterObject> list;
    ListView listView;
    Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.twitter_fragment,container,false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initialize();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);

    }

    private void initialize()
    {
        fragment = this;
        mLoginButton = (Button)getView().findViewById(R.id.login);
        list = new ArrayList<TwitterObject>();
        listView = (ListView)getView().findViewById(R.id.list);
        adapter = new TweetListAdapter(activity,list,activity);
        listView.setAdapter(adapter);
        configureTwitterFactory();

        SharedPreferences pref = activity.getSharedPreferences(Const.PREF_NAME, activity.MODE_PRIVATE);
        if(pref.getString(Const.PREF_KEY_ACCESS_TOKEN,null) !=null)
        {
            mTwitter.setOAuthAccessToken(new AccessToken(pref.getString(Const.PREF_KEY_ACCESS_TOKEN,null),pref.getString(Const.PREF_KEY_ACCESS_TOKEN_SECRET,null)));
            new Thread()
            {
                public void run()
                {
                    getTweets();
                }
            }.start();

            mLoginButton.setVisibility(View.GONE);
        }
        else {
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mTwitter.setOAuthAccessToken(null);
                    new Thread() {
                        @Override
                        public void run() {
                            getAuth();
                        }
                    }.start();


                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == activity.RESULT_OK) {
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            String oauthVerifier = intent.getExtras().getString(Const.IEXTRA_OAUTH_VERIFIER);
                            AccessToken accessToken = null;
                            try {
                                accessToken = mTwitter.getOAuthAccessToken(mRequestToken, oauthVerifier);
                            } catch (TwitterException e) {
                                e.printStackTrace();
                            }

                            SharedPreferences pref = activity.getSharedPreferences(Const.PREF_NAME, activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(Const.PREF_KEY_ACCESS_TOKEN, accessToken.getToken());
                            editor.putString(Const.PREF_KEY_ACCESS_TOKEN_SECRET, accessToken.getTokenSecret());
                            editor.commit();
                            Log.e("twitter_token",accessToken.getToken());
                            getTweets();
                        }
                    }.start();
            } else if (resultCode == activity.RESULT_CANCELED) {
                Log.w(TAG, "Twitter auth canceled.");
            }
        }
    }


    private void getTweets()
    {
        double lat = activity.getIntent().getDoubleExtra("latitude",0);
        double lon = activity.getIntent().getDoubleExtra("longitude",0);
        String place = activity.getIntent().getStringExtra("query");
        double res = 5;
        String resUnit = "mi";
        Query query = new Query(place).geoCode(new GeoLocation(lat,lon), res, resUnit).count(100);
        QueryResult result = null;
        try {
            result = mTwitter.search(query);
        }
        catch (TwitterException e) {
            e.printStackTrace();
        }
        for (Status status : result.getTweets()) {
            Log.e("tweets","@" + status.getUser().getScreenName() + ":" + status.getText());
            list.add(new TwitterObject(status.getText(),status.getUser().getScreenName()));
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void configureTwitterFactory()
    {
        ConfigurationBuilder confbuilder = new ConfigurationBuilder();
        Configuration conf = confbuilder
                .setOAuthConsumerKey(Const.CONSUMER_KEY)
                .setOAuthConsumerSecret(Const.CONSUMER_SECRET)
                .build();
        mTwitter = new TwitterFactory(conf).getInstance();
    }

    private void getAuth()
    {
        try {
            mRequestToken = mTwitter.getOAuthRequestToken(Const.CALLBACK_URL);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(activity, TwitterLogin.class);
                intent.putExtra(Const.IEXTRA_AUTH_URL, mRequestToken.getAuthorizationURL());
                fragment.startActivityForResult(intent, 0);
            }
        });
    }
}