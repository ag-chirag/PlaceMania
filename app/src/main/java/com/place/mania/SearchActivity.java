package com.place.mania;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.place.mania.adapters.SearchFragmentPagerAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by chirag on 4/25/15.
 */
public class SearchActivity extends FragmentActivity implements View.OnClickListener {

    protected static final int RESULT_SPEECH = 1;
    ImageView backButton;
    TextView searchInput, bookmark;
    private ViewPager resultsViewPager;
    private List<SearchResultPage> pages;
    SearchFragmentPagerAdapter searchFragmentPagerAdapter;
    SearchActivity activity;
    String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        query = getIntent().getStringExtra("query");
        initialize();
    }


    private void initialize() {
        activity = this;
        bookmark = (TextView) findViewById(R.id.bookmark);
        searchInput = (TextView) findViewById(R.id.search_input);
        backButton = (ImageView)findViewById(R.id.search_back_button2);
        searchInput.setText(query);
        backButton.setOnClickListener(this);
        resultsViewPager = (ViewPager) findViewById(R.id.search_viewpager);
        bookmark.setOnClickListener(this);
        pages = Arrays.asList(
                new SearchResultPage("Map Location",new MapLocations()),
                new SearchResultPage("Near by places",new NearByPlaces()),
                new SearchResultPage("Twitter", new TwitterFragment()),
                new SearchResultPage("Instagram", new Instagram()),
                new SearchResultPage("Google News", new GoogleNews())
        );

        resultsViewPager.setOffscreenPageLimit(pages.size());
        loadSearchPages(query);
        checkBookmark();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.search_mic:
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    searchInput.setText("");
                } catch (ActivityNotFoundException a) {
//                    App.makeToast("Opps! Your device doesn't support Speech to Text");
                }
                break;

            case R.id.search_back_button2:
                finish();
                break;

            case R.id.bookmark:
                if(!checkBookmark())
                    addBookmark();
                else
                    deleteBookmark();
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    searchInput.setText(text.get(0));
                }
                break;
            }
        }
    }


    private void loadSearchPages(String query) {

        if (searchFragmentPagerAdapter == null) {
            searchFragmentPagerAdapter = new SearchFragmentPagerAdapter(getSupportFragmentManager(), pages);
            resultsViewPager.setAdapter(searchFragmentPagerAdapter);
            SlidingTabLayout tabStrip = (SlidingTabLayout) findViewById(R.id.search_tabs);

            // Setting Custom Color for the Scroll bar indicator of the Tab View
            tabStrip.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.white);
                }
            });

            tabStrip.setViewPager(resultsViewPager);
//            for (SearchResultPage page : pages)
//                page.getFragment().doSearchForQuery(query);

        } else {

//            for (SearchResultPage page : pages)
//                page.getFragment().doSearchForQuery(query);

        }
    }


    private boolean checkBookmark()
    {
        SharedPreferences pref = getSharedPreferences("Bookmark List", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Type collectionType = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> bookmark_list = new ArrayList<>();
        if (pref.contains("bookmark set")) {
            String subscription_string = pref.getString("bookmark set", null);
            Gson gson = new Gson();
            if (subscription_string != null)
                bookmark_list = gson.fromJson(subscription_string, collectionType);
        }

        for(String location : bookmark_list)
            if(location == searchInput.getText().toString())
            {
                bookmark.setBackgroundColor(getResources().getColor(R.color.red));
                bookmark.setText("BOOKMARKED");
                return true;
            }
        return false;
    }

    private void addBookmark()
    {
        SharedPreferences pref = getSharedPreferences("Bookmark List", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Type collectionType = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> bookmark_list = new ArrayList<>();
        Gson gson = new Gson();
        if (pref.contains("bookmark set")) {
            String subscription_string = pref.getString("bookmark set", null);

            if (subscription_string != null)
                bookmark_list = gson.fromJson(subscription_string, collectionType);
        }
        bookmark_list.add(searchInput.getText().toString());
        editor.putString("bookmark set", gson.toJson(bookmark_list));
        editor.commit();
        bookmark.setBackgroundColor(getResources().getColor(R.color.red));
        bookmark.setText("BOOKMARKED");
    }

    private void deleteBookmark()
    {
        SharedPreferences pref = getSharedPreferences("Bookmark List", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Type collectionType = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> bookmark_list = new ArrayList<>();
        Gson gson = new Gson();
        if (pref.contains("bookmark set")) {
            String subscription_string = pref.getString("bookmark set", null);

            if (subscription_string != null)
                bookmark_list = gson.fromJson(subscription_string, collectionType);
        }
        bookmark_list.remove(searchInput.getText().toString());
        editor.putString("bookmark set", gson.toJson(bookmark_list));
        editor.commit();
        bookmark.setBackgroundColor(getResources().getColor(R.color.transparent));
        bookmark.setText("BOOKMARK");
    }

    public class SearchResultPage {

        private String title;
        //       private SearchFragment fragment;
        private Fragment fragment;

        public SearchResultPage(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }

        public String getTitle() {
            return title;
        }

        public Fragment getFragment() {
            return fragment;
        }
    }

}