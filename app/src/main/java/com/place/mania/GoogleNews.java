package com.place.mania;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.place.mania.Utils.Utils;
import com.place.mania.adapters.GoogleNewsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chirag on 4/26/15.
 */
public class GoogleNews extends Fragment {

    String NEWS_URL = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=";
    String query;
    ArrayList<GoogleNewsGSON> newsList;
    GoogleNewsAdapter adapter;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.google_news,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//    }

    private void initialize()
    {
        newsList = new ArrayList<GoogleNewsGSON>();
        query = getActivity().getIntent().getStringExtra("query");
        Utils.fetchDataFromUrl(NEWS_URL+ Uri.encode(query),new Utils.HttpListener() {
            @Override
            public void success(String data) {
                try {
                    JSONObject news = new JSONObject(data);
                    JSONObject x=news.getJSONObject("responseData");
                    JSONArray resultArray = x.getJSONArray("results");
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject newsItem = resultArray.getJSONObject(i);
                        String news_title = newsItem.getString("title");
                        String news_content = newsItem.getString("content");
                        String landing_url = newsItem.getString("url");
//                        JSONObject image = newsItem.getJSONObject("image");
//                        String image_url = image.getString("url");
                        newsList.add(new GoogleNewsGSON(news_title, news_content, landing_url, null));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void failed(String reason) {
                Log.e("news fetch","failed");
            }
        });

        listView = (ListView)getView().findViewById(R.id.list);
        adapter = new GoogleNewsAdapter(getActivity(),newsList,getActivity());
        listView.setAdapter(adapter);
    }
}
