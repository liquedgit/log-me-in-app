package com.mbe.chall2.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mbe.chall2.LoginActivity;
import com.mbe.chall2.R;
import com.mbe.chall2.helper.EndpointHelper;
import com.mbe.chall2.helper.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EndpointHelper endpointHelper = EndpointHelper.GetEndpointHelperInstance();
        endpointHelper.MakeRequest(getActivity().getApplicationContext(), Request.Method.GET, "/api/user", new VolleyCallback<JSONObject>() {

            @Override
            public void onSuccess(JSONObject response) {
                try{
                    String role = response.getString("role");
                    Log.i("Current role", role );
                }catch (JSONException e){
                    Log.e("Error on parsing json", e.toString());
                }
            }

            @Override
            public void onError(VolleyError error) {
                if(error.networkResponse != null){
                    try{
                        JSONObject jsonResponse = new JSONObject(new String(error.networkResponse.data));
                        Toast.makeText(getActivity().getApplicationContext(), jsonResponse.getString("error"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                        Log.e("Successfully reached endpoint but got rejected", error.toString());
                    }catch(JSONException e){
                        Log.e("Error parsing JSON" , e.toString());
                    }

                }else{
                    Log.e("Error on fetching endpoint", error.toString());
                }
            }
        });
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}