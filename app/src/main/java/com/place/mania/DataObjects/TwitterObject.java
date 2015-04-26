package com.place.mania.DataObjects;

import com.place.mania.TwitterFragment;

/**
 * Created by chirag on 4/26/15.
 */
public class TwitterObject {

    String tweet;
    String screenName;

    public TwitterObject(String tweet, String screenName)
    {
        this.tweet = tweet;
        this.screenName = screenName;
    }

    public String getTweet()
    {
        return tweet;
    }

    public String getScreenName()
    {
        return screenName;
    }
}
