package com.mbe.chall2.helper;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyCallback<T> {
    void onSuccess(T response);
    void onError(VolleyError error);
}
