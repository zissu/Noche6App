package com.example.zissu.noche_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

public class Favorite extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Button btnGet = (Button)findViewById(R.id.btnGet);
        assert btnGet !=null;
        btnGet.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        v.setEnabled(false);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://193.106.55.121:8080/getClosestPlaces/club/23.356/24.248/", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
           if (responseBody != null){
               TextView textResponse = (TextView)findViewById(R.id.textResponse);
               assert textResponse != null;
               textResponse.setText(new String(responseBody));
               /*try {
                   JSONArray array = new JSONArray(new String(responseBody));
               } catch (Exception e) {
                   e.printStackTrace();
               }*/
           }
                v.setEnabled(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                v.setEnabled(true);
            }
        });
    }
}
