package com.mbe.chall2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.mbe.chall2.helper.EndpointHelper;
import com.mbe.chall2.helper.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEt;
    private EditText passwordEt;
    private EditText confirmPasswordEt;
    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameEt = findViewById(R.id.usernameEt);
        passwordEt = findViewById(R.id.passwordEt);
        confirmPasswordEt = findViewById(R.id.confirmpasswordEt);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EndpointHelper endpointHelper = EndpointHelper.GetEndpointHelperInstance();
                JSONObject registerData = new JSONObject();
                try{
                    registerData.put("username", usernameEt.getText().toString());
                    registerData.put("password", passwordEt.getText().toString());
                    registerData.put("confirm_password", confirmPasswordEt.getText().toString());
                    endpointHelper.MakeRequest(RegisterActivity.this, Request.Method.POST, "/api/register",registerData, new VolleyCallback<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try{
                                String msg = response.getString("Message");
                                Toast.makeText(RegisterActivity.this, msg , Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }catch(JSONException e){
                                Log.e("Json parsing error", e.toString());
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            if(error.networkResponse != null){
                                try{
                                    JSONObject errorMessage = new JSONObject(new String(error.networkResponse.data));
                                    Toast.makeText(getApplicationContext(), errorMessage.getString("error"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Log.e("/register api is reached but there is somethings wrong", e.toString());
                                }
                            }else{
                                Log.e("Error on fetching /register", error.toString());
                            }
                        }
                    });

                }catch(JSONException e){
                    Log.e("Failed on creating json data", e.toString());
                }

            }
        });
    }
}