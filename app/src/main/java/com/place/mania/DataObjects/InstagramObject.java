package com.place.mania.DataObjects;

/**
 * Created by chirag on 4/25/15.
 */
public class InstagramObject {

    String url;
    String caption;

    public InstagramObject(String url,String caption)
    {
        this.url = url;
        this.caption = caption;
    }

    public String getUrl(){return url;}
    public String getCaption(){
        return caption;
    }

    public void setUrl(String s)
    {
        url = s;
    }

    public void setCaption(String s)
    {
        caption = s;
    }
}
