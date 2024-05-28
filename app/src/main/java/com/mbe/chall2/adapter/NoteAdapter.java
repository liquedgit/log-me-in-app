package com.mbe.chall2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mbe.chall2.R;
import com.mbe.chall2.model.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note> notes;

    public NoteAdapter(ArrayList<Note> notes){
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notes, parent);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currNote = notes.get(position);
        holder.titleTv.setText(currNote.getTitle());
        holder.titleTv.setText(currNote.getDescription());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder{
        public TextView titleTv;
        public TextView descTv;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleTv = itemView.findViewById(R.id.note_title);
            this.descTv = itemView.findViewById(R.id.notes_description);
        }
    }

}
