package com.example.zissu.noche_1;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;


/**
 * A simple {@link Fragment} subclass.
 */
public class Register extends Fragment implements View.OnClickListener {
    private Button buttonRegister;
    private EditText editUserName, editPassword;
    private FirebaseAuth firebaseAuth;

    //something we added not in tutorial

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);


        buttonRegister = (Button) view.findViewById(R.id.bRegister);
        editUserName = (EditText) view.findViewById(R.id.etUsername);
        editPassword = (EditText) view.findViewById(R.id.etPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(this);

        return view;
    }

    private void registerUser() {
        String password = editPassword.getText().toString().trim();
        String userName = editUserName.getText().toString().trim();

        if (TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(), "please eneter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userName)){

            Toast.makeText(getActivity(), "please eneter user name", Toast.LENGTH_SHORT).show();
            return;
        }
        //if everything is ok

        firebaseAuth.createUserWithEmailAndPassword( userName , password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //email and password with at least 6 digits
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "successfully", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));

                        }
                        else
                        {
                            Toast.makeText(getActivity(), "try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister){
            registerUser();
        }
    }
}
