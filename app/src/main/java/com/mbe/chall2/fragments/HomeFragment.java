package com.mbe.chall2.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mbe.chall2.CreatePostActivity;
import com.mbe.chall2.R;
import com.mbe.chall2.adapter.NoteAdapter;
import com.mbe.chall2.helper.EndpointHelper;
import com.mbe.chall2.helper.VolleyCallback;
import com.mbe.chall2.model.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Note> notes;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        floatingActionButton = view.findViewById(R.id.floating_action_button);
        notes = new ArrayList<>();


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
                        Log.e("Successfully reached endpoint but got rejected", error.toString());
                    }catch(JSONException e){
                        Log.e("Error parsing JSON" , e.toString());
                    }

                }else{
                    Log.e("Error on fetching endpoint", error.toString());
                }
            }
        });


        endpointHelper.MakeRequest(getActivity().getApplicationContext(), Request.Method.GET, "/api/note", new VolleyCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try{

                    JSONArray jsonArray = response.getJSONArray("notes");
                    Log.wtf("Json array", jsonArray.toString());
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String id = obj.getString("id");
                        String userId = obj.getString("user_id");
                        String title = obj.getString("title");
                        String description = obj.getString("description");
                        String imageUrl = obj.getString("image_url");
                        Note note = new Note(id, userId, title, description, imageUrl);
                        notes.add(note);
                    }
                    Collections.reverse(notes);

                    noteAdapter = new NoteAdapter(notes, requireActivity().getSupportFragmentManager());
                    recyclerView.setAdapter(noteAdapter);

                }catch(Exception e){
                    Log.e("Error parsing JSON" , e.toString());
                }

            }

            @Override
            public void onError(VolleyError error) {
                if(error.networkResponse != null){
                    try{
                        JSONObject jsonResponse = new JSONObject(new String(error.networkResponse.data));
                        Toast.makeText(getActivity().getApplicationContext(), jsonResponse.getString("error"), Toast.LENGTH_SHORT).show();
                        Log.e("Successfully reached endpoint but got rejected", error.toString());
                    }catch(JSONException e){
                        Log.e("Error parsing JSON" , e.toString());
                    }

                }else{
                    Log.e("Error on fetching endpoint", error.toString());
                }
            }
        });




        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), CreatePostActivity.class));
            }
        });


        return view;
    }

}