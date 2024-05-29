package com.mbe.chall2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.mbe.chall2.R;
import com.mbe.chall2.fragments.DetailsFragment;
import com.mbe.chall2.model.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note> notes;
    private FragmentManager fragmentManager;

    public NoteAdapter(ArrayList<Note> notes, FragmentManager fragmentManager){
        this.notes = notes;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notes, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currNote = notes.get(position);
        holder.titleTv.setText(currNote.getTitle());
        holder.descTv.setText(currNote.getDescription());
        holder.hotspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsFragment detailsFragment = DetailsFragment.newInstance(currNote.getId());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, detailsFragment); // Replace createNoteFragment with detailsFragment
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTv;
        private TextView descTv;
        private LinearLayout hotspot;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleTv = itemView.findViewById(R.id.note_title);
            this.descTv = itemView.findViewById(R.id.notes_description);
            this.hotspot = itemView.findViewById(R.id.hot_spot);
        }
    }

}
