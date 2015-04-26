package com.place.mania;

/**
 * Created by chirag on 4/26/15.
 */
public class GoogleNewsGSON {


    String title, content, url, image_url;

    public GoogleNewsGSON(String title, String content, String url, String image_url) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getImage_url() {
        return image_url;
    }
}

