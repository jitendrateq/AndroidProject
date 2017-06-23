package com.example.teqforce.veritalabinsurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewUserVehicleExteriorCondition extends AppCompatActivity {

    Button btnReadInstruction;
    Button btnSampleVideo;
    Button btnCallSupport;
    Button btnStartVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_vehicle_exterior_condition);

        addButtonListener();
    }

    public void addButtonListener() {
        final Context context = this;
        btnReadInstruction=(Button) findViewById(R.id.btn_ReadInst);
        btnSampleVideo=(Button) findViewById(R.id.btn_SampleVideo);
        btnCallSupport=(Button) findViewById(R.id.btn_LiveCall);
        btnStartVideo=(Button) findViewById(R.id.btn_StartVideo);

        btnReadInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, VideoInstructionsSteps.class);
                startActivity(intent);
            }
        });

        btnSampleVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, NewUserCheckSampleVideo.class);
                startActivity(intent);
            }
        });

        btnCallSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, NewUserLiveCallSupport.class);
                startActivity(intent);
            }
        });

        btnStartVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, NewUserStartExteriorVideo.class);
                startActivity(intent);
            }
        });
    }
}
