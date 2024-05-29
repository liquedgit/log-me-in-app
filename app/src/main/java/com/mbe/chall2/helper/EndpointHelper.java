package com.mbe.chall2.helper;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mbe.chall2.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class EndpointHelper {
    private static EndpointHelper instance;
    private String baseUrl;

    private EndpointHelper(String url){
        this.baseUrl = "https://"+url;
    }

    public static EndpointHelper GetEndpointHelperInstance(){
        if(instance == null){
            instance = new EndpointHelper("logmein.chall.liquedgit.com");
        }
        return instance;
    }



    public void setBaseUrl(String baseUrl) {
        this.baseUrl = "https://"+baseUrl;
    }


    public void MakeRequest(Context context,int method, String endpoint, VolleyCallback callback){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonObjectRequest mJsonRequest = new JsonObjectRequest(method, this.baseUrl + endpoint,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onError(error);
                }
            }){
                @Override
                public Map<String, String>getHeaders(){
                    SharedPreferences sp = context.getSharedPreferences(String.valueOf(R.string.shared_preferences), Context.MODE_PRIVATE);
                    String authToken  = sp.getString("token", "");

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " +authToken);
                    return headers;
                }
            };;

        requestQueue.add(mJsonRequest);
    }

    public void MakeRequest(Context context, int method, String endpoint, JSONObject jsonObject, VolleyCallback callback){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(method, this.baseUrl + endpoint, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        }){
            @Override
            public Map<String, String>getHeaders(){
                SharedPreferences sp = context.getSharedPreferences(String.valueOf(R.string.shared_preferences), Context.MODE_PRIVATE);
                String authToken  = sp.getString("token", "");

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " +authToken);
                return headers;
            }
        };
        requestQueue.add(mJsonObjectRequest);
    }




}
