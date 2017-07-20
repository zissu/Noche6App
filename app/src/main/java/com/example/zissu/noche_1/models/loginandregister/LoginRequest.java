package com.example.zissu.noche_1.models.loginandregister;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.zissu.noche_1.MyProfileActivity;

/**
 * Created by zissu on 19/07/2017.
 */

public class LoginRequest implements IActionOnPost {
    Activity activity;
    String email,pass;
    public LoginRequest(Activity activity2, String email, String pass) {
        this.activity = activity2;
        this.pass = pass;
        this.email = email;
    }
    @Override
    public void OnPost(String responeFromServer) {
        try
        {
            ((LoginAcitivtyReal) activity).StopPB();

            if (responeFromServer != null) {
                Intent intent = new Intent(activity.getApplicationContext(), MyProfileActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("pass", pass);
                activity.startActivity(intent);
            } else{
                Toast.makeText(activity, "Failed to login", Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
