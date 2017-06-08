package com.example.zissu.noche_1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.zissu.noche_1.models.PlaceModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Favorite extends AppCompatActivity implements View.OnClickListener {
    private TextView tvData;
    private ListView lvPlaces1;
    private Button btnFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

// Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start


        lvPlaces1 = (ListView)findViewById(R.id.lvPlaces1);
        btnFavorite = (Button)findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnFavorite){
            new JSONTask().execute("http://193.106.55.121:8080/getClosestPlaces/bar/23.2456/34.5667/");
        }
    }

    public class JSONTask extends AsyncTask<String, String, List<PlaceModel>> {

        @Override
        protected List<PlaceModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONArray parentArray = new JSONArray(finalJson);

                List<PlaceModel> placeModelsList = new ArrayList<>();
                for(int i = 0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    PlaceModel placeModel = new PlaceModel();
                    placeModel.setName(finalObject.getString("name"));
                    placeModel.setOpeningHours(finalObject.getString("openingHours"));
                    placeModel.setPhone(finalObject.getString("phone"));
                    placeModel.setWeb(finalObject.getString("web"));
                    placeModel.setLine(finalObject.getString("line"));
                    placeModel.setRank(finalObject.getDouble("rank"));
                    placeModel.setUrlFront(finalObject.getString("urlFront"));
                    placeModel.setUrlInside(finalObject.getString("urlInside"));
                    //might be problematic
                    PlaceModel.Location location = new PlaceModel.Location();
                    /*JSONObject locationObject = finalObject.getJSONObject("location");
                    location.setCity(locationObject.getString("city"));
                    location.setLat(locationObject.getDouble("lat"));
                    location.setLon(locationObject.getDouble("lon"));*/


                    //add multiple places
                    placeModelsList.add(placeModel);
                }


                return placeModelsList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<PlaceModel> result) {
            super.onPostExecute(result);

            Favorite.PlaceAdapter adapter = new Favorite.PlaceAdapter(getApplicationContext(), R.layout.row, result);
            lvPlaces1.setAdapter(adapter);
        }
    }

    public class PlaceAdapter extends ArrayAdapter {

        private List<PlaceModel> placeModelList;
        private int resource;
        private LayoutInflater inflater;

        public PlaceAdapter(Context context, int resource, List<PlaceModel> objects) {
            super(context, resource, objects);
            placeModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = inflater.inflate(resource, null);
            }

            ImageView icon;
            TextView tvName, tvOpeningHours, web, phone, line1;
            RatingBar rank;
            TextView location1, city, lat, lon;

            icon = (ImageView)convertView.findViewById(R.id.icon);
            tvName = (TextView)convertView.findViewById(R.id.tvName);
            tvOpeningHours = (TextView)convertView.findViewById(R.id.tvOpeningHours);
            web = (TextView)convertView.findViewById(R.id.web);
            phone = (TextView)convertView.findViewById(R.id.phone);
            line1 = (TextView)convertView.findViewById(R.id.line);
            rank = (RatingBar)convertView.findViewById(R.id.rank);
            //location1 =  (TextView)convertView.findViewById(R.id.location);

            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(placeModelList.get(position).getUrlInside(), icon); // Default options will be used



            tvName.setText(placeModelList.get(position).getName());
            tvOpeningHours.setText(placeModelList.get(position).getOpeningHours());
            web.setText(placeModelList.get(position).getWeb());
            phone.setText(placeModelList.get(position).getPhone());
            line1.setText(placeModelList.get(position).getLine());

            //rating bar
            //rank.setRating(placeModelList.get(position).getRank());

/*            StringBuffer stringBuffer = new StringBuffer();
            for (PlaceModel.Location location : placeModelList.get(position).getLocations()){
                stringBuffer.append(location.getCity() + "/n" + location.getLon() + "/n" + location.getLat() + "/n" +
                        location.getLon() + "/n");
            }
            location1.setText(stringBuffer);*/
            return convertView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//        if(id == R.id.action_refresh){
//            new JSONTask().execute("http://193.106.55.121:8080/getClosestPlaces/bar/23.2456/34.5667/");
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
