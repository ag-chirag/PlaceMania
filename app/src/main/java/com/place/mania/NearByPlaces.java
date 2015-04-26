package com.place.mania;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.place.mania.PlaceManiaFragment;
import com.place.mania.R;

/**
 * Created by chirag on 4/26/15.
 */
public class NearByPlaces extends Fragment implements View.OnClickListener{

    ToggleButton restaurants, malls, pubs, hospitals, parks, schools;
    int checkedId = 0;
    String query = "";
    WebView webView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.near_by_places,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize()
    {
        restaurants = (ToggleButton)getView().findViewById(R.id.restaurants);
        malls = (ToggleButton)getView().findViewById(R.id.malls);
        pubs = (ToggleButton)getView().findViewById(R.id.pubs);
        hospitals = (ToggleButton)getView().findViewById(R.id.hospitals);
        parks = (ToggleButton)getView().findViewById(R.id.parks);
        schools = (ToggleButton)getView().findViewById(R.id.schools);

        restaurants.setOnClickListener(this);
        malls.setOnClickListener(this);
        parks.setOnClickListener(this);
        pubs.setOnClickListener(this);
        hospitals.setOnClickListener(this);
        schools.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(checkedId!=0 && checkedId!= view.getId())
        {
            ((ToggleButton)getView().findViewById(checkedId)).setChecked(false);
            checkedId = view.getId();
            query = ((ToggleButton) view).getTextOn().toString();
            loadWebView();
        }
        else if(checkedId!=0 && checkedId == view.getId()){
            checkedId = 0;
        }
        else if(checkedId == 0)
        {
            checkedId = view.getId();
            query = ((ToggleButton) view).getTextOn().toString();
            loadWebView();
        }
    }


    private void loadWebView()
    {
        Intent i= getActivity().getIntent();
        final double latitude=i.getDoubleExtra("latitude", 0);
        final double longitude=i.getDoubleExtra("longitude",0);
        Log.e("nearby places",query);
        String url = "http://olaapp.site50.net/jsplaces.html/?olat=" + latitude + "&olong=" + longitude + "&query=" + query;
        Log.e("nearby places",url);
        webView = (WebView) getView().findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        webView.loadUrl(url);

//        webView.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress)
//            {
//                if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
//                    Pbar.setVisibility(ProgressBar.VISIBLE);
//                }
//                Pbar.setProgress(progress);
//                if(progress == 100) {
//                    Pbar.setVisibility(ProgressBar.GONE);
//                }
//            }
//        });
    }
}
