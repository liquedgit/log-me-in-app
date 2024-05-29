package com.mbe.chall2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.Toast;


public class CreatePostActivity extends AppCompatActivity {

    private Button selectImgBtn;
    private Button createNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        selectImgBtn = findViewById(R.id.selectImageBtn);
        createNoteBtn = findViewById(R.id.create_note_btn);
        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreatePostActivity.this, "Not here find somewhere else", Toast.LENGTH_SHORT).show();
            }
        });
        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreatePostActivity.this, "Not here find somewhere else", Toast.LENGTH_SHORT).show();

            }
        });
    }


    }