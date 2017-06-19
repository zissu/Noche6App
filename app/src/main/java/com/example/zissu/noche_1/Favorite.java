package com.example.zissu.noche_1;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.zissu.noche_1.models.PlaceModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
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

public class Favorite extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private TextView tvData;
    private ListView lvPlaces1;
    private Button btnFavorite;

    /*for map*/
    private static final String TAG = "MainActivity";
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private LocationManager locationManager;

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


        lvPlaces1 = (ListView) findViewById(R.id.lvPlaces1);
        btnFavorite = (Button) findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener((View.OnClickListener) this);

        /*Location*/

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //check whether location service is enable or not in your  phone
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        // You can now create a LatLng Object for use with maps
         LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }



    @Override
    public void onClick(View v) {
        double latitude = mLocation.getLatitude();
        double longitude =  mLocation.getLongitude();
        if(v == btnFavorite){
            new JSONTask().execute("http://193.106.55.121:8080/getClosestPlaces/bar/" + longitude+"/" + latitude + "/");
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
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }
    public void onClickMoreInfo (View view){
        Intent intent = new Intent(this, MoreInfo.class);
        intent.putExtra("data", "More info");
        startActivity(intent);
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
