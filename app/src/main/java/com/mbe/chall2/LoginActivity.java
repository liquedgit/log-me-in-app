package com.mbe.chall2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEt;
    private EditText passwordEt;
    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEt = findViewById(R.id.usernameEt);
        passwordEt = findViewById(R.id.passwordEt);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEt.getText().toString();
                String password = passwordEt.getText().toString();
                EndpointHelper endpointHelper = EndpointHelper.GetEndpointHelperInstance();
                JSONObject jsonObj = new JSONObject();
                try{
                    jsonObj.put("username", username);
                    jsonObj.put("password", password);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                endpointHelper.MakeRequest(getApplicationContext(),Request.Method.POST, "/api/login",jsonObj, new VolleyCallback<JSONObject>() {

                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        SharedPreferences sp = getApplicationContext().getSharedPreferences(String.valueOf(R.string.shared_preferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor spEditor = sp.edit();
                        try{
                            spEditor.putString("token", (String)jsonObject.get("token"));
                            spEditor.apply();
                            Toast.makeText(LoginActivity.this, (String) jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                        }catch(JSONException e){
                            Log.e("Error on parsing JSON", e.toString());
                        }
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        if(error.networkResponse != null){
                            try{
                                JSONObject errorMessage = new JSONObject(new String(error.networkResponse.data));
                                Toast.makeText(getApplicationContext(), (String) errorMessage.get("error"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Log.wtf("/login api is reached but there is somethings wrong", e.toString());
                            }
                        }else{
                            Log.e("Error on fetching /login", error.toString());
                        }
                    }
                });
            }
        });
    }
}