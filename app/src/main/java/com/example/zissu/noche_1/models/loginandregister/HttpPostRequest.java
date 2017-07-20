package com.example.zissu.noche_1.models.loginandregister;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.HttpEntity;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by zissu on 19/07/2017.
 */
public class HttpPostRequest extends AsyncTask<List<NameValuePair>, String, String> {
    Activity activity;
    String url;
    List<NameValuePair> _params;
    IActionOnPost onPost = null;

    public HttpPostRequest(Activity activity, String task, IActionOnPost on_post) {
        this.activity = activity;
        this.url = task;
        onPost = on_post;
    }

    @Override
    protected String doInBackground(List<NameValuePair>... keyAndValue) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(this.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();

            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }



        return null;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");
            try {
                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            } catch (Exception ex){
        }

        }

        return result.toString();
    }

    protected String getASCIIContentFromEntity(HttpEntity entity)
            throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n > 0) {
            byte[] b = new byte[4096];
            n = in.read(b);
            if (n > 0)
                out.append(new String(b, 0, n));
        }
        return out.toString();
    }
    @Override
    protected void onPostExecute(String result) {
        if (onPost != null) {
            onPost.OnPost(result);
        }

    }



}

