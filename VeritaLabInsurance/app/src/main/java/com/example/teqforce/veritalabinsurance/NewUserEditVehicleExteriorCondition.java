package com.example.teqforce.veritalabinsurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewUserEditVehicleExteriorCondition extends AppCompatActivity {

    Button btnReadInstruction;
    Button btnSampleVideo;
    Button btnCallSupport;
    Button btnStartVideo;

    Button btn_Submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_edit_vehicle_exterior_condition);

        OnButtonListener();
    }

    private void OnButtonListener() {

        final Context context = this;
        btnReadInstruction=(Button) findViewById(R.id.btnReadInstructions);
        btnSampleVideo=(Button) findViewById(R.id.btnSampleVideo);
        btnCallSupport=(Button) findViewById(R.id.btnLiveCallSupport);
        btnStartVideo=(Button) findViewById(R.id.btnTakeNewvideo);
        btn_Submit=(Button) findViewById(R.id.btn_Submit);

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

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, NewUserEditVehicleInteriorCondition.class);
                startActivity(intent);
            }
        });
    }
}
