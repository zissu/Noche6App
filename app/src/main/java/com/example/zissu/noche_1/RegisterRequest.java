package com.example.zissu.noche_1;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zissu on 16/07/2017.
 */

public class RegisterRequest extends StringRequest {
    private final static String Register_Request_URL = "193.106.55.121:8080/userName/email/password/age/gender/interests";
    private Map<String, String> params;
    public RegisterRequest(String userName, String email, String gender, int age,String interests, String password, Response.Listener<String> listener) {
        super(Method.POST, Register_Request_URL, listener, null);
        params = new HashMap<>();
        params.put("userName", userName);
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");
        params.put("gender", gender);
        params.put("interests", interests);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
