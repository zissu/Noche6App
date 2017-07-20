package com.example.zissu.noche_1.models.loginandregister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zissu.noche_1.R;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

public class LoginAcitivtyReal extends AppCompatActivity {
    EditText username, pass;
    Button btn;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitivty_real);

        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);

        username = (EditText) findViewById(R.id.username) ;
        pass = (EditText) findViewById(R.id.pass) ;
        btn = (Button) findViewById(R.id.loginbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpPostRequest res = new HttpPostRequest(LoginAcitivtyReal.this,"http://193.106.55.121:8080/login/" + username.getText().toString()+ "/"+pass.getText().toString()+"/",new LoginRequest(LoginAcitivtyReal.this,username.getText().toString(),pass.getText().toString()) );
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                res.execute(nameValuePairs);
            }
        });




    }

    public void StopPB() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(View.INVISIBLE);
            }
        });
    }
}
