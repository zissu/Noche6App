package com.example.zissu.noche_1;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment implements View.OnClickListener {
   private Button log_reg;
    private EditText editTextEmail, editTextPassword;
    private Button buttonSignIn;

    private FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        Button log_reg = (Button) rootView.findViewById(R.id.bRegister);
        log_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment register = new Register();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.login_l, register);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){

        }
        editTextEmail = (EditText) rootView.findViewById(R.id.eEmail);
        editTextPassword = (EditText) rootView.findViewById(R.id.ePassword);
        buttonSignIn = (Button) rootView.findViewById(R.id.bLogin);

        buttonSignIn.setOnClickListener(this);

        return rootView;
    }

private void userLogin(){
    String email = editTextEmail.getText().toString().trim();
    String password = editTextPassword.getText().toString().trim();

    if (TextUtils.isEmpty(password)){
        Toast.makeText(getActivity(), "please eneter password", Toast.LENGTH_SHORT).show();
        return;
    }
    if(TextUtils.isEmpty(email)){

        Toast.makeText(getActivity(), "please eneter user name", Toast.LENGTH_SHORT).show();
        return;
    }

    firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //start activity
                    startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));

                    }
                }
            });
}
    @Override
    public void onClick(View v) {
        if (v == buttonSignIn){
            userLogin();
        }
    }
}
