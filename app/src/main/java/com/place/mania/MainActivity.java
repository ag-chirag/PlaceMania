package com.place.mania;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.place.mania.DataObjects.TwitterObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener{
    protected static final int RESULT_SPEECH = 1;
    RelativeLayout searchMic, searchButton;
    EditText searchInput;
    MainActivity activity;
    Geocoder geo;
    TextView bookmark ;
    List<Address> list;
    double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize()
    {
        activity = this;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getBaseContext()));
        searchMic = (RelativeLayout)findViewById(R.id.search_mic);
        searchInput = (EditText)findViewById(R.id.search_input);
        searchButton = (RelativeLayout)findViewById(R.id.search_go_button2);
        bookmark = (TextView)findViewById(R.id.bookmark);
        bookmark.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        searchMic.setOnClickListener(this);
        geo=new Geocoder(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
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

            case R.id.search_go_button2:

                try {
                    String address = searchInput.getText().toString();
                    list = geo.getFromLocationName(address, 1);
                    if(list.size()>0)
                    {
                        latitude=list.get(0).getLatitude();
                        longitude=list.get(0).getLongitude();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                Log.e("Latitude and Longitude", "" + latitude + longitude);
                startActivity(new Intent(activity, SearchActivity.class).putExtra("latitude", latitude).putExtra("longitude", longitude).putExtra("query",searchInput.getText().toString()));
                break;

            case R.id.bookmark:
                bookmarkDialog();
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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

    public void bookmarkDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //    dialog.setTitle("Bookmarks");
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(true);
        ArrayList<String> list = getBookmark();
        ListView listView = (ListView)dialog.findViewById(R.id.list);
        BookmarksAdapter adapter = new BookmarksAdapter(this, list, this);
        listView.setAdapter(adapter);
        dialog.show();
    }

    private class BookmarksAdapter extends BaseAdapter
    {
        private Context context;
        private List<String> listDataHeader; // header titles
        // child data in format of header title, child title
        private ArrayList<String> datalist;
        private ImageLoader imageLoader = ImageLoader.getInstance();
        Activity activity;
        public BookmarksAdapter(Context context, ArrayList<String> datalist, Activity activity) {
            this.activity = activity;
            this.context = context;
            this.datalist = datalist;
        }

        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public String getItem(int position) {
            return datalist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final String childParent = (String) datalist.get(position);


            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.dialog_item, null);
            }

            ((TextView)convertView.findViewById(R.id.text)).setText(datalist.get(position));
            return convertView;
        }

    }

    private ArrayList<String> getBookmark()
    {
        ArrayList<String> list = new ArrayList<String>();
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
        {   Log.e("bookmark",location);
            list.add(location);
        }
        return list;
    }

}
