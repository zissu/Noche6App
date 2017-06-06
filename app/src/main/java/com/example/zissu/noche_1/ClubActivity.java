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

public class ClubActivity extends AppCompatActivity {

    private ListView lvClub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start


        lvClub = (ListView)findViewById(R.id.lvClub);
    }

    public class Task extends AsyncTask<String, String, List<PlaceModel>>{

        @Override
        protected List<PlaceModel> doInBackground(String... params) {
            HttpURLConnection connection2 = null;
            BufferedReader reader2 = null;
            try {
                URL url2 = new URL(params[0]);
                connection2 = (HttpURLConnection) url2.openConnection();
                connection2.connect();

                InputStream stram2 = connection2.getInputStream();
                reader2 = new BufferedReader(new InputStreamReader(stram2));

                StringBuffer buffer2 = new StringBuffer();

                String line3 = "";
                while ((line3 = reader2.readLine()) != null) {
                    buffer2.append(line3);
                }
                ImageView urlPic1 = null;
                String finalJson = buffer2.toString();
                JSONArray parentArray = new JSONArray(finalJson);
                StringBuffer data = new StringBuffer();

                List<PlaceModel> clubModelList = new ArrayList<>();
                for(int i=0; i<parentArray.length(); i++){
                    PlaceModel clubModel = new PlaceModel();
                    clubModel.setUrlFront(parentArray.getString(i));
                    clubModelList.add(clubModel);
                }

                return clubModelList;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection2 != null)
                    connection2.disconnect();
                try {
                    if (reader2 != null) {
                        reader2.close();
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
            ClubActivity.ClubAdapter adapter1 = new ClubActivity.ClubAdapter(getApplicationContext(), R.layout.club_row, result);
            lvClub.setAdapter(adapter1);
        }

    }

    public class ClubAdapter extends ArrayAdapter{
        private List<PlaceModel> placeModelsList;
        private int resource;
        private LayoutInflater inflater;

        public ClubAdapter(Context context, int resource, List<PlaceModel> objects){
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
            ImageView club_logo;

            club_logo = (ImageView)convertView.findViewById(R.id.club_logo);
            ImageLoader.getInstance().displayImage(placeModelsList.get(position).getUrlFront() , club_logo);
            return convertView;
        }
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
            new ClubActivity.Task().execute("http://193.106.55.121:8080/getFrontUrlClubs");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onClickMoreInfo (View view){
        Intent intent = new Intent(this, MoreInfo.class);
        intent.putExtra("data", "More info from intent");
        startActivity(intent);
    }
}