package com.place.mania;

import java.util.ArrayList;

/**
 * Created by HAL53 on 25-04-2015.
 */
public class InstagramGSON {

    ArrayList<InstagramItem> data;

    public ArrayList<InstagramItem> getInstagramItems()
    {
        return data;
    }

    public class InstagramItem
    {
        String type;
        Caption caption;
        InstagramImage images;
        public String getType()
        {
            return type;
        }

        public String getCaption()
        {
            return caption.getText();
        }

        public String getImageUrl()
        {
            return images.getImage().getUrl();
        }
    }

    public class Caption
    {
        String text;

        public String getText()
        {
            return text;
        }
    }

    public class InstagramImage
    {
        public StandardImage standard_resolution;

        public StandardImage getImage()
        {
            return standard_resolution;
        }
    }

    public class StandardImage
    {
        String url;

        public String getUrl(){
            return url;
        }
    }
}
