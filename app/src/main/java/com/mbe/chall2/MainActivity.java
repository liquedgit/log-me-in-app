package com.mbe.chall2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mbe.chall2.helper.EndpointHelper;
import com.scottyab.rootbeer.RootBeer;

public class MainActivity extends AppCompatActivity {

    private Button startBtn;
    private EditText endPointET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RootBeer rootBeer = new RootBeer(this);
        if(rootBeer.isRooted()){
            Toast.makeText(this, "Application is runned on root device", Toast.LENGTH_SHORT).show();
            finish();
        }
        startBtn = findViewById(R.id.startBtn);
        endPointET = findViewById(R.id.baseUrlET);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartBtnOnClick();
            }
        });
    }

    private void StartBtnOnClick(){
        String endpoint = endPointET.getText().toString();
        if(endpoint.length() == 0){
            Toast.makeText(this, "Endpoint cannot be empty", Toast.LENGTH_SHORT).show();
        }else{
            EndpointHelper endpointHelper = EndpointHelper.GetEndpointHelperInstance();
            endpointHelper.setBaseUrl(endpoint);
            Toast.makeText(this, "Successfully set endpoint", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}