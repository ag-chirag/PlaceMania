package com.place.mania.Utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chirag on 4/25/15.
 */
public class Utils {



    public interface HttpListener {
        public void success(String data);

        public void failed(String reason);
    }

    public static void fetchDataFromUrl(final String url, final HttpListener listener) {

        if (listener == null) {
            return;
        }

        final String LOGTAG = "Url called";
        new Thread(new Runnable() {

            @Override
            public void run() {
                String data = "";

     {

                    Log.d("Url called", "Final URL = " + url);

                    try {
                        HttpGet get = new HttpGet(url);

                        HttpClient client = new DefaultHttpClient();
                        HttpResponse response = client.execute(get);
                        int status = response.getStatusLine().getStatusCode();
                        InputStream dataStream = response.getEntity().getContent();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataStream));
                        String responseLine;
                        StringBuilder responseBuilder = new StringBuilder();
                        while ((responseLine = bufferedReader.readLine()) != null) {
                            responseBuilder.append(responseLine);
                        }

                        data = responseBuilder.toString();

                        if (data != null) {
                            listener.success(data);
                        } else {
                            listener.failed("Null data received");
                        }

                        if (status == HttpStatus.SC_UNAUTHORIZED) {
                            Log.e(LOGTAG, "Server said Unauthorized. Url :" + url);
                            listener.failed("Unauthorized");
                        } else if (status != HttpStatus.SC_OK) {
                            Log.e(LOGTAG, "Error " + status + " " + org.apache.commons.httpclient.HttpStatus.getStatusText(status) + " data : " + data);
                            Log.e(LOGTAG, "Url was : " + url);
                            listener.failed("Error " + status + " " + org.apache.commons.httpclient.HttpStatus.getStatusText(status) + " data : " + data);
                        }
                    } catch (ClientProtocolException e) {
                        Log.e(LOGTAG, "ClientProtocolException", e);
                        listener.failed("ClientProtocolException ");
                    } catch (IOException e) {
                        Log.e(LOGTAG, "IOException ", e);
                        listener.failed("IOException ");
                    } catch (StackOverflowError e) {
                        Log.e(LOGTAG, "StackOverflowError ", e);
                        listener.failed("StackOverflowError ");
                    } catch (IllegalArgumentException e) {
                        Log.e(LOGTAG, "IllegalArgumentException ", e);
                        listener.failed("IllegalArgumentException ");
                    } catch (IllegalStateException e) {
                        Log.e(LOGTAG, "IllegalStateException ", e);
                        listener.failed("IllegalStateException ");
                    } catch (Exception e) {
                        Log.e(LOGTAG, "IllegalStateException ", e);
                        listener.failed("IllegalStateException ");
                    }
                }
            }
        }).start();

    }

}
