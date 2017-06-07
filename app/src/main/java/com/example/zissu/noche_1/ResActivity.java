package com.example.zissu.noche_1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import com.example.zissu.noche_1.R;
import com.example.zissu.noche_1.models.PlaceModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResActivity extends AppCompatActivity {

    private TextView logos;
    private ListView lvRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start


        lvRes = (ListView)findViewById(R.id.lvRes);
        //        new Task().execute("http://193.106.55.121:8080/getFrontUrlRest");
    }
    public class Task extends AsyncTask<String, String, List<PlaceModel>>{

        @Override
        protected List<PlaceModel> doInBackground(String... params) {
            HttpURLConnection connection1 = null;
            BufferedReader reader1 = null;
            try {
                URL url1 = new URL(params[0]);
                connection1 = (HttpURLConnection) url1.openConnection();
                connection1.connect();

                InputStream stram1 = connection1.getInputStream();
                reader1 = new BufferedReader(new InputStreamReader(stram1));

                StringBuffer buffer1 = new StringBuffer();

                String line2 = "";
                while ((line2 = reader1.readLine()) != null) {
                    buffer1.append(line2);
                }
                ImageView urlPic = null;
                String finalJson = buffer1.toString();
                JSONArray parentArray = new JSONArray(finalJson);
                StringBuffer data = new StringBuffer();

                List<PlaceModel> resModelList = new ArrayList<>();
                for(int i=0; i<parentArray.length(); i++){
                    PlaceModel resModel = new PlaceModel();
                     resModel.setUrlFront(parentArray.getString(i));
                    resModelList.add(resModel);
                }

                return resModelList;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection1 != null)
                    connection1.disconnect();
                try {
                    if (reader1 != null) {
                        reader1.close();
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
            ResAdapter adapter = new ResAdapter(getApplicationContext(), R.layout.res_row, result);
            lvRes.setAdapter(adapter);
        }

    }

    public class ResAdapter extends ArrayAdapter{
        private List<PlaceModel> placeModelsList;
        private int resource;
        private LayoutInflater inflater;

        public ResAdapter(Context context, int resource, List<PlaceModel> objects){
            super(context, resource, objects);
            placeModelsList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = inflater.inflate(resource, null);
            }
            ImageView res_logo;

            res_logo = (ImageView)convertView.findViewById(R.id.res_logo);
            ImageLoader.getInstance().displayImage(placeModelsList.get(position).getUrlFront() , res_logo);
            return convertView;
        }
    }

    public void onClickMoreInfo (View view){
        Intent intent = new Intent(this, MoreInfo.class);
        intent.putExtra("data", "More info from intent");
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            new Task().execute("http://193.106.55.121:8080/getFrontUrlRest");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}