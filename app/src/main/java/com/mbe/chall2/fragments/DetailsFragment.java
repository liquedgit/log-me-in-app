package com.mbe.chall2.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.mbe.chall2.LoginActivity;
import com.mbe.chall2.R;
import com.mbe.chall2.helper.EndpointHelper;
import com.mbe.chall2.helper.VolleyCallback;
import com.mbe.chall2.model.Note;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    private static final String ARG_NOTE_ID = "noteId";
    private String noteId;
    private Note note;

    private ImageView imageView;
    private TextView titleTv;
    private TextView descriptionTv;

    public static DetailsFragment newInstance(String noteId) {
        Bundle args = new Bundle();
        DetailsFragment fragment = new DetailsFragment();
        args.putString(ARG_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteId = getArguments().getString(ARG_NOTE_ID);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        imageView = view.findViewById(R.id.image_view);
        titleTv = view.findViewById(R.id.titleTv);
        descriptionTv = view.findViewById(R.id.descriptionTv);

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

        endpointHelper.MakeRequest(requireActivity().getApplicationContext(), Request.Method.GET, "/api/note/"+noteId, new VolleyCallback<JSONObject>(){

            @Override
            public void onSuccess(JSONObject response) {
                try{
                    String id = response.getString("id");
                    String user_id = response.getString("user_id");
                    String title = response.getString("title");
                    String description = response.getString("description");
                    String image_url = response.getString("image_url");
                    note = new Note(id, user_id, title, description, image_url);
                    titleTv.setText(note.getTitle());
                    descriptionTv.setText(note.getDescription());
                    SharedPreferences sp = requireActivity().getApplicationContext().getSharedPreferences(String.valueOf(R.string.shared_preferences), Context.MODE_PRIVATE);
                    String authToken  = sp.getString("token", "");
                    GlideUrl url = new GlideUrl(note.getImageUrl(), new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + authToken)
                            .build());
                    // Load image with custom headers
                    Glide.with(view)
                            .load(url)
                            .into(imageView);
                }catch (JSONException e){
                    Log.e("Error while parsing json", e.toString());
                }
            }

            @Override
            public void onError(VolleyError error) {
                if(error.networkResponse != null){
                    try{
                        JSONObject jsonObj = new JSONObject(new String(error.networkResponse.data));
                        Toast.makeText(requireActivity().getApplicationContext(), jsonObj.getString("error"), Toast.LENGTH_SHORT).show();
                    }catch(JSONException e){
                        Log.e("Error parsing JSON", e.toString());
                    }
                }else{
                    Log.e("Error while fetching", error.toString());
                }
            }
        });



        return view;
    }
}